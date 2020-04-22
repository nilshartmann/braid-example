package nh.graphql.braidexample.shopservice.braid;

import com.atlassian.braid.Braid;
import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.execution.instrumentation.Instrumentation;
import graphql.execution.instrumentation.SimpleInstrumentation;
import graphql.execution.instrumentation.dataloader.DataLoaderDispatcherInstrumentationOptions;
import graphql.execution.preparsed.NoOpPreparsedDocumentProvider;
import graphql.execution.preparsed.PreparsedDocumentProvider;
import graphql.schema.GraphQLSchema;
import graphql.servlet.*;

import javax.security.auth.Subject;
import javax.servlet.http.HttpServletResponse;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.function.Supplier;

public class BraidBasedGraphQLQueryInvoker extends GraphQLQueryInvoker  {

    private final Braid braid;
    private final BatchExecutionHandler batchExecutionHandler;

    public BraidBasedGraphQLQueryInvoker(Braid braid) {
        super(
            DefaultExecutionStrategyProvider::new,
            () -> SimpleInstrumentation.INSTANCE,
            () -> NoOpPreparsedDocumentProvider.INSTANCE,
            DataLoaderDispatcherInstrumentationOptions::newOptions, new DefaultBatchExecutionHandler());

        this.braid = braid;
        this.batchExecutionHandler = new DefaultBatchExecutionHandler();
    }

    /** Copied from superclass. Unchanged */
    @Override
    public ExecutionResult query(GraphQLSingleInvocationInput singleInvocationInput) {
        return query(singleInvocationInput, singleInvocationInput.getExecutionInput());
    }

    /** Copied from superclass. Unchanged */
    @Override
    public void query(GraphQLBatchedInvocationInput batchedInvocationInput, HttpServletResponse response, GraphQLObjectMapper graphQLObjectMapper) {
        batchExecutionHandler.handleBatch(batchedInvocationInput, response, graphQLObjectMapper, this::query);
    }

    /** Copied from superclass. Unchanged */
    private ExecutionResult query(GraphQLInvocationInput invocationInput, ExecutionInput executionInput) {
        if (Subject.getSubject(AccessController.getContext()) == null && invocationInput.getSubject().isPresent()) {
            return Subject.doAs(invocationInput.getSubject().get(), (PrivilegedAction<ExecutionResult>) () -> {
                try {
                    return query(invocationInput.getSchema(), executionInput);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });
        }

        return query(invocationInput.getSchema(), executionInput);
    }

    // BRAID
    private ExecutionResult query(GraphQLSchema schema, ExecutionInput executionInput) {
        return braid.newGraphQL().
            execute(executionInput)
            .join();
    }

//    // GRAPHQL ------------
//    private GraphQL newGraphQL(GraphQLSchema schema, Object context) {
//        ExecutionStrategyProvider executionStrategyProvider = getExecutionStrategyProvider.get();
//        return GraphQL.newGraphQL(schema)
//            .queryExecutionStrategy(executionStrategyProvider.getQueryExecutionStrategy())
//            .mutationExecutionStrategy(executionStrategyProvider.getMutationExecutionStrategy())
//            .subscriptionExecutionStrategy(executionStrategyProvider.getSubscriptionExecutionStrategy())
//            .instrumentation(getInstrumentation(context))
//            .preparsedDocumentProvider(getPreparsedDocumentProvider.get())
//            .build();
//    }
}
