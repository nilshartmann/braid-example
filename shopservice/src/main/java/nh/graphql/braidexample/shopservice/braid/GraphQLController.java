package nh.graphql.braidexample.shopservice.braid;

import com.atlassian.braid.Braid;
import com.atlassian.braid.BraidGraphQL;
import com.atlassian.braid.Link;
import com.atlassian.braid.SchemaNamespace;
import com.atlassian.braid.source.QueryExecutorSchemaSource;
import com.atlassian.braid.source.SchemaLoader;
import com.atlassian.braid.source.StringSchemaLoader;
import com.google.gson.Gson;
import graphql.ExecutionInput;
import graphql.ExecutionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.function.Supplier;

@Controller
public class GraphQLController {
    private static final Logger log = LoggerFactory.getLogger( GraphQLController.class );
    private final SchemaNamespace SHOP_NAMESPACE = SchemaNamespace.of("shop");

//    private final SchemaNamespace NODE2 = SchemaNamespace.of("graphql-node2");
//    private final String NODE2_SCHEMA_URL = "https://localhost:8444/bms";

    @PostMapping(path="/braid", consumes="application/json", produces="application/json")
    public @ResponseBody
    String graphql(@RequestBody GraphQLParameters params) {
        log.info("RECEIVED QUERY: '{}'", params);
        Gson gson = new Gson();
//        Supplier<Reader> usersSchemaProvider = () -> new RemoteIntrospection(BASICS_SCHEMA_URL).get();
//        Supplier<Reader> ordersSchemaProvider = () -> new RemoteIntrospection(NODE2_SCHEMA_URL).get();
//
//        ArrayList<Link> links = new ArrayList();
//        links.add(Link.from(NODE2, "basicnode", "node2").to(BASIC_NODE, "User").build());

        final String localSchema = readLocalSchema();

        Braid braid = Braid
            .builder()
            .schemaSource(
                QueryExecutorSchemaSource
                    .builder()
                    .namespace(SHOP_NAMESPACE)
                    .schemaLoader(new StringSchemaLoader(SchemaLoader.Type.IDL, localSchema))
                    .localRetriever( query -> {
                        log.info("QUERY =================>>>>>>>> '{}'", query);
                        return null;
                    })
                    .build())
//            .schemaSource(
//                QueryExecutorSchemaSource
//                    .builder()
//                    .namespace(NODE2)
//                    .schemaProvider(ordersSchemaProvider)
//                    .remoteRetriever(new RemoteRetriever(NODE2_SCHEMA_URL))
//                    .links(links)
//                    .build())
            .build();

        BraidGraphQL graphql = braid.newGraphQL();

        ExecutionResult result = graphql
            .execute(
                ExecutionInput
                    .newExecutionInput()
                    .query(params.getQuery())
                    .build())
            .join();

        return gson.toJson(result.toSpecification());
    }

    private String readLocalSchema() {
        try (InputStream is = getClass().getResourceAsStream("/graphql/shopservice.graphqls")) {
            return StreamUtils.copyToString(is, StandardCharsets.UTF_8);

        } catch (IOException ex) {
            throw new IllegalStateException(ex);
        }
    }
}