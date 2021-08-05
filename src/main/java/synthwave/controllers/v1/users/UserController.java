package synthwave.controllers.v1.users;

import platform.constants.DefaultRights;
import platform.utils.transformers.JsonTransformer;

import synthwave.controllers.base.RESTController;
import synthwave.controllers.messages.UsersMessages;
import synthwave.models.mongodb.standalones.User;
import synthwave.repositories.mongodb.v1.UsersRepository;
import synthwave.services.v1.users.UserService;

import dev.morphia.Datastore;

/**
 * Class controller of users API
 * @author small-entropy
 * @version 1
 */
public class UserController 
	extends RESTController<User, UsersRepository, UserService> {
	
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
				DefaultRights.USERS.getName(),
				"/:user_id",
				"",
				null,
				null,
				true,
				true,
				false,
				true,
				true,
				null,
				UsersMessages.LIST.getMessage(), 
				UsersMessages.ENTITY.getMessage(), 
				null,
				UsersMessages.DELETED.getMessage()
		);
	}
}
