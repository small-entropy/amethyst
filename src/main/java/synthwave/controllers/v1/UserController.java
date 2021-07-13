package synthwave.controllers.v1;

import synthwave.controllers.messages.TagsMessages;
import synthwave.controllers.messages.UsersMessages;
import platform.constants.DefaultRights;
import platform.controllers.BaseController;
import platform.exceptions.DataException;
import synthwave.models.mongodb.standalones.User;
import platform.utils.responses.SuccessResponse;
import synthwave.services.v1.UserService;
import platform.utils.transformers.JsonTransformer;
import dev.morphia.Datastore;
import java.util.List;
import static spark.Spark.*;

/**
 * Class controller of users API
 * @author small-entropy
 * @version 1
 */
public class UserController 
	extends BaseController<UserService, JsonTransformer> {
	
	/**
	 * Default constructor for users controller. Create instance
	 * by datastore & response transformer
	 * @param datastore Morphia datastore object
	 * @param transformer response transformer
	 */
	public UserController(
			Datastore datastore,
			JsonTransformer transformer
	) {
		super(
				new UserService(datastore),
				transformer,
				DefaultRights.USERS.getName()
		);
	}
	
	/**
	 * Method for get users list
	 */
	protected void getUsersListRoute() {
        get("", (request, response) -> {
            List<User> users = getService().getList(
            		request, 
            		getRight(),
            		getReadActionName()
            );
            // Check users list size.
            // If size equal - fail method status & message
            // If size not equal - success method status & message
            if (!users.isEmpty()) {
                return new SuccessResponse<>(
                		UsersMessages.LIST.getMessage(), 
                		users
                );
            } else {
                Error error = new Error("Users list is empty");
                throw new DataException("NotFound", error);
            }
        }, getTransformer());
	}
	
	/**
	 * Method for get user entity
	 */
	protected void getUserByIdRoute() {
		get("/:user_id", (request, response) -> {
            User user = getService().getUserById(request);
            if (user != null) {
                return new SuccessResponse<>(
                		TagsMessages.ENTITY.getMessage(), 
                		user
                );
            } else {
                Error error = new Error("Can not find user by id");
                throw  new DataException("NotFound", error);
            }
        }, getTransformer());
	}
	
	/**
	 * Method for deactivate user
	 */
	protected void deleteUserRoute() {
		delete("/:user_id", (request, response) -> {
            User user = getService().markToRemove(request);
            return new SuccessResponse<>(
            		UsersMessages.DELETED.getMessage(), 
            		user
            );
        }, getTransformer());
	}
    
    /**
     * Method register routes for work with users data
     */
	@Override
    public void register() {
		getUsersListRoute();
		getUserByIdRoute();
		deleteUserRoute();
    }
}
