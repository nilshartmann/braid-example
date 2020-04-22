package nh.graphql.braidexample.shopservice.braid;

import com.atlassian.braid.Braid;
import com.atlassian.braid.SchemaNamespace;
import com.atlassian.braid.TypeRename;
import com.atlassian.braid.source.QueryExecutorSchemaSource;
import com.atlassian.braid.source.SchemaLoader;
import com.atlassian.braid.source.StringSchemaLoader;
import graphql.ExecutionInput;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.execution.AsyncExecutionStrategy;
import graphql.execution.DataFetcherResult;
import graphql.execution.instrumentation.tracing.TracingInstrumentation;
import graphql.schema.GraphQLSchema;
import graphql.servlet.GraphQLQueryInvoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StreamUtils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static com.atlassian.braid.graphql.language.GraphQLNodes.printNode;

@ConditionalOnProperty(value = "braid-example.enable-braid", havingValue = "true")
@Configuration
public class BraidConfiguration {
    private static final Logger log = LoggerFactory.getLogger(BraidConfiguration.class);

    private final SchemaNamespace SHOP_NAMESPACE = SchemaNamespace.of("shop");
    private final SchemaNamespace DELIVERY_NAMESPACE = SchemaNamespace.of("delivery");

    /**
     * Created (and registered) by graphql-spring-boot
     */
    @Bean
    public RemoteRetriever<Object> remoteRetriever() {
        return new RemoteRetriever<>("http://localhost:9080/graphql");
    }

    @Bean
    public GraphQLQueryInvoker braidBasedQueryInvoker(Braid braid) {
        log.info("Using GraphQLQueryInvoker");
        return new BraidBasedGraphQLQueryInvoker(braid);
    }

    @Bean
    public Braid braid(GraphQLSchema schema, final RemoteRetriever<Object> remoteRetriever) {
        log.info("USING BRAID");

        final String localSchema = readResource("/graphql/shopservice.graphqls");
        final String deliverySchema = readResource("/graphql/deliveryservice.schema.json");

        Braid braid = Braid.builder()
            .schemaSource(
                QueryExecutorSchemaSource
                    .builder()
                    .namespace(SHOP_NAMESPACE)
                    .schemaLoader(new StringSchemaLoader(SchemaLoader.Type.IDL, localSchema))
                    .localRetriever(query -> {
                        log.info("Schema {}", schema);
                        GraphQL graphQL = GraphQL.newGraphQL(schema)
                            .build();

                        final String query1 = printNode(query.getQuery());
                        ExecutionInput executionInput = ExecutionInput.newExecutionInput()
                            .operationName(query.getOperationName())
                            .variables(query.getVariables())
                            .query(query1)
                            .build();

                        final ExecutionResult executionResult = graphQL.executeAsync(executionInput).join();

                        // see: https://bitbucket.org/atlassian/graphql-braid/src/cfb65e852baa51bc3d01e7a2c50e621e90674aca/src/test/java/com/atlassian/braid/BraidSchemaConsumerTest.java?at=master#lines-87
                        return new DataFetcherResult<>(
                            executionResult.getData(),
                            executionResult.getErrors()
                        );
                    })
                    .build())
            .schemaSource(
                QueryExecutorSchemaSource
                    .builder()
                    .namespace(DELIVERY_NAMESPACE)
                    .schemaLoader(new StringSchemaLoader(SchemaLoader.Type.INTROSPECTION, deliverySchema))
                    .remoteRetriever(remoteRetriever)
                    .typeRenames(List.of(TypeRename.from("Customer", "DeliveryCustomer")))
                    .build()
            )
            .build();

        return braid;

    }

    private String readResource(String classpathResource) {
        try (InputStream is = getClass().getResourceAsStream(classpathResource)) {
            if (is == null) {
                throw new FileNotFoundException(classpathResource);
            }
            return StreamUtils.copyToString(is, StandardCharsets.UTF_8);

            //
        } catch (IOException ex) {
            throw new IllegalStateException(String.format
                ("Could not read resource '%s' from classpath: %s", classpathResource, ex.getMessage()),
                ex);
        }
    }
}
