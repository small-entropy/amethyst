package Controllers.v1;

import Controllers.core.v1.AbstractController;
import Utils.transformers.JsonTransformer;
import dev.morphia.Datastore;
import static spark.Spark.*;

/**
 * Class controller for work with products collection
 * @author small-entropy
 */
public class ProductsController extends AbstractController {
 
    public static void routes(Datastore store, JsonTransformer transformer) {
        get("", (req, res) -> "Get products list");
        get("/:product_id", (req, res) -> "Get product document");
        get("/catalog/:catalog_id", (req, res) -> "Get catelog products");
        get("/categories/:category_id", (req, res) -> "Get product for category");
        get("/owner/:user_id", (req, res) -> "Get user products");
        get("/owner/:user_id/product/:product_id", (req, res) -> "Get product document");
        put("/owner/:user_id/product/:product_id", (req, res) -> "Update product document");
        delete("/owner/:user_id/product/:product_id", (req, res) -> "Delete product document");
        get("/owner/:user_id/catalog/:catalog_id", (req, res) -> "Get user products for catelog");
        post("/owner/:user_id/catalog/:catalog_id", (req, res) -> "Create product for catalog");
        get("/owner/:user_id/categories/:category_id", (req, res) -> "");
    }
}
