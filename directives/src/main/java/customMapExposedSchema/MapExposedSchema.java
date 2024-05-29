package customMapExposedSchema;

import graphql.introspection.Introspection;
import graphql.schema.GraphQLArgument;
import graphql.schema.GraphQLDirective;
import graphql.schema.GraphQLScalarType;
import graphql.schema.GraphQLSchema;
import io.leangen.graphql.GraphQLSchemaGenerator;

public class MapExposedSchema {
    public static GraphQLSchema customSchema(Object query, String basePackageName) {

        GraphQLSchema schema = new GraphQLSchemaGenerator()
                .withBasePackages(basePackageName)
                .withOperationsFromSingletons(query)
                .generate();

        // UNREPRESENTABLE scalar
        GraphQLScalarType unrepresentableScalar = (GraphQLScalarType) schema.getType("UNREPRESENTABLE");

        // _mappedType directive definition
        GraphQLDirective mappedTypeDirective = GraphQLDirective.newDirective()
                .name("_mappedType")
                .description("")
                .validLocation(Introspection.DirectiveLocation.OBJECT)
                .argument(GraphQLArgument.newArgument()
                        .name("type")
                        .description("")
                        .type(unrepresentableScalar)
                        .build()
                )
                .build();

        // _mappedOperation directive definition
        GraphQLDirective mappedOperationDirective = GraphQLDirective.newDirective()
                .name("_mappedOperation")
                .description("")
                .validLocation(Introspection.DirectiveLocation.FIELD_DEFINITION)
                .argument(GraphQLArgument.newArgument()
                        .name("operation")
                        .description("")
                        .type(unrepresentableScalar)
                        .build()
                )
                .build();

        // _mappedInputField directive definition
        GraphQLDirective mappedInputFieldDirective = GraphQLDirective.newDirective()
                .name("_mappedInputField")
                .description("")
                .validLocation(Introspection.DirectiveLocation.INPUT_FIELD_DEFINITION)
                .argument(GraphQLArgument.newArgument()
                        .name("inputField")
                        .description("")
                        .type(unrepresentableScalar)
                        .build()
                )
                .build();

        // Add new definitions to schema
        GraphQLSchema newSchema = GraphQLSchema.newSchema(schema)
                .additionalDirective(mappedTypeDirective)
                .additionalDirective(mappedOperationDirective)
                .additionalDirective(mappedInputFieldDirective)
                .build();

        return newSchema;
    }
}
