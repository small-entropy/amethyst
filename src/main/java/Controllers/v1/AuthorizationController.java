package Controllers.v1;

import Controllers.base.BaseAuthorizationController;
import Models.Standalones.User;
import Utils.responses.SuccessResponse;
import Services.v1.AuthorizationService;
import Utils.transformers.JsonTransformer;
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
       
        AuthorizationService service = new AuthorizationService(datastore);

        // Route for register user
        post("/register", (req, res) -> {
            // Register user by request data
            User user = service.registerUser(req);
            // Get first token
            String token = user.getFirstToken();
            // Set response headers
            setAuthHeaders(res, token);
            // Return response
            return new SuccessResponse<>(MSG_REGISTERED, user);
        }, transformer);

        // Route for user login
        post("/login", (req, res) -> {
            // Get user document
            User user = service.loginUser(req, RULE, READ);
            String token = user.getFirstToken();
            // Set headers for authorization
            setAuthHeaders(res, token);
            // Return answer
            return new SuccessResponse<>(MSG_LOGIN, user);
        }, transformer);
        
        // Route for change password for user by token
        post("/change-password/:user_id", (req, res) -> {
            // Change password
            User user = service.changePassword(req, RULE, READ);
            // Return success answer
            return new SuccessResponse<>(MSG_PASSWORD_CHANGED, user);
        }, transformer);

        // Route for user autologin
        get("/autologin", (req, res) -> {
            User user = service.autoLoginUser(req, RULE, READ);
            return new SuccessResponse<>(MSG_AUTOLOGIN, user);
        }, transformer);

        // Logout user
        get("/logout", (req, res) -> {
            User user = service.logoutUser(req, RULE, READ);
            return new SuccessResponse<>(MSG_LOGOUT, user);
        }, transformer);
    }
}
