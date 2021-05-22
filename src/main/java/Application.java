// Import UserController class
import Controllers.common.ApiController;
import Controllers.common.CORSController;
import Controllers.core.ErrorsController;
import Controllers.v1.*;
// Import JsonTransformer class
import Transformers.JsonTransformer;
// Import MongoClient class
import com.mongodb.client.MongoClients;
// Import Morphia classes
import dev.morphia.Datastore;
import dev.morphia.Morphia;
import dev.morphia.mapping.MapperOptions;
// Import Spark classes
import static spark.Spark.*;

/**
 * Enum with routes paths
 */
enum RoutesPath {
    API("/api"), // endpoint for server API
    VERSION_ONE("/v1"), // endpoint for API version 1
    USERS("/users"), // endpoint for users API
    CATALOGS("/catalogs"), // endpoint for catalogs API
    CATEGORIES("/categories"); //endpoint for categories API
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
    ORIGINS("*"), // default config for allowed origin
    METHODS("GET, POST, PUT, DELETE, OPTIONS"), // default config for allowed methods
    HEADERS("Content-Type, api_key, Authorization"); // default config for allowed headers
    private final String value;
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
        MapperOptions options = MapperOptions.builder()
                .storeNulls(true)
                .storeEmpties(true)
                .build();
        
        // Create database connection
        final Datastore store = Morphia.createDatastore(MongoClients.create(), "Amethyst", options);
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
                path(RoutesPath.USERS.getValue(), () -> {
                    // Routes for register user, login user, logout user
                    // & autologin by token
                    AuthorizationController.routes(store, toJson);
                    // Routes for work with user document
                    UserController.routes(store, toJson);
                    // Routes for work with user property documents
                    UserPropertyController.routes(store, toJson);
                    // Routs for work with profile documents
                    UserProfileController.routes(store, toJson);
                    // Routes for work with user rights documents
                    UserRightsController.routes(store, toJson);
                });
                // Grouped API routes for work with catalogs
                path(RoutesPath.CATALOGS.getValue(), () -> CatalogsController.routes(store, toJson));
                // Groupted API routes for categories
                path(RoutesPath.CATEGORIES.getValue(), () -> CategoriesController.routes(store, toJson));
            });
            // Callback after call all routes with /api/* pattern
            ApiController.afterCallCommon();
        });

        // Errors handling
        ErrorsController.errors_InternalServerError();
        ErrorsController.errors_ExternalPackagesErrors();
        ErrorsController.errors_Custom();
    }
}
