package synthwave.controllers.v1.users;

import core.constants.DefaultRights;
import synthwave.controllers.abstracts.EmbeddedPropertiesController;
import synthwave.constants.UserProfileMessages;
import synthwave.filters.UsersFilter;
import synthwave.models.morphia.extend.User;
import synthwave.repositories.morphia.UserProfileRepository;
import synthwave.repositories.morphia.UsersRepository;
import synthwave.services.v1.users.UserProfileService;
import core.response.transformers.JsonTransformer;
import dev.morphia.Datastore;

/**
 * Class with routes for work with profile documents
 * @author small-entropy
 */
public class UserProfileController 
	extends EmbeddedPropertiesController<
		User,
		UsersFilter,
		UsersRepository, 
		UserProfileRepository,
		UserProfileService> {
	
	/**
	 * Default user profile controller. Create instance by
	 * datastore & response transformer
	 * @param datastore Morphia datastore object
	 * @param transformer response transformer 
	 */
	public UserProfileController(
			Datastore datastore,
			JsonTransformer transformer
	) {
		super(
				new UserProfileService(datastore),
				transformer,
				DefaultRights.USERS.getName(),
				"/:user_id/profile",
				"/:user_id/profile/:property_id",
				false,
				false
		);
	}

	@Override
	protected String getSuccessMessage(String message) {
        return switch (message) {
            case "created" -> UserProfileMessages.CREATED.getMessage();
            case "list" -> UserProfileMessages.LIST.getMessage();
            case "entity" -> UserProfileMessages.ENTITY.getMessage();
            case "update" -> UserProfileMessages.UPDATED.getMessage();
            case "delete" -> UserProfileMessages.DELETED.getMessage();
            default -> "Unknown success action";
        };
    }
}
