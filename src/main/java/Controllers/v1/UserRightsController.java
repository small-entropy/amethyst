package Controllers.v1;

import Transformers.JsonTransformer;
import dev.morphia.Datastore;

import static spark.Spark.*;

/**
 * Static class for work with user right document
 */
public class UserRightsController {
    public static void routes (Datastore store, JsonTransformer transformer) {
        // Routes for work with user rights
        // Get all rights by user UUID
        get("/:id/rights", (request, response) -> "Get user rights");
        // Create new user rights (user find by UUID)
        post("/:id/rights", (request, response) -> "Create user right");
        // Get new user right by UUID (find user by UUID)
        get("/:id/rights/:id", (request, response) -> "Get user right");
        // Update user right by UUID (find user by UUID)
        put("/:id/rights/:id", (request, response) -> "Update user right");
        // Mark to remove user right (find user by UUID)
        delete("/:id/rights/:id", (request, response) -> "Remove user right");
    }
}
