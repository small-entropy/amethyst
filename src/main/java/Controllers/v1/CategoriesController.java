package Controllers.v1;

import DataTransferObjects.RuleDTO;
import Models.Category;
import Responses.SuccessResponse;
import Services.v1.CategoryService;
import Sources.CatalogsSource;
import Sources.CategoriesSource;
import Sources.UsersSource;
import Transformers.JsonTransformer;
import Utils.constants.DefaultActions;
import Utils.constants.DefaultRights;
import Utils.constants.ResponseMessages;
import Utils.v1.RightManager;
import dev.morphia.Datastore;
import static spark.Spark.*;

/**
 * Class-controller for categories
 * @author small-entropy
 */
public class CategoriesController {
    public static void routes(Datastore store, JsonTransformer transformer) {
        // Create catalog datastore source
        CatalogsSource catalogsSource = new CatalogsSource(store);
        // Create user datastore source
        UsersSource userSource = new UsersSource(store);
        // Create categories datastore source
        CategoriesSource categoriesSource = new CategoriesSource(store);
        
        // Route for get categories list
        get("", (req, res) -> "Get categories list");
        
        get("/owner/user_id", (req, res) -> "Get category by owner");
        
        get("/catalog/:catalog_id", (req, res) -> "Get categories by catalog");
        
        // Route for get catalog categories
        get("/catalog/:catalog_id", (req, res) -> "Get categories by catalog");
       
        // Route for create category for catelog
        post("/catalog/:catalog_id/owner/:user_id", (req, res) -> {
            RuleDTO rule = RightManager.getRuleByRequest_Token(req, userSource, DefaultRights.CATEGORIES.getName(), DefaultActions.CREATE.getName());
            Category category = CategoryService.createCategory(req, categoriesSource, catalogsSource, userSource, rule);
            return new SuccessResponse<>(ResponseMessages.CATEGORY_CREATED.getMessage(), category);
        }, transformer);
        
        // Route for get category
        get("/:category_id", (req, res) -> "Get category by id");
        
        // Route for update category
        put("/:category_id", (req, res) -> "Update category");
        
        // Routt for mark to delete category
        delete("/:category_id", (req, res) -> "Mark to delete category");
    }
}
