package Controllers.v1;

import Controllers.core.CoreController;
import DTO.RuleDTO;
import Models.User;
import Responses.SuccessResponse;
import Services.v1.AuthorizationService;
import Transformers.JsonTransformer;
import Utils.common.HeadersUtils;
import Utils.v1.RightManager;
import dev.morphia.Datastore;

import static spark.Spark.get;
import static spark.Spark.post;

/**
 * Class with authorization routes
 */
public class AuthorizationController extends CoreController {

    private enum Messages {
        REGISTERED("User successfully registered"),
        AUTOLOGIN("Successfully login by token"),
        LOGIN("Login is success"),
        LOGOUT("User successfully logout");
        private final String message;
        Messages(String message) {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    /**
     * Method with define authorization routes
     * @param store Morphia datastore
     * @param transformer JSON response transformer
     */
    public static void routes(Datastore store, JsonTransformer transformer) {

        // Route for register user
        post("/register", (req, res) -> {
            // Register user by request data
            User user = AuthorizationService.registerUser(req, store);
            // Define index of current token
            final int firstIndex = 0;
            String token = user.getIssuedTokens().get(firstIndex);
            // Set response headers
            res.header(HeadersUtils.getAuthHeaderField(), HeadersUtils.getAuthHeaderValue(token));
            // Return response
            return new SuccessResponse<User>(Messages.REGISTERED.getMessage(), user);
        }, transformer);

        // Route for user login
        post("/login", (req, res) -> {
            // Get rule for user by request
            RuleDTO rule = RightManager.getRuleByRequest_Username(req, store, "users_right", "read");
            // Get user document
            User user = AuthorizationService.loginUser(req, store, rule);
            // Init default user issued token first index
            final int firstIndex = 0;
            // Get token from issued token
            String token = user.getIssuedTokens().get(firstIndex);
            // Set headers for authorization
            res.header(HeadersUtils.getAuthHeaderField(), HeadersUtils.getAuthHeaderValue(token));
            // Return answer
            return new SuccessResponse<User>(Messages.LOGIN.getMessage(), user);
        }, transformer);

        // Route for user autologin
        get("/autologin", (req, res) -> {
            RuleDTO rule = RightManager.getRuleByRequest_Token(req, store, "users_right", "read");
            User user = AuthorizationService.autoLoginUser(req, store, rule);
            return new SuccessResponse<User>(Messages.AUTOLOGIN.getMessage(), user);
        }, transformer);

        // Logout user
        get("/logout", (req, res) -> {
            RuleDTO rule = RightManager.getRuleByRequest_Token(req, store, "users_right", "read");
            User user = AuthorizationService.logoutUser(req, store, rule);
            return new SuccessResponse<User>(Messages.LOGOUT.getMessage(), user);
        }, transformer);
    }
}
