import com.mongodb.client.MongoClients;
import dev.morphia.Datastore;
import dev.morphia.Morphia;

import static spark.Spark.*;

public class Application {
    public static void main(String[] args) {


        final Datastore datastore = Morphia.createDatastore(MongoClients.create(), "Amethyst");
        datastore.getMapper().mapPackage("Amethyst.Models");
        datastore.getDatabase().drop();
        datastore.ensureIndexes();

        // Route for work with users list
        get("/users", (request, response) -> "Get users list");

        // Routes for user actions
        post("/users/register", (request, response) -> "Register new user");
        post("/users/login", (request, response) -> "Login route");
        post("/users/logout", (request, response) -> "User logout");
        get("/users/autologin", (request, response) -> "Autologin route");

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
