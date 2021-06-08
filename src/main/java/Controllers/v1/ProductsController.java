package Controllers.v1;

import Controllers.core.AbstractController;
import Transformers.JsonTransformer;
import Utils.constants.DefaultRights;
import Utils.constants.ResponseMessages;
import dev.morphia.Datastore;
import static spark.Spark.*;

/**
 *
 * @author small-entropy
 */
public class ProductsController extends AbstractController {
    
    private static final String RULE = DefaultRights.PRODUCTS.getName();

    private static final String MSG_LIST = ResponseMessages.PRODUCTS.getMessage();
    private static final String MSG_ENTITY = ResponseMessages.PRODUCT.getMessage();
    private static final String MSG_CREATED = ResponseMessages.PRODUCT_CREATED.getMessage();
    private static final String MSG_UPDATED = ResponseMessages.PRODUCT_UPDATED.getMessage();
    private static final String MSG_DELETED = ResponseMessages.PRODUCT_DELETED.getMessage();

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
