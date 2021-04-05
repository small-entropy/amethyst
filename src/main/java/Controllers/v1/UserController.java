package Controllers.v1;
import Services.UserService;
import Transformers.JsonTransformer;
import dev.morphia.Datastore;
import static spark.Spark.*;

/**
 * Class controller of users API
 */
public class UserController {

    /**
     * Method for get routes for users API
     * @param store datastore object (Morphia connection)
     * @param transformer transformer object (for transform answer to JSON)
     */
    public static void routes(Datastore store, JsonTransformer transformer) {
        // Route for work with users list
        get("", (req, res) -> UserService.getList(req, res, store), transformer);
        // Route for register user
        post("/register", (req, res) -> UserService.registerUser(req, res, store), transformer);
        // Route for user login
        post("/login", (req, res) -> UserService.loginUser(req, res, store), transformer);
        // Route for user autologin
        get("/autologin", (req, res) -> UserService.autoLoginUser(req, res, store), transformer);
        // Logout user
        get("/logout", (req, res) -> UserService.logoutUser(req, res, store), transformer);

        // Routes for work with user document
        // Get user document by UUID
        get("/:id", (request, response) -> "Get user data");
        // Update user document by UUID
        put("/:id", (request, response) -> "Update user data");
        // Mark to remove user document by UUID
        delete("/:id", (request, response) -> "Remove user document");

        // Routes for work with user properties
        // Get user properties list by user UUID
        get("/:id/properties", (request, response) -> "User properties list");
        // Create new user property (user find by UUID)
        post("/:id/properties", (request, response) -> "Create user properties");
        // Get user property by UUID (user find by UUID)
        get("/:id/properties/:id", (request, response) -> "Get user property");
        // Update user property by property UUID (user find by UUID)
        put("/:id/properties/:id", (request, response) -> "Update user property");
        // Merk to remove user proeprty by UUID (user find by UUID)
        delete("/:id/properties/:id", (request, response) -> "Remove user property");

        // Routes for work with users orders
        // Get all orders by user UUID
        get("/:id/orders", (request, response) -> "Get users orders list");
        // Create new user order (find user by UUID)
        post("/:id/orders", (request, response) -> "Create user order");
        // Get user order by UUID (find user by UUID)
        get("/:id/orders/:id", (request, response) -> "Get user order");
        // Get update user order by UUID (find user by UUID)
        put("/:id/orders/:id", (request, response) -> "Update user order");
        // Mark to remove user order by UUID (find user by UUID)
        delete("/:id/orders/:id", (request, response) -> "Remove user order");

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
