package Controllers.v1;

import Controllers.core.CoreController;
import DataTransferObjects.RuleDTO;
import Models.User;
import Responses.SuccessResponse;
import Services.v1.AuthorizationService;
import Sources.UsersSource;
import Transformers.JsonTransformer;
import Utils.common.HeadersUtils;
import Utils.constants.DefaultActions;
import Utils.constants.DefaultRights;
import Utils.v1.RightManager;
import dev.morphia.Datastore;
import static spark.Spark.get;
import static spark.Spark.post;

/**
 * Class with authorization routes
 */
public class AuthorizationController extends CoreController {

    /**
     * Enum for exception messages
     */
    private enum Messages {
        REGISTERED("User successfully registered"),
        AUTOLOGIN("Successfully login by token"),
        LOGIN("Login is success"),
        LOGOUT("User successfully logout");
        // Property for text message
        private final String message;

        /**
         * Constructor fo enum
         * @param message text of message
         */
        Messages(String message) {
            this.message = message;
        }

        /**
         * Getter for message property
         * @return value of message property
         */
        public String getMessage() {
            return message;
        }
    }

    /**
     * Method with define authorization routes
     * @param datastore Morphia datastore
     * @param transformer JSON response transformer
     */
    public static void routes(Datastore datastore, JsonTransformer transformer) {
        
        UsersSource source = new UsersSource(datastore);

        // Route for register user
        post("/register", (req, res) -> {
            // Register user by request data
            User user = AuthorizationService.registerUser(req, source);
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
            RuleDTO rule = RightManager.getRuleByRequest_Username(req, source, DefaultRights.USERS.getName(), DefaultActions.READ.getName());
            // Get user document
            User user = AuthorizationService.loginUser(req, source, rule);
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
            RuleDTO rule = RightManager.getRuleByRequest_Username(req, source, DefaultRights.USERS.getName(), DefaultActions.READ.getName());
            User user = AuthorizationService.autoLoginUser(req, source, rule);
            return new SuccessResponse<User>(Messages.AUTOLOGIN.getMessage(), user);
        }, transformer);

        // Logout user
        get("/logout", (req, res) -> {
            RuleDTO rule = RightManager.getRuleByRequest_Username(req, source, DefaultRights.USERS.getName(), DefaultActions.READ.getName());
            User user = AuthorizationService.logoutUser(req, source, rule);
            return new SuccessResponse<User>(Messages.LOGOUT.getMessage(), user);
        }, transformer);
    }
}
