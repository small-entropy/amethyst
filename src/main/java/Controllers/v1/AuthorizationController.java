package Controllers.v1;

import Controllers.core.AbstractController;
import DataTransferObjects.RuleDTO;
import Models.Standalones.User;
import Responses.SuccessResponse;
import Services.v1.AuthorizationService;
import Sources.UsersSource;
import Transformers.JsonTransformer;
import Utils.common.HeadersUtils;
import Utils.constants.DefaultActions;
import Utils.constants.DefaultRights;
import Utils.constants.ResponseMessages;
import Utils.v1.RightManager;
import dev.morphia.Datastore;
import static spark.Spark.get;
import static spark.Spark.post;

/**
 * Class with authorization routes
 * @version 1
 * @author entropy
 */
public class AuthorizationController extends AbstractController {
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
            return new SuccessResponse<>(ResponseMessages.REGISTERED.getMessage(), user);
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
            return new SuccessResponse<>(ResponseMessages.LOGIN.getMessage(), user);
        }, transformer);
        
        // Route for change password for user by token
        post("/change-password/:user_id", (req, res) -> {
            // Get rule for get user data
            RuleDTO rule = RightManager.getRuleByRequest_Token(req, source, DefaultRights.USERS.getName(), DefaultActions.READ.getName());
            // Change password
            User user = AuthorizationService.changePassword(req, source, rule);
            // Return success answer
            return new SuccessResponse<>(ResponseMessages.PASSWORD_CHANGED.getMessage(), user);
        }, transformer);

        // Route for user autologin
        get("/autologin", (req, res) -> {
            RuleDTO rule = RightManager.getRuleByRequest_Token(req, source, DefaultRights.USERS.getName(), DefaultActions.READ.getName());
            User user = AuthorizationService.autoLoginUser(req, source, rule);
            return new SuccessResponse<>(ResponseMessages.AUTOLOGIN.getMessage(), user);
        }, transformer);

        // Logout user
        get("/logout", (req, res) -> {
            RuleDTO rule = RightManager.getRuleByRequest_Token(req, source, DefaultRights.USERS.getName(), DefaultActions.READ.getName());
            User user = AuthorizationService.logoutUser(req, source, rule);
            return new SuccessResponse<>(ResponseMessages.LOGOUT.getMessage(), user);
        }, transformer);
    }
}
