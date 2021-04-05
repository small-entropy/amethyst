package Controllers.v1;

import Transformers.JsonTransformer;
import dev.morphia.Datastore;

import static spark.Spark.*;

public class CatalogsController {
    public static void routes(Datastore store, JsonTransformer transformer) {
        // Route for work with catalog list
        get("/catalogs", (request, response) -> "Get catalogs list");

        // Routes for work with catalog documents
        get("/catalogs/:id", (request, response) -> "Get catalog");
        post("/catalogs/:id", (request, response) -> "Create catalog document");
        put("/catalogs/:id", (request, response) -> "Update catalog document");
        delete("/catalogs/:id", (request, response) -> "Remove catalog document");

        // Routes for work with catalog categories
        get("/catalogs/:id/categories", (request, response) -> "Get categories list");
        post("/catalogs/:id/categories", (request, response) -> "Create category");
        get("/catalogs/:id/categories/:id", (request, response) -> "Get category document");
        put("/catalogs/:id/categories/:id", (request, response) -> "Update category document");
        delete("/catalogs/:id/categories/:id", (request, response) -> "Remove category document");

        // Routes for work with category products
        get("/catalogs/:id/categories/:id/products", (request, response) -> "Get product list");
        post("/catalogs/:id/categories/:id/products", (request, response) -> "Create product document");
        get("/catalogs/:id/categories/:id/products/:id", (request, response) -> "Get product");
        put("/catalogs/:id/categories/:id/products/:id", (request, response) -> "Update product");
        delete("/catalogs/:id/categories/:id/products/:id", (request, response) -> "Remove product");

        // Routes for work with products properties
        get("/catalogs/:id/categories/:id/products/:id/properties", (request, response) -> "Get product properties list");
        post("/catalogs/:id/categories/:id/products/:id/properties", (request, response) -> "Create product properties");
        get("/catalogs/:id/categories/:id/products/:id/properties/:id", (request, response) -> "Get product property");
        put("/catalogs/:id/categories/:id/products/:id/properties/:id", (request, response) -> "Update product property");
        delete("/catalogs/:id/categories/:id/products/:id/properties/:id", (request, response) -> "Remove product property");
    }
}
