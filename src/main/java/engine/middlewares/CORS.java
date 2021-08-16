package engine.middlewares;

import spark.Request;
import spark.Response;

import static spark.Spark.*;

/**
 * Class for work with CRORS
 * @author small-entropy
 */
public class CORS {
    
    private final static String HEADER_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    private final static String HEADER_ALLOW_METHODS = "Access-Control-Allow-Methods";
    private final static String HEADER_ALLOW_HEADERS = "Access-Control-Allow-Headers";

    /**
     * Method for enable headers for CORS
     * @param allowOrigin allowed origin
     * @param allowMethods allowed methods
     * @param allowHeaders allowed headers
     */
    public static void enable(
            String allowOrigin, 
            String allowMethods, 
            String allowHeaders
    ) {
        // Call callback before call request handlers
        before((Request req, Response res) -> {
            res.header(HEADER_ALLOW_ORIGIN, allowOrigin);
            res.header(HEADER_ALLOW_METHODS, allowMethods);
            res.header(HEADER_ALLOW_HEADERS, allowHeaders);
        });
    }
}
