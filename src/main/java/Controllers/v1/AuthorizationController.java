package Controllers.v1;

import Controllers.base.BaseAuthorizationController;
import DataTransferObjects.RuleDTO;
import Models.Standalones.User;
import Responses.SuccessResponse;
import Services.v1.AuthorizationService;
import Sources.UsersSource;
import Transformers.JsonTransformer;
import dev.morphia.Datastore;
import static spark.Spark.get;
import static spark.Spark.post;

/**
 * Class with authorization routes
 * @version 1
 * @author entropy
 */
public class AuthorizationController extends BaseAuthorizationController {
    
    /**
     * Method with define authorization routes
     * @param datastore Morphia datastore
     * @param transformer JSON response transformer
     */
    public static void routes(
            Datastore datastore, 
            JsonTransformer transformer
    ) {
       
        UsersSource source = new UsersSource(datastore);

        // Route for register user
        post("/register", (req, res) -> {
            // Register user by request data
            User user = AuthorizationService.registerUser(req, source);
            // Get first token
            String token = user.getFirstToken();
            // Set response headers
            setAuthHeaders(res, token);
            // Return response
            return new SuccessResponse<>(MSG_REGISTERED, user);
        }, transformer);

        // Route for user login
        post("/login", (req, res) -> {
            // Get rule for user by request
            RuleDTO rule = getRule_byUsername(req, source, RULE, READ);
            // Get user document
            User user = AuthorizationService.loginUser(req, source, rule);
            String token = user.getFirstToken();
            // Set headers for authorization
            setAuthHeaders(res, token);
            // Return answer
            return new SuccessResponse<>(MSG_LOGIN, user);
        }, transformer);
        
        // Route for change password for user by token
        post("/change-password/:user_id", (req, res) -> {
            // Get rule for get user data
            RuleDTO rule = getRule(req, source, RULE, READ);
            // Change password
            User user = AuthorizationService.changePassword(req, source, rule);
            // Return success answer
            return new SuccessResponse<>(MSG_PASSWORD_CHANGED, user);
        }, transformer);

        // Route for user autologin
        get("/autologin", (req, res) -> {
            RuleDTO rule = getRule(req, source, RULE, READ);
            User user = AuthorizationService.autoLoginUser(req, source, rule);
            return new SuccessResponse<>(MSG_AUTOLOGIN, user);
        }, transformer);

        // Logout user
        get("/logout", (req, res) -> {
            RuleDTO rule = getRule(req, source, RULE, READ);
            User user = AuthorizationService.logoutUser(req, source, rule);
            return new SuccessResponse<>(MSG_LOGOUT, user);
        }, transformer);
    }
}
