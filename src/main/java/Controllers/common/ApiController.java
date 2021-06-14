package Controllers.common;
import static spark.Spark.*;

/**
 * Common API controller class
 * @author small-entropy
 */
public class ApiController {

    /**
     * Method for call after all api methods calls
     */
    public static void afterCallCommon() {
        // Callback for call after methods
        after("/*", (req, res) -> {
            res.type("application/json");
        });
    }
}
