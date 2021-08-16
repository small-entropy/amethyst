package synthwave.controllers.v1.users;

import core.constants.DefaultRights;
import synthwave.controllers.abstracts.EmbeddedPropertiesController;
import synthwave.constants.UserPropertiesMessages;
import synthwave.filters.UsersFilter;
import synthwave.models.morphia.extend.User;
import synthwave.repositories.morphia.UserPropertiesRepository;
import synthwave.repositories.morphia.UsersRepository;
import synthwave.services.v1.users.UserPropertyService;
import core.response.transformers.JsonTransformer;
import dev.morphia.Datastore;

/**
 * Static class with routes for work with user property documents
 */
public class UserPropertiesController 
	extends EmbeddedPropertiesController<
		User,
		UsersFilter,
		UsersRepository,
		UserPropertiesRepository,
		UserPropertyService> {
	
	public UserPropertiesController(
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
