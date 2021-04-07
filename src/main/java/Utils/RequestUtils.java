package Utils;
import spark.Request;

/**
 * Class for work with Spark Request object
 */
public class RequestUtils {
    /**
     * Method for get token from headers or query params
     * @param request Spark request object
     * @return token from headers or query
     */
    public static String getTokenByRequest(Request request) {
        String header = HeadersUtils.getTokenFromHeaders(request);
        String queryParam = QueryUtils.getTokenFromQuery(request);
        if (header != null || queryParam != null) {
            return (header != null) ? header : queryParam;
        } else {
            return null;
        }
    }
}
