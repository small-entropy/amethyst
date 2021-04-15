package Controllers.v1;

import DTO.RuleDTO;
import Models.User;
import Responses.StandardResponse;
import Services.UserService;
import Transformers.JsonTransformer;
import Utils.HeadersUtils;
import Utils.RightManager;
import dev.morphia.Datastore;

import static spark.Spark.get;
import static spark.Spark.post;

/**
 * Class with authorization routes
 */
public class AuthorizationController {

    /**
     * Method with define authorization routes
     * @param store Morphia datastore
     * @param transformer JSON response transformer
     */
    public static void routes(Datastore store, JsonTransformer transformer) {
        // Route for register user
        post("/register", (req, res) -> {
            // Register user by request data
            User user = UserService.registerUser(req, store);
            // Set response status field
            String status = "success";
            // Set response message field
            String message = "User registered";
            // Define index of current token
            final int firstIndex = 0;
            // Get current token from user document
            String token = user.getIssuedTokens().get(firstIndex);
            // Set response headers
            res.header(
                    HeadersUtils.getAuthHeaderField(),
                    HeadersUtils.getAuthHeaderValue(token)
            );
            // Return response
            return new StandardResponse<User>(status, message, user);
        }, transformer);
        // Route for user login
        post("/login", (req, res) -> {
            // Get rule for user by request
            RuleDTO rule = RightManager.getRuleByRequest_Username(req, store, "users_right", "read");
            // Get user document
            User user = UserService.loginUser(req, store, rule);
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
            RuleDTO rule = RightManager.getRuleByRequest_Token(req, store, "users_right", "read");
            User user = UserService.autoLoginUser(req, store, rule);
            String status = (user != null) ? "success" : "fail";
            String message = (user != null)
                    ? "Successfully autologin by token"
                    : "Can not autologin by token";
            return new StandardResponse<User>(status, message, user);
        }, transformer);
        // Logout user
        get("/logout", (req, res) -> {
            RuleDTO rule = RightManager.getRuleByRequest_Token(req, store, "users_right", "read");
            User user = UserService.logoutUser(req, store, rule);
            String status = (user != null) ? "success" : "fail";
            String message = (user != null)
                    ? "Successfully logout"
                    : "Can not logout";
            return new StandardResponse<User>(status, message, user);
        }, transformer);
    }
}
