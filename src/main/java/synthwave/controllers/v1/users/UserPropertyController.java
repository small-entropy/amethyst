package synthwave.controllers.v1.users;

import platform.constants.DefaultRights;
import synthwave.controllers.base.EmbeddedPropertiesController;
import synthwave.controllers.messages.UserPropertiesMessages;
import synthwave.filters.UsersFilter;
import synthwave.models.mongodb.standalones.User;
import synthwave.repositories.mongodb.v1.UserPropertiesRepository;
import synthwave.repositories.mongodb.v1.UsersRepository;
import synthwave.services.v1.users.UserPropertyService;
import platform.utils.transformers.JsonTransformer;
import dev.morphia.Datastore;

/**
 * Static class with routes for work with user property documents
 */
public class UserPropertyController 
	extends EmbeddedPropertiesController<
		User,
		UsersFilter,
		UsersRepository,
		UserPropertiesRepository,
		UserPropertyService> {
	
	public UserPropertyController(
			Datastore datastore,
			JsonTransformer transformer
	) {
		super(
				new UserPropertyService(datastore),
				transformer,
				DefaultRights.USERS.getName(),
				"/:user_id/properties",
				"/:user_id/properties/:property_id"
		);
	}

	@Override
	protected String getSuccessMessage(String message) {
        return switch (message) {
            case "created" -> UserPropertiesMessages.CREATED.getMessage();
            case "list" -> UserPropertiesMessages.LIST.getMessage();
            case "entity" -> UserPropertiesMessages.ENTITY.getMessage();
            case "update" -> UserPropertiesMessages.UPDATED.getMessage();
            case "delete" -> UserPropertiesMessages.DELETED.getMessage();
            default -> "Successfully";
        };
    }
}
