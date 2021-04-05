// Import UserController class
import Controllers.common.ApiController;
import Controllers.common.CORSController;
import Controllers.common.ErrorsController;
import Controllers.v1.UserController;
// Import JsonTransformer class
import Transformers.JsonTransformer;
// Import MongoClient class
import com.mongodb.client.MongoClients;
// Import Morphia classes
import dev.morphia.Datastore;
import dev.morphia.Morphia;
// Import Spark classes
import static spark.Spark.*;

/**
 * Enum with routes paths
 */
enum RoutesPath {
    API("/api"),
    VERSION_ONE("/v1"),
    USERS("/users");

    private String value;

    RoutesPath(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

/**
 * Enum for CORS configs
 */
enum CORSConfigs {
    ORIGINS("*"),
    METHODS("GET, POST, PUT, DELETE, OPTIONS"),
    HEADERS("Content-Type, api_key, Authorization");

    private String value;

    CORSConfigs(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

/** Main server class*/
public class Application {


    /**
     * Main function (run by start project)
     * @param args method arguments
     */
    public static void main(String[] args) {

        // Create database connection
        final Datastore store = Morphia.createDatastore(MongoClients.create(), "Amethyst");
        // Map all models from package
        store.getMapper().mapPackage("Models");
        // Ensure database indexes by models
        store.ensureIndexes();

        // Create JsonTransformer
        final JsonTransformer toJson = new JsonTransformer();

        // Enable CORS
        CORSController.enable(
                CORSConfigs.ORIGINS.getValue(),
                CORSConfigs.HEADERS.getValue(),
                CORSConfigs.METHODS.getValue()
        );

        // Grouped API routes
        path(RoutesPath.API.getValue(), ()-> {
            // Grouped API routes version 1
            path(RoutesPath.VERSION_ONE.getValue(), () -> {
                // Grouped API routes for work with users
                path(RoutesPath.USERS.getValue(), () -> UserController.routes(store, toJson));
            });
            // Callback after call all routes with /api/* pattern
            ApiController.afterCallCommon();
        });

        // Errors handling
        ErrorsController.errors_InternalServerError();
        ErrorsController.errors_Custom();

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

        // Routes for works with generics
    }
}
