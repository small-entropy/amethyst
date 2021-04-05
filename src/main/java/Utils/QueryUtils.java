package Utils;
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

public class QueryUtils {
    /**
     * Method for get token from query
     * @param request Spark request object
     * @return token from query string
     */
    public static String getTokenFromQuery(Request request) {
        return request.queryMap().get(queryKeys.AUTHORIZATION_KEY.getValue()).value();
    }
}
