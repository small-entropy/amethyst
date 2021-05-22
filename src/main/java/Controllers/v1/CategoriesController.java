package Controllers.v1;

import Transformers.JsonTransformer;
import dev.morphia.Datastore;
import static spark.Spark.*;

/**
 * Class-controller for categories
 * @author small-entropy
 */
public class CategoriesController {
    public static void routes(Datastore store, JsonTransformer transformer) {
        
        // Route for get categories list
        get("", (req, res) -> "Get categories list");
        
        get("/owner/user_id", (req, res) -> "Get category by owner");
        
        get("/catalog/:catalog_id", (req, res) -> "Get categories by catalog");
        
        // Route for get catalog categories
        get("/catalog/:catalog_id", (req, res) -> "Get categories by catalog");
       
        // Route for create category for catelog
        post("/catalog/:catalog_id/owner/:user_id", (req, res) -> "Create category for catalog");
        
        // Route for get category
        get("/:category_id", (req, res) -> "Get category by id");
        
        // Route for update category
        put("/:category_id", (req, res) -> "Update category");
        
        // Routt for mark to delete category
        delete("/:category_id", (req, res) -> "Mark to delete category");
    }
}
