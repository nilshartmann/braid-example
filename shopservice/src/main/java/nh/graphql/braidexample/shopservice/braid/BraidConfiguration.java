package nh.graphql.braidexample.shopservice.braid;

import com.atlassian.braid.Braid;
import com.atlassian.braid.SchemaNamespace;
import com.atlassian.braid.TypeRename;
import com.atlassian.braid.source.GraphQLRemoteRetriever;
import com.atlassian.braid.source.QueryExecutorSchemaSource;
import com.atlassian.braid.source.SchemaLoader;
import com.atlassian.braid.source.StringSchemaLoader;
import graphql.ExecutionResult;
import graphql.GraphQL;
import graphql.schema.GraphQLSchema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StreamUtils;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Configuration
public class BraidConfiguration {

    private static final Logger log = LoggerFactory.getLogger(BraidConfiguration.class);

    private final SchemaNamespace SHOP_NAMESPACE = SchemaNamespace.of("shop");
    private final SchemaNamespace DELIVERY_NAMESPACE = SchemaNamespace.of("delivery");

    /**
     * Wird von graphql-spring-boot erzeugt
     */
    @Autowired
    private GraphQLSchema schema;

    @Bean
    public RemoteRetriever<Object> remoteRetriever() {
        return new RemoteRetriever<>("http://localhost:9080/graphql");
    }

    @Bean
    public Braid braid(final RemoteRetriever<Object> remoteRetriever) {

        final String localSchema = readResource("/graphql/shopservice.graphqls");
        final String deliverySchema = readResource("/graphql/deliveryservice.schema.json");


        // ACHTUNG! todo: remote aufruf beim Starten des Sevices. Was passiert, wenn aufgerufener Service
        //                nicht erreichbar ist
//        final String remoteSchema = remoteRetriever.executeIntrospectionQuery();
        log.info("Remote deliverySchema Schema: {}", deliverySchema);

        Braid braid = Braid
            .builder()
            .schemaSource(
                QueryExecutorSchemaSource
                    .builder()
                    .namespace(SHOP_NAMESPACE)
                    // todo: könnte man auch das GraphQLSchema direkt verwenden?
                    .schemaLoader(new StringSchemaLoader(SchemaLoader.Type.IDL, localSchema))
                    .localRetriever(query -> {
                        GraphQL graphQL = GraphQL.newGraphQL(schema).build();
                        final ExecutionResult executionResult = graphQL.execute(query.asExecutionInput());

                        // gefährliches Halbwissen
                        // siehe: https://bitbucket.org/atlassian/graphql-braid/src/cfb65e852baa51bc3d01e7a2c50e621e90674aca/src/test/java/com/atlassian/braid/BraidSchemaConsumerTest.java?at=master#lines-87
                        return executionResult.getData();
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
