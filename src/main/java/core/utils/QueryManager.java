package core.utils;
import spark.Request;

enum queryKeys {
    AUTHORIZATION_KEY("token");

    String value;

    queryKeys(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

public class QueryManager {
    
    private final static String SKIP_KEY = "skip";
    private final static String LIMIT_KEY = "limit";
    private final static int DEFAULT_LIMIT = 10;
    private final static int DEFAULT_SKIP = 0;
    
    
    /**
     * Method for get token from query
     * @param request Spark request object
     * @return token from query string
     */
    public static String getTokenFromQuery(Request request) {
        return request.queryMap().get(queryKeys.AUTHORIZATION_KEY.getValue()).value();
    }
    
    public static int getSkip(Request request) {
        String fromQuery = request
                .queryMap()
                .get(SKIP_KEY)
                .value();
        return (fromQuery == null) 
                ? DEFAULT_SKIP 
                : Integer.parseInt(fromQuery);
    }
    
    public static int getLimit(Request request) {
        String fromQuery = request
                .queryMap()
                .get(LIMIT_KEY)
                .value();
        return (fromQuery == null) 
                ? DEFAULT_LIMIT 
                : Integer.parseInt(fromQuery);
    }
}
