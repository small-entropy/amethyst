// Import UserController class
import Controllers.UserController;
// Import JsonTransformer class
import Utils.JsonTransformer;
// Import MongoClient class
import com.mongodb.client.MongoClients;
// Import Morphia classes
import dev.morphia.Datastore;
import dev.morphia.Morphia;
// Import Spark classes
import static spark.Spark.*;

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
        // Enshure database indexes by models
        store.ensureIndexes();

        // Create JsonTransformer
        final JsonTransformer toJson = new JsonTransformer();

        // Enable CORS
        before((req, res) -> {
            res.header("Access-Control-Allow-Origin", "*");
            res.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
            res.header("Access-Control-Allow-Headers", "Content-Type, api_key, Authorization");
        });

        // Grouped API routes
        path("/api", ()-> {
            // Grouped API routes version 1
            path("/v1", () -> {
                // Group users routes
                path("/users", () -> {
                    // Route for work with users list
                    get("", (req, res) ->
                            UserController.getList(req, res, store), toJson);
                    // Route for register user
                    post("/register", (req, res) ->
                            UserController.registerUser(req, res, store), toJson);
                    // Route for user login
                    post("/login", (req, res) ->
                            UserController.loginUser(req, res, store), toJson);
                    // Route for user autologin
                    get("/autologin", (req, res) ->
                            UserController.autoLoginUser(req, res, store), toJson);
                    // Logout user
                    get("/logout", (req, res) ->
                            UserController.logoutUser(req, res, store), toJson);
                });
            });
            after("/api/*", (req, res) -> {
                res.type("application/json");
            });
        });

        // Routes for work with user document
        get("/users/:id", (request, response) -> "Get user data");
        put("/users/:id", (request, response) -> "Update user data");
        delete("/users/:id", (request, response) -> "Remove user document");

        // Routes for work with user properties
        get("/users/:id/properties", (request, response) -> "User properties list");
        post("/users/:id/properties", (request, response) -> "Create user properties");
        get("/users/:id/properties/:id", (request, response) -> "Get user property");
        put("/users/:id/properties/:id", (request, response) -> "Update user property");
        delete("/users/:id/properties/:id", (request, response) -> "Remove user property");

        // Routes for work with users orders
        get("/users/:id/orders", (request, response) -> "Get users orders list");
        post("/users/:id/orders", (request, response) -> "Create user order");
        get("/users/:id/orders/:id", (request, response) -> "Get user order");
        put("/users/:id/orders/:id", (request, response) -> "Update user order");
        delete("/users/:id/orders/:id", (request, response) -> "Remove user order");

        // Routes for work with user rights
        get("/users/:id/rights", (request, response) -> "Get user rights");
        post("/users/:id/rights", (request, response) -> "Create user right");
        get("/users/:id/rights/:id", (request, response) -> "Get user right");
        put("/users/:id/rights/:id", (request, response) -> "Update user right");
        delete("/user/:id/rights/:id", (request, response) -> "Remove user right");

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
