// Import UserController class
import Controllers.common.ApiController;
import Controllers.common.CORSController;
import Controllers.common.ErrorsController;
import Controllers.v1.CatalogsController;
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
    USERS("/users"),
    CATALOGS("/catalogs");
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
                // Grouped API routes for work with catalogs
                path(RoutesPath.CATALOGS.getValue(), () -> CatalogsController.routes(store, toJson));
            });
            // Callback after call all routes with /api/* pattern
            ApiController.afterCallCommon();
        });

        // Errors handling
        ErrorsController.errors_InternalServerError();
        ErrorsController.errors_Custom();
    }
}
