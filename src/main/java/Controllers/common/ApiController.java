package Controllers.common;
import spark.Request;
import spark.Response;

import static spark.Spark.*;

public class ApiController {
    /**
     * Method for call after all api methods calls
     */
    public static void afterCall() {
        // Callback for call after methods
        after("/*", (Request req, Response res) -> {
            res.type("application/json");
        });
    }
}
