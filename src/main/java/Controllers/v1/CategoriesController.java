package Controllers.v1;

import Controllers.base.BaseCategoriesController;
import DataTransferObjects.v1.RuleDTO;
import Models.Standalones.Category;
import Utils.responses.SuccessResponse;
import Services.v1.CategoryService;
import Repositories.v1.CatalogsRepository;
import Repositories.v1.CategoriesRepository;
import Repositories.v1.UsersRepository;
import Utils.transformers.JsonTransformer;
import dev.morphia.Datastore;
import java.util.List;
import static spark.Spark.*;

/**
 * Class-controller for categories
 * @author small-entropy
 */
public class CategoriesController extends BaseCategoriesController {
    
    public static void routes(Datastore store, JsonTransformer transformer) {
        // Create catalog datastore source
        CatalogsRepository catalogsRepository = new CatalogsRepository(store);
        // Create user datastore source
        UsersRepository usersRepository = new UsersRepository(store);
        // Create categories datastore source
        CategoriesRepository categoriesRepository = new CategoriesRepository(store);
        
        // Route for get categories list
        get("", (req, res) -> {
            RuleDTO rule = getRule(req, usersRepository, RIGHT, READ);
            List<Category> categories = CategoryService.getCategoriesList(
                    req,
                    categoriesRepository,
                    rule
            );
            return new SuccessResponse<>(MSG_LIST, categories);
        }, transformer);
        
        // Route for get all categories by owner id
        get("/owner/:user_id", (req, res) -> {
            RuleDTO rule = getRule(req, usersRepository, RIGHT, READ);
            List<Category> categories = CategoryService.getCategoriesByUser(
                    req, 
                    categoriesRepository, 
                    rule
            );
            return new SuccessResponse<>(MSG_LIST, categories);
        }, transformer);
        
        get("/catalog/:catalog_id", (req, res) -> {
            RuleDTO rule = getRule(req, usersRepository, RIGHT, READ);
            List<Category> categories = CategoryService.getCatalogCategories(
                    req,
                    categoriesRepository,
                    rule
            );
            return new SuccessResponse<>(MSG_LIST, categories);
        }, transformer);
        
        // Route for create category for catelog
        post("/catalog/:catalog_id/owner/:user_id", (req, res) -> {
            RuleDTO rule = getRule(req, usersRepository, RIGHT, CREATE);
            Category category = CategoryService.createCategory(
                    req, 
                    categoriesRepository, 
                    catalogsRepository, 
                    usersRepository, 
                    rule
            );
            return new SuccessResponse<>(MSG_CREATED, category);
        }, transformer);
        
        // Route for get category
        get("/owner/:user_id/category/:category_id", (req, res) -> {
            RuleDTO rule = getRule(req, usersRepository, RIGHT, READ);
            Category category = CategoryService.getCategoryById(
                    req, 
                    categoriesRepository,
                    rule
            );
            return new SuccessResponse<>(MSG_ENTITY, category);
        }, transformer);
        
        // Route for update category
        put("/owner/:user_id/category/:category_id", (req, res) -> {
            RuleDTO rule = getRule(req, usersRepository, RIGHT, UPDATE);
            Category category = CategoryService.updateCategory(
                    req,
                    categoriesRepository,
                    rule
            );
            return new SuccessResponse<>(MSG_UPDATED, category);
        }, transformer);
        
        // Route for mark to delete category
        delete("/owner/:user_id/category/:category_id", (req, res) -> {
            RuleDTO rule = getRule(req, usersRepository, RIGHT, DELETE);
            Category category = CategoryService.deleteCategory(
                    req, 
                    categoriesRepository, 
                    rule
            );
            return new SuccessResponse<>(MSG_DELETED, category);
        }, transformer);
    }
}
