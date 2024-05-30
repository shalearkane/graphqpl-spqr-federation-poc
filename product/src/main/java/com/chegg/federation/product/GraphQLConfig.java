package com.chegg.federation.product;

import com.apollographql.federation.graphqljava.Federation;
import com.apollographql.federation.graphqljava._Entity;
import com.chegg.federation.product.model.Product;
import com.chegg.federation.product.query.ProductQuery;
import com.chegg.federation.product.query.ProductService;
import customMapExposedSchema.MapExposedSchema;
import directives.FederationKeyDirective;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.SchemaPrinter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.*;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.web.context.support.StandardServletEnvironment;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;

@Configuration
public class GraphQLConfig {

    @Autowired
    ProductQuery productQuery;

    @Autowired
    ProductService productService;
    private static final String MAPPED_TYPE = "_mappedType";
    private static final String TYPE = "type";

    private Class<?> extractBeanClass(BeanDefinition beanDefinition) {
        try {
            return ((ScannedGenericBeanDefinition) beanDefinition)
                    .resolveBeanClass(this.getClass().getClassLoader());
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    @Bean
    public GraphQLSchema createSchemaWithDirectives() throws InvocationTargetException, IllegalAccessException, NoSuchMethodException {
        GraphQLSchema schema = MapExposedSchema.customSchema(productQuery, "com.chegg.federation.product");

        ClassPathScanningCandidateComponentProvider scanner =
                new ClassPathScanningCandidateComponentProvider(false);

        scanner.addIncludeFilter(new AnnotationTypeFilter(FederationKeyDirective.class));

        HashMap<String, String> EntityKeyMap = new HashMap<>();
        HashMap<String, Class<?>> EntityBeanMap = new HashMap<>();
        for (BeanDefinition bd : scanner.findCandidateComponents("com.chegg.federation.product")) {
            System.out.println(bd.getBeanClassName());
            final String EntityName = bd.getBeanClassName().substring(bd.getBeanClassName().lastIndexOf('.') + 1);
            ScannedGenericBeanDefinition bds = (ScannedGenericBeanDefinition) bd;
            Class c = extractBeanClass(bd);
            System.out.println(c.getMethod("lookup", null).invoke(null));
//            System.out.println(bds.getClass().getMethod("lookup", null));
            System.out.println(((ScannedGenericBeanDefinition) bd)
                    .getMetadata()
                    .getAnnotationAttributes(FederationKeyDirective.class.getName()).toString());

            final Map<String, Object> MapsForFederation = ((ScannedGenericBeanDefinition) bd)
                    .getMetadata()
                    .getAnnotationAttributes(FederationKeyDirective.class.getName());

            final String KeyFields = (String) MapsForFederation.get("fields");
            EntityKeyMap.put(EntityName, KeyFields);
            EntityBeanMap.put(EntityName, extractBeanClass(bd));
        }

        GraphQLSchema federationSchema1 = Federation.transform(schema).fetchEntities(env -> env.<List<Map<String, Object>>>getArgument(_Entity.argumentName)
                        .stream()
                        .map(values -> {
                            final String EntityClass = (String) values.get("__typename");
                            if(EntityKeyMap.containsKey(values.get("__typename"))) {
                                final Object key =values.get(EntityKeyMap.get(EntityClass));
                                if(key instanceof String) {
                                    final Class<?> c = EntityBeanMap.get(EntityClass);
                                    if (c != null) {
                                        try {
                                            return c.getMethod("lookup", null).invoke(null);
                                        } catch (Exception e) {
                                            throw new RuntimeException(e);
                                        }
                                    }
                                }
                            }
                            return null;
                        })
                        .collect(Collectors.toList()))
                .resolveEntityType(env -> {
                    final String EntityNameInProject = env.getObject().getClass().getName();
                    final String EntityName = EntityNameInProject.substring(EntityNameInProject.lastIndexOf('.') + 1);
                    if (EntityKeyMap.containsKey(EntityName)) {
                        return env.getSchema().getObjectType(EntityName);
                    }
                    return null;
                }).build();

        return federationSchema1;
    }

    private void printSchema(GraphQLSchema schema) {
        System.out.println("Schema With Federation >>>");
        String printedSchema = new SchemaPrinter(
                // Tweak the options accordingly
                SchemaPrinter.Options.defaultOptions().
                        includeDirectives(true)
        ).print(schema);


        System.out.println(printedSchema);
        System.out.println(" >>>>>>>>>>>    ");

    }
}
