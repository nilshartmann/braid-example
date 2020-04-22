package nh.graphql.braidexample.shopservice.controller;

import graphql.ExecutionInput;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Supplier;

public class AbstractController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    protected ExecutionInput executionInput(String query) {
        return ExecutionInput
            .newExecutionInput()
            .query(query)
            .build();
    }

    protected <RETURN> RETURN execute(Supplier<RETURN> f) {
        long s = System.currentTimeMillis();

        RETURN result = f.get();

        long d = System.currentTimeMillis();
        log.info("Took: {}ms", (d - s));

        return result;
    }

}
