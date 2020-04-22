package nh.graphql.braidexample.shopservice.controller;

import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.execution.AsyncExecutionStrategy;
import graphql.schema.GraphQLSchema;
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
public class SimpleGraphQLController extends AbstractController {
    private static final Logger log = LoggerFactory.getLogger(SimpleGraphQLController.class);

    /** Wird von graphql-spring-boot erzeugt */
    @Autowired
    private GraphQLSchema schema;

    @PostMapping("/graphql-controller")
    public Object execute(@RequestBody GraphQLRequest params) {
        log.info("Executing Query {}", params);

        log.info("Schema {}", schema);

        GraphQL graphql = GraphQL.newGraphQL(schema)
            .build();



        ExecutionResult result = execute(() ->
            graphql.executeAsync(executionInput(params.getQuery())
            )
            .join()
        );

        final Map<String, Object> specCompliantResult = result.toSpecification();

        return specCompliantResult;


    }

}
