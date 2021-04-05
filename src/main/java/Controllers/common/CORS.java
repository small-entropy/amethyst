package Controllers.common;

import spark.Request;
import spark.Response;
import static spark.Spark.*;

public class CORS {
    private final static String HEADER_ALLOW_ORIGIN = "Access-Control-Allow-Origin";
    private final static String HEADER_ALLOW_METHODS = "Access-Control-Allow-Methods";
    private final static String HEADER_ALLOW_HEADERS = "Access-Control-Allow-Headers";

    public static void enable(String allowOrigin, String allowMethods, String allowHeaders) {
        before((Request req, Response res) -> {
            res.header(CORS.HEADER_ALLOW_ORIGIN, allowOrigin);
            res.header(CORS.HEADER_ALLOW_METHODS, allowMethods);
            res.header(CORS.HEADER_ALLOW_HEADERS, allowHeaders);
        });
    }
}
