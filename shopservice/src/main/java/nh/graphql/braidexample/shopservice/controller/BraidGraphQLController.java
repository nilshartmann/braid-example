package nh.graphql.braidexample.shopservice.controller;

import com.atlassian.braid.Braid;
import com.atlassian.braid.BraidGraphQL;
import graphql.ExecutionResult;
import nh.graphql.braidexample.shopservice.braid.GraphQLRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;


/**
 * Executes a GraphQL Query using Braid (local + remote schema), only available if braid-example.enable-braid
 * is set to true in application.properties
 */
@ConditionalOnBean(name = {"braid"})
@RestController
public class BraidGraphQLController extends AbstractController {
    private static final Logger log = LoggerFactory.getLogger(BraidGraphQLController.class);

    @Autowired
    private Braid braid;

    @PostMapping(path = "/braid")
    @ResponseBody
    public Object executeGraphQL(@RequestBody GraphQLRequest params) {
        log.info("RECEIVED QUERY: '{}'", params);

        BraidGraphQL graphql = braid.newGraphQL();

        ExecutionResult result = execute(() -> graphql
            .execute(executionInput(params.getQuery())
            )
            .join()
        );

        return result.toSpecification();
    }


}