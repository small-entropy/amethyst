package synthwave.controllers.v1.tags;

import synthwave.controllers.base.RESTController;
import synthwave.controllers.messages.TagsMessages;
import synthwave.models.mongodb.standalones.Tag;
import synthwave.repositories.mongodb.v1.TagsRepository;
import synthwave.services.v1.tags.TagService;
import platform.constants.DefaultActions;
import platform.constants.DefaultRights;
import platform.dto.RuleDTO;
import platform.exceptions.AccessException;
import platform.utils.transformers.JsonTransformer;
import spark.Request;
import spark.Response;
import dev.morphia.Datastore;

/**
 * Class for work with tags collection
 * @author small-entropy
 */
public class TagsController
	extends RESTController<Tag, TagsRepository, TagService> {

	@Override
	protected void beforeUpdateRoute(Request request, Response response) 
		throws AccessException {
		RuleDTO rule = getService().getRule(
			request, 
			getRight(),
			DefaultActions.UPDATE.getName()
		);
		boolean hasAccess = getService().checkHasAccess(request, rule);
		nextIfHasAccess(
			hasAccess,
			"CanNotUpdate",
			"Has no access to update tag document"
		);
	}

	@Override
	protected void beforeCreateEntity(Request request, Response response) 
		throws AccessException {
		RuleDTO rule = getService().getRule(
			request, 
			getRight(), 
			DefaultActions.CREATE.getName()
		);
		boolean hasAccess = getService().checkHasGlobalAccess(request, rule);
		nextIfHasAccess(hasAccess, "CanNotCreate", "Has no access to create tag");
	}

	@Override
	protected void beforeDeleteRoute(Request request, Response response)
		throws AccessException {
		boolean hasAccess = getService().checkHasGlobalAccess(
			request, 
			getRight(), 
			DefaultActions.DELETE.getName()
		);
		nextIfHasAccess(
			hasAccess, 
			"CanNotDelete", 
			"Has no access to delete tag document"
		);
	}
	
	/**
	 * Default tag controller constructor. Create instance
	 * by datastore & transformer
	 * @param datastore Morphia datastore object
	 * @param transformer response transformer
	 */
	public TagsController(
			Datastore datastore,
			JsonTransformer transformer
	) {
		super(
				new TagService(datastore),
				transformer,
				DefaultRights.TAGS.getName(),
				null,
				"",
				"/owner/:user_id/tag/:tag_id",
				"/owner/:user_id",
				true,
				true,
				true,
				true,
				true,
				TagsMessages.CREATED.getMessage(),
				TagsMessages.LIST.getMessage(),
				TagsMessages.ENTITY.getMessage(), 
				TagsMessages.UPDATED.getMessage(), 
				TagsMessages.DELETED.getMessage()
		);
	}
}
