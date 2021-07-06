package platform.utils.helpers;
import platform.utils.helpers.QueryManager;
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
        String queryParam = QueryManager.getTokenFromQuery(request);
        if (header != null || queryParam != null) {
            return (header != null) ? header : queryParam;
        } else {
            return null;
        }
    }
}
