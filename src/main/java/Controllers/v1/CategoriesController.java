package Controllers.v1;

import Controllers.core.AbstractController;
import DataTransferObjects.RuleDTO;
import Models.Standalones.Category;
import Responses.SuccessResponse;
import Services.v1.CategoryService;
import Sources.CatalogsSource;
import Sources.CategoriesSource;
import Sources.UsersSource;
import Transformers.JsonTransformer;
import Utils.constants.DefaultRights;
import Utils.constants.ResponseMessages;
import dev.morphia.Datastore;
import java.util.List;
import static spark.Spark.*;

/**
 * Class-controller for categories
 * @author small-entropy
 */
public class CategoriesController extends AbstractController {
    
    /** Property with name of right */
    private static final String RIGHT = DefaultRights.CATEGORIES.getName();
    
    /** Property with message for success get categories */
    private static final String MSG_LIST = ResponseMessages.CATEGORIES.getMessage();
    /** Property with message for success create category document */
    private static final String MSG_CREATED = ResponseMessages.CATEGORY_CREATED.getMessage();
    /** Property with message for success get category document */
    private static final String MSG_ENTITY = ResponseMessages.CATEGORY.getMessage();
    /** Property with emssage for scucess update category document */
    private static final String MSG_UPDATED = ResponseMessages.CATEGORY_UPDATED.getMessage();
    private static final String MSG_DELETED = ResponseMessages.CATEGORY_DELETED.getMessage();
    
    public static void routes(Datastore store, JsonTransformer transformer) {
        // Create catalog datastore source
        CatalogsSource catalogsSource = new CatalogsSource(store);
        // Create user datastore source
        UsersSource userSource = new UsersSource(store);
        // Create categories datastore source
        CategoriesSource categoriesSource = new CategoriesSource(store);
        
        // Route for get categories list
        get("", (req, res) -> {
            RuleDTO rule = getRule(req, userSource, RIGHT, READ);
            List<Category> categories = CategoryService.getCategoriesList(
                    req,
                    categoriesSource,
                    rule
            );
            return new SuccessResponse<>(MSG_LIST, categories);
        }, transformer);
        
        // Route for get all categories by owner id
        get("/owner/:user_id", (req, res) -> {
            RuleDTO rule = getRule(req, userSource, RIGHT, READ);
            List<Category> categories = CategoryService.getCategoriesByUser(
                    req, 
                    categoriesSource, 
                    rule
            );
            return new SuccessResponse<>(MSG_LIST, categories);
        }, transformer);
        
        get("/catalog/:catalog_id", (req, res) -> {
            RuleDTO rule = getRule(req, userSource, RIGHT, READ);
            List<Category> categories = CategoryService.getCatalogCategories(
                    req,
                    categoriesSource,
                    rule
            );
            return new SuccessResponse<>(MSG_LIST, categories);
        }, transformer);
        
        // Route for create category for catelog
        post("/catalog/:catalog_id/owner/:user_id", (req, res) -> {
            RuleDTO rule = getRule(req, userSource, RIGHT, CREATE);
            Category category = CategoryService.createCategory(
                    req, 
                    categoriesSource, 
                    catalogsSource, 
                    userSource, 
                    rule
            );
            return new SuccessResponse<>(MSG_CREATED, category);
        }, transformer);
        
        // Route for get category
        get("/owner/:user_id/category/:category_id", (req, res) -> {
            RuleDTO rule = getRule(req, userSource, RIGHT, READ);
            Category category = CategoryService.getCategoryById(
                    req, 
                    categoriesSource,
                    rule
            );
            return new SuccessResponse<>(MSG_ENTITY, category);
        }, transformer);
        
        // Route for update category
        put("/owner/:user_id/category/:category_id", (req, res) -> {
            RuleDTO rule = getRule(req, userSource, RIGHT, UPDATE);
            Category category = CategoryService.updateCategory(
                    req,
                    categoriesSource,
                    rule
            );
            return new SuccessResponse<>(MSG_UPDATED, category);
        }, transformer);
        
        // Routt for mark to delete category
        delete("/owner/:user_id/category/:category_id", (req, res) -> {
            RuleDTO rule = getRule(req, userSource, RIGHT, DELETE);
            Category category = CategoryService.deleteCategory(
                    req, 
                    categoriesSource, 
                    rule
            );
            return new SuccessResponse<>(MSG_DELETED, category);
        }, transformer);
    }
}
