package synthwave.controllers.v1.users;

import dev.morphia.Datastore;
import spark.Request;
import spark.Response;

import core.constants.DefaultRights;
import core.exceptions.TokenException;
import core.utils.RequestManager;
import core.response.transformers.JsonTransformer;

import synthwave.controllers.abstracts.RESTController;
import synthwave.constants.UsersMessages;
import synthwave.models.morphia.extend.User;
import synthwave.repositories.morphia.UsersRepository;
import synthwave.services.v1.users.UserService;
import core.utils.Comparator;

/**
 * Class controller of users API
 * @author small-entropy
 * @version 1
 */
public class UserController 
	extends RESTController<User, UsersRepository, UserService> {

	@Override
	protected void beforeDeleteRoute(Request request, Response response) 
		throws TokenException {
		String token = RequestManager.getTokenByRequest(request);
		if (token == null) {
			Error error = new Error("Token not send");
            throw new TokenException("NotSend", error);
		} else {
			boolean isEqualsIds = Comparator.id_fromParam_fromToken(request);
			if (!isEqualsIds) {
				Error error = new Error("Incorrect token");
                throw new TokenException("NotEquals", error);
			}
		}
	}
	
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
