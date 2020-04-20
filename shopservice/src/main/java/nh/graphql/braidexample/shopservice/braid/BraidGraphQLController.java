package nh.graphql.braidexample.shopservice.braid;

import com.atlassian.braid.Braid;
import com.atlassian.braid.BraidGraphQL;
import com.atlassian.braid.SchemaNamespace;
import com.atlassian.braid.source.QueryExecutorSchemaSource;
import com.atlassian.braid.source.SchemaLoader;
import com.atlassian.braid.source.StringSchemaLoader;
import com.google.gson.Gson;
import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;


/**
 * Executes a GraphQL Query using Braid (local + remote schema)
 */
@RestController
public class BraidGraphQLController {
    private static final Logger log = LoggerFactory.getLogger(BraidGraphQLController.class);

    @Autowired
    private Braid braid;

    @PostMapping(path = "/braid")
    @ResponseBody
    public Object executeGraphQL(@RequestBody GraphQLRequest params) {
        log.info("RECEIVED QUERY: '{}'", params);

        BraidGraphQL graphql = braid.newGraphQL();

//        GraphQL graphql = GraphQL.newGraphQL(braid.getSchema()).build();

        ExecutionResult result = graphql
            .execute(
                ExecutionInput
                    .newExecutionInput()
                    .query(params.getQuery())
                    .build())
            .join();

        return result.toSpecification();
    }


}