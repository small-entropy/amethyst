package platform.middlewares;
import static spark.Spark.*;

/**
 * Common API controller class
 * @author small-entropy
 */
public class ResponseTypeJSON {

    /**
     * Method for call after all api methods calls
     */
    public static void afterCall() {
        // Callback for call after methods
        after("/*", (req, res) -> {
            res.type("application/json");
        });
    }
}
