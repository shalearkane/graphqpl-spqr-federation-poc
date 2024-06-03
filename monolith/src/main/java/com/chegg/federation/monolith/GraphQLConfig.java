package com.chegg.federation.monolith;

import com.chegg.federation.monolith.query.*;
import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.SchemaPrinter;
import io.leangen.graphql.GraphQLSchemaGenerator;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
public class GraphQLConfig {
    private static final Logger LOGGER = LoggerFactory.getLogger(GraphQLConfig.class);

    private final GraphQL graphQL;


    @Autowired
    ProductQuery productQuery;

    @Autowired
    ProductService productService;

    @Autowired
    ReviewQuery reviewQuery;

    @Autowired
    UserQuery userQuery;

    @Autowired
    UserService userService;

    @Autowired
    public GraphQLConfig(ProductQuery productQuery,
                         ProductService productService,
                         ReviewQuery reviewQuery,
                         UserQuery userQuery) {

        //Schema generated from query classes
        GraphQLSchema schema = new GraphQLSchemaGenerator()
                .withBasePackages("io.leangen.spqr.samples.demo")
                .withOperationsFromSingletons(productQuery, productService, reviewQuery, userQuery)
                .generate();
        graphQL = GraphQL.newGraphQL(schema).build();

        LOGGER.info("Generated GraphQL schema using SPQR");
    }

    @PostMapping(value = "/graphql", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Map<String, Object> indexFromAnnotated(@RequestBody Map<String, String> request, HttpServletRequest raw) {
        ExecutionResult executionResult = graphQL.execute(ExecutionInput.newExecutionInput()
                .query(request.get("query"))
                .operationName(request.get("operationName"))
                .context(raw)
                .build());
        return executionResult.toSpecification();
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
