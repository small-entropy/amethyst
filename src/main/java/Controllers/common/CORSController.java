package Controllers.common;

import spark.Request;
import spark.Response;
import static spark.Spark.*;

/**
 * Class for work with CRORS
 * @author small-entropy
 */
public class CORSController {
    
    /**
    * Enum with headers keys for CORS
    * @author small-entropy
    */
    private enum HeadersKeys {
       HEADER_ALLOW_ORIGIN("Access-Control-Allow-Origin"),
       HEADER_ALLOW_METHODS("Access-Control-Allow-Methods"),
       HEADER_ALLOW_HEADERS("Access-Control-Allow-Headers");

       private final String value;

       HeadersKeys(String value) {
           this.value = value;
       }

       public String getValue() {
           return this.value;
       }
   }
    
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
            res.header(
                    HeadersKeys.HEADER_ALLOW_ORIGIN.getValue(), 
                    allowOrigin
            );
            res.header(
                    HeadersKeys.HEADER_ALLOW_METHODS.getValue(), 
                    allowMethods
            );
            res.header(
                    HeadersKeys.HEADER_ALLOW_HEADERS.getValue(), 
                    allowHeaders
            );
        });
    }
}
