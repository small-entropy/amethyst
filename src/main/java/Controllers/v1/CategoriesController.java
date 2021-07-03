package Controllers.v1;

import Controllers.base.BaseCategoriesController;
import Models.Standalones.Category;
import Utils.responses.SuccessResponse;
import Services.v1.CategoryService;
import Utils.transformers.JsonTransformer;
import dev.morphia.Datastore;
import java.util.List;
import static spark.Spark.*;

/**
 * Class-controller for categories
 * @author small-entropy
 */
public class CategoriesController extends BaseCategoriesController {
    
    public static void routes(Datastore datastoer, JsonTransformer transformer) {
        CategoryService service = new CategoryService(datastoer);
        
        // Route for get categories list
        get("", (req, res) -> {
            List<Category> categories = service.getCategoriesList(
                    req, 
                    RIGHT, 
                    READ
            );
            return new SuccessResponse<>(MSG_LIST, categories);
        }, transformer);
        
        // Route for get all categories by owner id
        get("/owner/:user_id", (req, res) -> {
            List<Category> categories = service.getCategoriesByUser(
                    req, 
                    RIGHT, 
                    READ
            );
            return new SuccessResponse<>(MSG_LIST, categories);
        }, transformer);
        
        get("/catalog/:catalog_id", (req, res) -> {
            List<Category> categories = service.getCatalogCategories(
                    req,
                    RIGHT,
                    READ
            );
            return new SuccessResponse<>(MSG_LIST, categories);
        }, transformer);
        
        // Route for create category for catelog
        post("/catalog/:catalog_id/owner/:user_id", (req, res) -> {
            Category category = service.createCategory(
                    req, 
                    RIGHT,
                    CREATE
            );
            return new SuccessResponse<>(MSG_CREATED, category);
        }, transformer);
        
        // Route for get category
        get("/owner/:user_id/category/:category_id", (req, res) -> {
            Category category = service.getCategoryById(
                    req, 
                    RIGHT,
                    READ
            );
            return new SuccessResponse<>(MSG_ENTITY, category);
        }, transformer);
        
        // Route for update category
        put("/owner/:user_id/category/:category_id", (req, res) -> {
            Category category = service.updateCategory(
                    req,
                    RIGHT,
                    UPDATE
            );
            return new SuccessResponse<>(MSG_UPDATED, category);
        }, transformer);
        
        // Route for mark to delete category
        delete("/owner/:user_id/category/:category_id", (req, res) -> {
            Category category = service.deleteCategory(
                    req, 
                    RIGHT,
                    DELETE
            );
            return new SuccessResponse<>(MSG_DELETED, category);
        }, transformer);
    }
}
