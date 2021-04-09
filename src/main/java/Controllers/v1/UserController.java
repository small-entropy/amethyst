package Controllers.v1;
import Models.User;
import Models.UserProperty;
import Responses.StandardResponse;
import Services.UserProfileService;
import Services.UserPropertyService;
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
            List<User> users = UserService.getList(req, store);
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
            User user = UserService.registerUser(req, store);
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
            User user = UserService.loginUser(req, store);
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
            User user = UserService.autoLoginUser(req, store);
            String status = (user != null) ? "success" : "fail";
            String message = (user != null)
                    ? "Successfully autologin by token"
                    : "Can not autologin by token";
            return new StandardResponse<User>(status, message, user);
        }, transformer);

        // Logout user
        get("/logout", (req, res) -> {
            User user = UserService.logoutUser(req, store);
            String status = (user != null) ? "success" : "fail";
            String message = (user != null)
                    ? "Successfully logout"
                    : "Can not logout";
            return new StandardResponse<User>(status, message, user);
        }, transformer);

        // Routes for work with user document
        // Get user document by UUID
        get("/:id", (req, res) -> {
            User user = UserService.getUserById(req, store);
            String status = (user != null) ? "success" : "fails";
            String message = (user != null)
                    ? "Successfully user found"
                    : "Can not find user";
            return new StandardResponse<User>(status, message, user);
        }, transformer);

        // Update user document by UUID
        put("/:id", (request, response) -> UserService.updateUser());

        // Mark to remove user document by UUID
        delete("/:id", (req, res) -> {
            User user = UserService.markToRemove(req, store);
            String status = (user != null) ? "success" : "fail";
            String message = (user != null)
                    ? "User successfully deactivated"
                    : "Can not deactivate user";
            return new StandardResponse<User>(status, message, user);
        }, transformer);

        // Routes for work with user properties
        // Get user properties list by user UUID
        get("/:id/properties", (req, res) -> {
            List<UserProperty> properties = UserPropertyService.getUserProperties(req, store);
            String status = (properties != null) ? "success" : "fail";
            String message = (properties != null)
                    ? "User properties found"
                    : "Can not find user properties";
            return new StandardResponse<List<UserProperty>>(status, message, properties);
        }, transformer);

        // Create new user property (user find by UUID)
        // This method only for create public property!
        // For create not public property use other method!
        post("/:id/properties", (req, res) -> {
            UserProperty userProperty = UserPropertyService.createUserProperty(req, store);
            String status = (userProperty != null) ? "success" : "fail";
            String message = (userProperty != null)
                    ? "Successfully create user property"
                    : "Can not create user property";
            return new StandardResponse<UserProperty>(status, message, userProperty);
        }, transformer);

        // Get user property by UUID (user find by UUID)
        get("/:id/properties/:property_id", (req, res) -> {
            UserProperty property = UserPropertyService.getUserPropertyById(req, store);
            String status = (property != null) ? "success" : "fail";
            String message = (property != null)
                    ? "User property found"
                    : "Can not find user property";
            return new StandardResponse<UserProperty>(status, message, property);
        }, transformer);

        // Update user property by property UUID (user find by UUID)
        put("/:id/properties/:property_id", (request, response) -> "Update user property");
        // Remove user property by UUID (user find by UUID)
        delete("/:id/properties/:property_id", (request, response) -> "Remove user property");

        // Route for get user profile
        get("/:id/profile", (req, res)-> {
            List<UserProperty> profile = UserProfileService.getUserProfile(req, store);
            String status = (profile != null) ? "success" : "fail";
            String message = (profile != null)
                    ? "Successfully found user profile"
                    : "Can not found user profile";
            return new StandardResponse<List<UserProperty>>(status, message, profile);
        }, transformer);

        // Route for get user profile property by ID
        get("/:id/profile/:property_id", (req, res) -> {
            UserProperty property = UserProfileService.getUserProfilePropertyById(req, store);
            String status = (property != null) ? "success" : "fail";
            String message = (property != null)
                    ? "Founded profile property"
                    : "Can not found profile property";
            return new StandardResponse<UserProperty>(status, message, property);
        }, transformer);
        // Route for create profile property
        post("/:id/profile", (req, res) -> {
            UserProperty property = UserProfileService.createUserProfileProperty(req, store);
            String status = (property != null) ? "success" : "fail";
            String message = (property != null)
                    ? "Successfully created profile property"
                    : "Can not create profile property";
            return new StandardResponse<UserProperty>(status, message, property);
        }, transformer);

        // Update user profile property by property UUID (user find by UUID)
        put("/:id/properties/:property_id", (request, response) -> "Update user profile property");
        // Remove user profile property by UUID (user find by UUID)
        delete("/:id/properties/:property_id", (request, response) -> "Remove user profile property");


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
