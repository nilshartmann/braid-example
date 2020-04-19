package nh.graphql.braidexample.shopservice.braid;

import com.atlassian.braid.source.Query;

import java.util.Map;

public class GraphQLRequest {

    private final String operationName;
    private final String query;
    private final Map<String, Object> variables;

    GraphQLRequest(String operationName, String query, Map<String, Object> variables) {
        this.operationName = operationName;
        this.query = query;
        this.variables = variables;
    }

    public static GraphQLRequest forQuery(String query) {
        return new GraphQLRequest(null, query, null);
    }

    public static GraphQLRequest forQuery(Query query) {
        return new GraphQLRequest(
            query.getOperationName(),
            query.asExecutionInput().getQuery(),
            query.getVariables()
        );
    }

    public String getOperationName() {
        return operationName;
    }

    public String getQuery() {
        return query;
    }

    public Map<String, Object> getVariables() {
        return variables;
    }

}