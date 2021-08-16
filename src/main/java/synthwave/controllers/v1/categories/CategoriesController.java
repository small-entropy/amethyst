package synthwave.controllers.v1.categories;

import synthwave.controllers.abstracts.RESTController;
import synthwave.constants.CategoriesMessages;
import synthwave.models.morphia.extend.Category;
import synthwave.repositories.morphia.CategoriesRepository;
import synthwave.services.v1.categories.CategoryService;
import core.utils.Comparator;
import core.constants.DefaultRights;
import engine.dto.RuleDTO;
import core.exceptions.AccessException;
import engine.response.answer.Success;
import core.response.transformers.JsonTransformer;
import spark.Request;
import spark.Response;
import dev.morphia.Datastore;
import java.util.List;
import static spark.Spark.*;

/**
 * Class-controller for categories
 * @author small-entropy
 * @version 1
 */
public class CategoriesController 
	extends RESTController<Category, CategoriesRepository, CategoryService> {

	@Override
	protected void beforeCreateEntityRoute(Request request, Response response)
		throws AccessException {
		RuleDTO rule = getService().getRule(
			request, 
			getRight(), 
			getCreateActionName()
		);
		boolean isTrusted = Comparator.id_fromParam_fromToken(request);
		boolean hasAccess;
		if (rule == null) {
			hasAccess = false;
		} else {
			hasAccess = (isTrusted) ? rule.isMyGlobal() : rule.isOtherGlobal();
		}
		nextIfHasAccess(hasAccess, "CanNotCreate", "Has no access to create catalog");
	}

	@Override
	protected void beforeUpdateRoute(Request request, Response response) 
		throws AccessException {
		RuleDTO rule = getService().getRule(
			request, 
			getRight(), 
			getUpdateActionName()
		);
		boolean isTrusted = Comparator.id_fromParam_fromToken(request);
		boolean hasAccess = getService().checkHasAccess(rule, isTrusted);
		nextIfHasAccess(
			hasAccess, 
			"CanNotUpdate", 
			"Has no access to update cateogry document"
		);
	}

	@Override
	protected void beforeDeleteRoute(Request request, Response response) 
		throws AccessException {
		boolean hasAccess = getService().checkHasGlobalAccess(
			request, 
			getRight(), 
			getDeleteActionName()
		);
		nextIfHasAccess(hasAccess, "CanNotDelete", "Has no access to delete category");
	}
    
	/**
	 * Default categories controller constructor.
	 * @param datastore Morphia datastore object
	 * @param transformer response transformer
	 */
	public CategoriesController(
			Datastore datastore,
			JsonTransformer transformer
	) {
		super(
			new CategoryService(datastore),
			transformer,
			DefaultRights.CATEGORIES.getName(),
			null,
			"",
			"/owner/:user_id/category/:category_id",
			"/owner/:user_id",
			true,
			true,
			true,
			true,
			true,
			CategoriesMessages.CREATED.getMessage(),
			CategoriesMessages.LIST.getMessage(),
			CategoriesMessages.ENTITY.getMessage(),
			CategoriesMessages.UPDATED.getMessage(),
			CategoriesMessages.DELETED.getMessage()
		);
	}
	
	@Override
	protected void customRoutes() {
		// Method for get categories list by catalog
		get("/catalog/:catalog_id", (request, response) -> {
            List<Category> categories = getService().getCatalogCategories(
                    request,
                    getRight(),
                    getReadActionName()
            );
            return new Success<>(
            		CategoriesMessages.LIST.getMessage(), 
            		categories
           );
        }, getTransformer());
	}
}
