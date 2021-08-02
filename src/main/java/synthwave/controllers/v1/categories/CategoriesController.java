package synthwave.controllers.v1.categories;

import synthwave.controllers.messages.CategoriesMessages;
import synthwave.models.mongodb.standalones.Category;
import synthwave.services.v1.categories.CategoryService;
import platform.constants.DefaultRights;
import platform.controllers.BaseController;
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
	extends BaseController<CategoryService, JsonTransformer> {
    
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
			DefaultRights.CATEGORIES.getName()
		);
	}
	
	/**
	 * Method for get categories list
	 */
	protected void getCategoriesListRoute() {
		get("", (request, response) -> {
            List<Category> categories = getService().getCategoriesList(
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
	
	/**
	 * Method for get categories list by owner
	 */
	protected void getCategoriesListByOwnerRoute() {
		get("/owner/:user_id", (request, response) -> {
            List<Category> categories = getService().getCategoriesByUser(
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
	
	/**
	 * Method for get categories list by catalog
	 */
	protected void getCategoriesListByCatalogRoute() {
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
	
	/**
	 * Method for create category entity
	 */
	protected void createCategoryRoute() {
		post("/catalog/:catalog_id/owner/:user_id", (request, response) -> {
            Category category = getService().createCategory(
                    request, 
                    getRight(),
                    getCreateActionName()
            );
            return new SuccessResponse<>(
            		CategoriesMessages.CREATED.getMessage(), 
            		category
            );
        }, getTransformer());
	}
	
	/**
	 * Method for get category entity by owner id and entity id
	 */
	protected void getCategoryByOwnerAndIdRoute() {
		get("/owner/:user_id/category/:category_id", (request, response) -> {
            Category category = getService().getCategoryById(
                    request, 
                    getRight(),
                    getReadActionName()
            );
            return new SuccessResponse<>(
            		CategoriesMessages.ENTITY.getMessage(), 
            		category
            );
        }, getTransformer());
	}
	
	/**
	 * Method for update category entity
	 */
	protected void updateCategoryRoute() {
		put("/owner/:user_id/category/:category_id", (request, response) -> {
            Category category = getService().updateCategory(
                    request,
                    getRight(),
                    getUpdateActionName()
            );
            return new SuccessResponse<>(
            		CategoriesMessages.UPDATED.getMessage(), 
            		category
            );
        }, getTransformer());
	}
	
	/**
	 * Method for deactivate category entity
	 */
	protected void deleteCategoryRoute() {
		 delete("/owner/:user_id/category/:category_id", (request, ressponse) -> {
	            Category category = getService().deleteCategory(
	                    request, 
	                    getRight(),
	                    getDeleteActionName()
	            );
	            return new SuccessResponse<>(
	            		CategoriesMessages.DELETED.getMessage(), 
	            		category
	            );
	        }, getTransformer());
	}
	
	/**
	 * Method for get/register all routes
	 */
	@Override
    public void register() {
    	createCategoryRoute();
    	getCategoriesListRoute();
    	getCategoriesListByOwnerRoute();
    	getCategoriesListByCatalogRoute();
    	getCategoryByOwnerAndIdRoute();
    	updateCategoryRoute();
    	deleteCategoryRoute();
    }
}
