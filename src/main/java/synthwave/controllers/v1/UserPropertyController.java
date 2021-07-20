package synthwave.controllers.v1;

import platform.constants.DefaultRights;
import platform.controllers.BaseController;
import platform.exceptions.DataException;
import synthwave.controllers.messages.UserPropertiesMessages;
import synthwave.models.mongodb.embeddeds.EmbeddedProperty;
import synthwave.services.v1.users.UserPropertyService;
import platform.utils.responses.SuccessResponse;
import platform.utils.transformers.JsonTransformer;
import dev.morphia.Datastore;
import java.util.List;
import static spark.Spark.*;

/**
 * Static class with routes for work with user property documents
 */
public class UserPropertyController 
	extends BaseController<UserPropertyService, JsonTransformer> {
	
	public UserPropertyController(
			Datastore datastore,
			JsonTransformer transformer
	) {
		super(
				new UserPropertyService(datastore),
				transformer,
				DefaultRights.USERS.getName()
		);
	}
	
	/**
	 * Get user properties list by user id
	 */
	protected void getPropertiesListByUserIdRoute() {
        get("/:user_id/properties", (request, response) -> {
            List<EmbeddedProperty> properties = getService().getUserProperties(
                    request,
                    getRight(), 
                    getReadActionName()
            );
            return new SuccessResponse<>(
            		UserPropertiesMessages.LIST.getMessage(), 
            		properties
            );
        }, getTransformer());
	}
	
	/**
	 * Method for create user property
	 */
	protected void createUserPropoertyRoute() {
		post("/:user_id/properties", (request, response) -> {
            EmbeddedProperty userProperty = getService().createUserProperty(
                    request, 
                    getRight(), 
                    getCreateActionName()
            );
            return new SuccessResponse<>(
            		UserPropertiesMessages.CREATED.getMessage(), 
            		userProperty
            );
        }, getTransformer());
	}
	
	/**
	 * Method for get user property by user id & property id
	 */
	protected void getUserPropertyByUserIdAndId() {
		get("/:user_id/properties/:property_id", (request, ressponse) -> {
            EmbeddedProperty property = getService().getUserPropertyById(
                    request, 
                    getRight(), 
                    getReadActionName()
            );
            if (property != null) {
                return new SuccessResponse<>(
                		UserPropertiesMessages.ENTITY.getMessage(), 
                		property
                );
            } else {
                Error error = new Error("Can not find user property");
                throw new DataException("NotFound", error);
            }
        }, getTransformer());
	}
	
	/**
	 * Method for update user property
	 */
	protected void updatePropertyRoute() {
		put("/:user_id/properties/:property_id", (request, response) -> {
            // Get user rule for update users documents
            EmbeddedProperty property = getService().updateProperty(
                            request, 
                            getRight(),
                            getUpdateActionName()
                    );
            return new SuccessResponse<>(
            		UserPropertiesMessages.UPDATED.getMessage(), 
            		property
            );
        }, getTransformer());
	}
	
	/**
	 * Method for remove user property
	 */
	protected void deleteUserPropertyRoute() {
		delete("/:user_id/properties/:property_id", (request, response) -> {
            List<EmbeddedProperty> properties = getService().deleteUserProperty(
                    request, 
                    getRight(),
                    getDeleteActionName()
            );
            return new SuccessResponse<>(
            		UserPropertiesMessages.DELETED.getMessage(), 
            		properties
            );
        }, getTransformer());
	}
    
    /**
     * Method register routes for work with user properties
     */
	@Override
    public void register() {
		createUserPropoertyRoute();
		getPropertiesListByUserIdRoute();
		getUserPropertyByUserIdAndId();
		updatePropertyRoute();
		deleteUserPropertyRoute();
    }
}
