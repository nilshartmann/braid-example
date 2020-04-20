package nh.graphql.braidexample.shopservice.graphql;

import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import graphql.servlet.GraphQLSchemaProvider;
import nh.graphql.braidexample.shopservice.braid.GraphQLRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * Executes a GraphQL query against the LOCAL schema (without Braid)
 */
@RestController
public class SimpleGraphQLController {
    private static final Logger log = LoggerFactory.getLogger(SimpleGraphQLController.class);

    /** Wird von graphql-spring-boot erzeugt */
    @Autowired
    private GraphQLSchema schema;

    @PostMapping("/graphql-example")
    public Object execute(@RequestBody GraphQLRequest parameters) {
        log.info("Executing Query {}", parameters);

        log.info("Schema {}", schema);

        GraphQL graphQL = GraphQL.newGraphQL(schema).build();

        final ExecutionResult executionResult = graphQL.execute(parameters.getQuery());

        final Map<String, Object> specCompliantResult = executionResult.toSpecification();

        return specCompliantResult;


    }

}
