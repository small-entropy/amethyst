package synthwave.controllers.v1.users;

import synthwave.controllers.messages.UserRightsMessages;
import synthwave.models.mongodb.embeddeds.EmbeddedRight;
import synthwave.services.v1.users.UserRightService;
import platform.constants.DefaultRights;
import platform.controllers.BaseController;
import platform.utils.responses.SuccessResponse;
import platform.utils.transformers.JsonTransformer;
import dev.morphia.Datastore;
import java.util.List;
import static spark.Spark.*;

/**
 * Static class for work with user right document
 * @author small-entropy
 * @version 1
 */
public class UserRightsController 
	extends BaseController<UserRightService, JsonTransformer> {
	
	public UserRightsController(
			Datastore datastore, 
			JsonTransformer transformer
	) {
		super(
				new UserRightService(datastore),
				transformer,
				DefaultRights.USERS.getName()
		);
	}
	
	/**
	 * Method for get user rights list by user id
	 */
	protected void getUserRightsListByUserIdRoute() {
		get("/:user_id/rights", (request, response) -> {
            List<EmbeddedRight> rights = getService().getUserRights(
            		request, 
            		getRight(), 
            		getReadActionName()
            );
            return new SuccessResponse<>(
            		UserRightsMessages.LIST.getMessage(), 
            		rights
            );
        }, getTransformer());
	}
	
	/**
	 * Method for create user right entity
	 */
	protected void createUserRightRoute() {
		post("/:user_id/rights", (request, response) -> {
            EmbeddedRight right = getService().createUserRight(
            		request, 
            		getRight(), 
            		getCreateActionName()
            );
            return new SuccessResponse<>(
            		UserRightsMessages.CREATED.getMessage(), 
            		right
            );
        }, getTransformer());
	}
	
	/**
	 * Method for get user rights entity by user id & right id
	 */
	protected void getUserRightByUserIdAndIdRoute() {
		get("/:user_id/rights/:right_id", (request, ressponse) -> {
            EmbeddedRight right = getService().getUserRightById(
            		request, 
            		getRight(), 
            		getReadActionName()
            );
            return new SuccessResponse<>(
            		UserRightsMessages.ENTITY.getMessage(),
            		right
            );
        }, getTransformer());
	}
	
	/**
	 * Method for update user right entity
	 */
	protected void updateUserRightRoute() {
		 put("/:user_id/rights/:right_id", (request, response) -> {
	            EmbeddedRight right = getService().updateRight(
	            		request,
	            		getRight(), 
	            		getUpdateActionName()
	            );
	            return new SuccessResponse<>(
	            		UserRightsMessages.UPDATED.getMessage(), 
	            		right
	            );
	        }, getTransformer());
	}
	
	/**
	 * Method for remove user rights entity
	 */
	protected void deleteUserRightRoute() {
        delete("/:user_id/rights/:right_id", (request, response) -> {
            List<EmbeddedRight> rights = getService().deleteRight(
            		request,
            		getRight(), 
            		getDeleteActionName()
            );
            return new SuccessResponse<>(
            		UserRightsMessages.DELETED.getMessage(), 
            		rights
            );
        }, getTransformer());
	}
    
	/**
	 * Method register routes for work with user rights
	 */
	@Override
    public void register() {
		createUserRightRoute();
		getUserRightsListByUserIdRoute();
		getUserRightByUserIdAndIdRoute();
		updateUserRightRoute();
		deleteUserRightRoute();
    }
}
