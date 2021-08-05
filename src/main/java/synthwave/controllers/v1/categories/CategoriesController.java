package synthwave.controllers.v1.categories;

import synthwave.controllers.base.RESTController;
import synthwave.controllers.messages.CategoriesMessages;
import synthwave.models.mongodb.standalones.Category;
import synthwave.repositories.mongodb.v1.CategoriesRepository;
import synthwave.services.v1.categories.CategoryService;
import platform.constants.DefaultRights;
import platform.utils.responses.SuccessResponse;
import platform.utils.transformers.JsonTransformer;
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
            return new SuccessResponse<>(
            		CategoriesMessages.LIST.getMessage(), 
            		categories
           );
        }, getTransformer());
	}
}
