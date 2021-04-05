package Controllers.v1;
import Models.User;
import Responses.StandardResponse;
import Services.UserService;
import Transformers.JsonTransformer;
import Utils.HeadersUtils;
import dev.morphia.Datastore;

import java.util.List;

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
        get("", (req, res) -> {
            List<User> users = UserService.getList(req, res, store);
            // Check users list size.
            // If size equal - fail method status & message
            // If size not equal - success method status & message
            boolean isEmptyUsers = users.size() != 0;
            // Set status, message
            String status = isEmptyUsers ? "success" : "fail";
            String message = isEmptyUsers ? "Success finding users" : "Can not finding users";
            return new StandardResponse<List<User>>(status, message, users);
        }, transformer);

        // Route for register user
        post("/register", (req, res) -> {
            User user = UserService.registerUser(req, res, store);
            String status = "success";
            String message = "User registered";
            final int firstIndex = 0;
            String token = user.getIssuedTokens().get(firstIndex);
            res.header(
                    HeadersUtils.getAuthHeaderField(),
                    HeadersUtils.getAuthHeaderValue(token)
            );
            return new StandardResponse<User>(status, message, user);
        }, transformer);

        // Route for user login
        post("/login", (req, res) -> {
            User user = UserService.loginUser(req, res, store);
            String status;
            String message;
            if (user != null) {
                status = "success";
                message = "Login is success";
                final int firstIndex = 0;
                String token = user.getIssuedTokens().get(firstIndex);
                res.header(
                        HeadersUtils.getAuthHeaderField(),
                        HeadersUtils.getAuthHeaderValue(token)
                );
            } else {
                status = "fail";
                message = "Can not auth by current username & password";
            }
            return new StandardResponse<User>(status, message, user);
        }, transformer);

        // Route for user autologin
        get("/autologin", (req, res) -> {
            User user = UserService.autoLoginUser(req, res, store);
            String status = (user != null) ? "success" : "fail";
            String message = (user != null)
                    ? "Successfully autologin by token"
                    : "Can not autologin by token";
            return new StandardResponse<User>(status, message, user);
        }, transformer);

        // Logout user
        get("/logout", (req, res) -> {
            User user = UserService.logoutUser(req, res, store);
            String status = (user != null) ? "success" : "fail";
            String message = (user != null)
                    ? "Successfully logout"
                    : "Can not logout";
            return new StandardResponse<User>(status, message, user);
        }, transformer);

        // Routes for work with user document
        // Get user document by UUID
        get("/:id", (req, res) -> {
            User user = UserService.getUserByUuid(req, res, store);
            String status = (user != null) ? "success" : "fails";
            String message = (user != null)
                    ? "Successfully user finded"
                    : "Can not find user";
            return new StandardResponse<User>(status, message, user);
        }, transformer);
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
        // Merk to remove user property by UUID (user find by UUID)
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
