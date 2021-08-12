package synthwave.controllers.v1.users;

import synthwave.controllers.messages.UserRightsMessages;
import synthwave.models.mongodb.embeddeds.EmbeddedRight;
import synthwave.services.v1.users.UserRightService;
import platform.constants.DefaultRights;
import platform.controllers.BaseController;
import platform.exceptions.AccessException;
import platform.utils.responses.SuccessResponse;
import platform.utils.transformers.JsonTransformer;
import spark.Request;
import spark.Response;
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

	protected void beforeGetEntityByIdRoute(Request request, Response response) 
		throws AccessException {
		boolean hasAccess = getService().checkHasAccess(
			request, 
			getRight(), 
			getReadActionName()
		);
		nextIfHasAccess(
			hasAccess, 
			"CanNotRead", 
			"Has not rights for read private fields"
		);
	}

	protected void beforeCreateEntityRoute(Request request, Response response)
		throws AccessException {
		boolean hasAccess = getService().checkHasAccess(
			request, 
			getRight(), 
			getCreateActionName()
		);
		nextIfHasAccess(
			hasAccess, 
			"CanNotCreate", 
			"Has not rights to create right document for user document"
		);
	}

	protected void beforeGetListRoute(Request request, Response response)
		throws AccessException {
		boolean hasAccess = getService().checkHasAccess(
			request, 
			getRight(), 
			getReadActionName()
		);
		nextIfHasAccess(
			hasAccess, 
			"CanNotRead", 
			"Has not rights for read private fields"
		);
	}

	protected void beforeDeleteRoute(Request request, Response response)
		throws AccessException {
			boolean hasAccess = getService().checkHasAccess(
					request, 
					getRight(), 
					getDeleteActionName()
			);
			nextIfHasAccess(
				hasAccess, 
				"CanNotDelete",
				"Can't delete user right"
			);
	}

	protected void beforeUpdateRoute(Request request, Response response)
		throws AccessException {
		boolean hasAccess = getService().checkHasAccess(
			request, 
			getRight(), 
			getUpdateActionName()
		);
		nextIfHasAccess(
			hasAccess,
			"CanNotUpdate", 
			"Can't update user right"	
		);
	}

	protected void registerBeforeList() {
		before("/:user_id/rights", (request, response) -> {
			switch(request.requestMethod()) {
				case "GET":
					beforeGetListRoute(request, response);
					break;
				case "POST":
					beforeCreateEntityRoute(request, response);
					break;
				default:
					break;
			}
		});
	}

	protected void registerBeforeEntity() {
		before("/:user_id/rigthts/:right_id", (request, response) -> {
			switch(request.requestMethod()) {
				case "GET":
					beforeGetEntityByIdRoute(request, response);
					break;
				case "PUT":
					beforeUpdateRoute(request, response);
					break;
				case "DELETE":
					beforeDeleteRoute(request, response);
					break;
				default:
					break;
			}
		});
	}

	@Override
	protected void registerBefore() {
		registerBeforeEntity();
		registerBeforeList();
	}

	
	/**
	 * Method for get user rights list by user id
	 */
	protected void getListRoute() {
		get("/:user_id/rights", (request, response) -> {
            List<EmbeddedRight> rights = getService().getUserRights(request);
            return new SuccessResponse<>(
            		UserRightsMessages.LIST.getMessage(), 
            		rights
            );
        }, getTransformer());
	}
	
	/**
	 * Method for create user right entity
	 */
	protected void createEntity() {
		post("/:user_id/rights", (request, response) -> {
            EmbeddedRight right = getService().createUserRight(request);
            return new SuccessResponse<>(
            		UserRightsMessages.CREATED.getMessage(), 
            		right
            );
        }, getTransformer());
	}
	
	/**
	 * Method for get user rights entity by user id & right id
	 */
	protected void getEntityByIdRoute() {
		get("/:user_id/rights/:right_id", (request, ressponse) -> {
            EmbeddedRight right = getService().getUserRightById(request);
            return new SuccessResponse<>(
            		UserRightsMessages.ENTITY.getMessage(),
            		right
            );
        }, getTransformer());
	}
	
	/**
	 * Method for update user right entity
	 */
	protected void updateRoute() {
		 put("/:user_id/rights/:right_id", (request, response) -> {
			EmbeddedRight right = getService().updateRight(request);
			return new SuccessResponse<>(
					UserRightsMessages.UPDATED.getMessage(), 
					right
			);
	    }, getTransformer());
	}
	
	/**
	 * Method for remove user rights entity
	 */
	@Override
	protected void deleteRoute() {
        delete("/:user_id/rights/:right_id", (request, response) -> {
            List<EmbeddedRight> rights = getService().deleteRight(request);
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
		registerBefore();
		createEntity();
		getListRoute();
		getEntityByIdRoute();
		updateRoute();
		deleteRoute();
    }
}
