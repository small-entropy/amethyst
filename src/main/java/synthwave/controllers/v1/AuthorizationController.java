package synthwave.controllers.v1;

import synthwave.controllers.messages.AuthorizationMessages;
import synthwave.models.mongodb.standalones.User;
import platform.constants.DefaultRights;
import platform.controllers.BaseController;
import platform.utils.helpers.HeadersUtils;
import platform.utils.responses.SuccessResponse;
import synthwave.services.v1.AuthorizationService;
import platform.utils.transformers.JsonTransformer;
import spark.Response;
import dev.morphia.Datastore;
import static spark.Spark.get;
import static spark.Spark.post;

/**
 * Class with authorization routes
 * @version 1
 * @author small-entropy
 */
public class AuthorizationController 
	extends BaseController<AuthorizationService, JsonTransformer> {

	/**
	 * Default constructor for authorization controller
	 * @param datastore Morphia datastore object
	 * @param transformer transformer object
	 */
	public AuthorizationController(
			Datastore datastore,
			JsonTransformer transformer
	) {
		super(
				new AuthorizationService(datastore), 
				transformer,
				DefaultRights.USERS.getName()
		);
	}
	
	/**
     * Method for set authorization headers in response
     * @param response Spark response object
     * @param token user JSON web token
     */
    protected static void setAuthHeaders(Response response, String token) {
        response.header(
                HeadersUtils.getAuthHeaderField(),
                HeadersUtils.getAuthHeaderValue(token)
        );
    }
	
    /**
     * Method with registration route
     */
	protected void registerRoute() {
        post("/register", (req, res) -> {
            User user = getService().registerUser(req);
            String token = user.getFirstToken();
            setAuthHeaders(res, token);
            return new SuccessResponse<>(
            		AuthorizationMessages.REGISTERED.getMessage(), 
            		user
            );
        }, getTransformer());
	}
	
	/**
	 * Method for login route
	 */
	protected void loginRoute() {
        post("/login", (request, response) -> {
            User user = getService().loginUser(
            		request, 
            		getRight(), 
            		getReadActionName()
            );
            String token = user.getFirstToken();
            setAuthHeaders(response, token);
            return new SuccessResponse<>(
            		AuthorizationMessages.LOGIN.getMessage(), 
            		user
            );
        }, getTransformer());
	}
	
	/**
	 * Method with route for change password
	 */
	protected void changePasswordRoute() {
		post("/change-password/:user_id", (request, response) -> {
            User user = getService().changePassword(
            		request, 
            		getRight(), 
            		getReadActionName()
            );
            return new SuccessResponse<>(
            		AuthorizationMessages.PASSWORD_CHANGED.getMessage(), 
            		user
            );
        }, getTransformer());

	}
	
	/**
	 * Method with route for autologin
	 */
	protected void autoLoginRoute() {
        get("/autologin", (request, response) -> {
            User user = getService().autoLoginUser(
            		request, 
            		getRight(),
            		getReadActionName()
            );
            return new SuccessResponse<>(
            		AuthorizationMessages.AUTOLOGIN.getMessage(), 
            		user
            );
        }, getTransformer());
	}
	
	/**
	 * Method with route for user login
	 */
	protected void logoutRoute() {
        get("/logout", (request, response) -> {
            User user = getService().logoutUser(
            		request, 
            		getRight(), 
            		getReadActionName()
            );
            return new SuccessResponse<>(
            		AuthorizationMessages.LOGOUT.getMessage(), 
            		user
            );
        }, getTransformer());
	}
	
    /**
     * Method with define authorization routes
     */
	@Override
    public void register() {
		registerRoute();
		loginRoute();
		changePasswordRoute();
		autoLoginRoute();
		logoutRoute();
    }
}
