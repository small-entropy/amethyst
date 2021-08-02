package synthwave.controllers.v1.users;

import platform.constants.DefaultRights;
import synthwave.controllers.base.EmbeddedPropertiesController;
import synthwave.controllers.messages.UserProfileMessages;
import synthwave.filters.UsersFilter;
import synthwave.models.mongodb.standalones.User;
import synthwave.repositories.mongodb.v1.UserProfileRepository;
import synthwave.repositories.mongodb.v1.UsersRepository;
import synthwave.services.v1.users.UserProfileService;
import platform.utils.transformers.JsonTransformer;
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
