package synthwave.controllers.v1;

import platform.constants.DefaultRights;
import platform.controllers.BaseController;
import platform.exceptions.DataException;
import synthwave.controllers.messages.UserProfileMessages;
import synthwave.models.mongodb.embeddeds.EmbeddedProperty;
import synthwave.services.v1.users.UserProfileService;
import platform.utils.responses.SuccessResponse;
import platform.utils.transformers.JsonTransformer;
import dev.morphia.Datastore;
import java.util.List;
import static spark.Spark.*;

/**
 * Class with routes for work with profile documents
 */
public class UserProfileController 
	extends BaseController<UserProfileService, JsonTransformer> {
	
	/**
	 * Default user profile controller. Create instance by
	 * datastore & response transformer
	 * @param datastore Morphia datastore object
	 * @param transformer responser transformer 
	 */
	public UserProfileController(
			Datastore datastore,
			JsonTransformer transformer
	) {
		super(
				new UserProfileService(datastore),
				transformer,
				DefaultRights.USERS.getName()
		);
	}
	
	/**
	 * Method for get user profile by user id
	 */
	protected void getUserProfileByUserIdRoute() {
        get("/:user_id/profile", (request, response)-> {
            List<EmbeddedProperty> profile = getService().getEmbeddedProperties(
            		request
            );
            return new SuccessResponse<>(
            		UserProfileMessages.LIST.getMessage(), 
            		profile
            );
        }, getTransformer());
	}
    
	/**
	 * Message for crate profile property
	 */
	protected void createProfilePropertyRoute() {
		post("/:user_id/profile", (request, response) -> {
            EmbeddedProperty property = getService().createUserProperty(
            		request
            );
            return new SuccessResponse<>(
            		UserProfileMessages.CREATED.getMessage(), 
            		property
            );
        }, getTransformer());
	}

	/**
	 * Method for get profile property by user id & property id
	 */
	protected void getProfilPropertyByUserIdAndIdRoute() {
		get("/:user_id/profile/:property_id", (request, response) -> {
            EmbeddedProperty property = getService().getEmbeddedProperty(
                    request
            );
            if (property != null) {
                return new SuccessResponse<>(
                		UserProfileMessages.ENTITY.getMessage(), 
                		property
                );
            } else {
                Error error = new Error("Can found property with this id");
                throw new DataException("NotFound", error);
            }
        }, getTransformer());
	}
	
	/**
	 * Method for update profile property entity
	 */
	protected void updateProfilePropertyRoute() {
		put("/:user_id/profile/:property_id", (request, response) -> {
            EmbeddedProperty property = getService().updateUserProperty(
                    request, 
                    getRight(), 
                    getUpdateActionName()
            );
            return new SuccessResponse<>(
            		UserProfileMessages.UPDATED.getMessage(), 
            		property
            );
        }, getTransformer());
	}
	
	/**
	 * Method for delete profile property
	 */
	protected void deleteProfilePropertyRoute() {
		delete("/:user_id/profile/:property_id", (request, response) -> {
            List<EmbeddedProperty> profile = getService().deleteUserProperty(
                    request, 
                    getRight(), 
                    getDeleteActionName()
            );
            return new SuccessResponse<>(
            		UserProfileMessages.DELETED.getMessage(), 
            		profile
            );
        }, getTransformer());
	}
	
    /**
     * Method register routes for work with user profile data
     */
	@Override
    public void register() {
		createProfilePropertyRoute();
		getUserProfileByUserIdRoute();
		getProfilPropertyByUserIdAndIdRoute();
		updateProfilePropertyRoute();
		deleteProfilePropertyRoute();
    }
}
