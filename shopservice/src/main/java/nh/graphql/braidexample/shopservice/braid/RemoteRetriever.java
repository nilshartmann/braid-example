package nh.graphql.braidexample.shopservice.braid;

import com.atlassian.braid.source.GraphQLRemoteRetriever;
import com.atlassian.braid.source.Query;
import com.google.gson.Gson;
import graphql.ExecutionInput;
import graphql.introspection.IntrospectionQuery;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class RemoteRetriever<C> implements GraphQLRemoteRetriever<C> {
    
    private static final Logger log = LoggerFactory.getLogger(RemoteRetriever.class );
    
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private final String url;

    RemoteRetriever(String url) {
        this.url = url;
    }

    @Override
    public CompletableFuture<Map<String, Object>> queryGraphQL(Query query, C c) {

        Gson gson = new Gson();
        OkHttpClient client = new OkHttpClient();

        GraphQLRequest graphQLRequest = GraphQLRequest.forQuery(query);

        String json = gson.toJson(graphQLRequest);
        log.info("Sending GraphQL Query '{}' ", json);
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        try {
            Response response = client.newCall(request).execute();
            final String string = response.body().string();
            log.info("Received GraphQL Response '{}'", string);

            HashMap<String, Object> jsonResult = gson.fromJson(string, HashMap.class);
            return CompletableFuture.completedFuture(jsonResult);
        } catch(IOException error) {
            log.error("Fehler beim Ausführen des remote GraphQL Requests: {}", error.getMessage());
            throw new IllegalStateException(error);
        }
    }
}