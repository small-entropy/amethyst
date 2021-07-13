// Import UserController class
import synthwave.controllers.v1.CompaniesController;
import synthwave.controllers.v1.UserRightsController;
import synthwave.controllers.v1.ProductsController;
import synthwave.controllers.v1.CategoriesController;
import synthwave.controllers.v1.AuthorizationController;
import synthwave.controllers.v1.UserPropertyController;
import synthwave.controllers.v1.UserProfileController;
import synthwave.controllers.v1.UserController;
import synthwave.controllers.v1.TagsController;
import synthwave.controllers.v1.CatalogsController;
import platform.middlewares.ResponseTypeJSON;
import platform.middlewares.CORS;
import synthwave.controllers.common.ErrorsController;
// Import JsonTransformer class
import platform.utils.transformers.JsonTransformer;
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
    CATEGORIES("/categories"),
    TAGS("/tags"),
    COMPANIES("/companies"),
    PRODUCTS("/products"); //endpoint for categories API
    private final String value;
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
        final Datastore datastore = Morphia.createDatastore(
                MongoClients.create(), 
                "Amethyst", 
                options
        );
        // Map all models from package
        datastore.getMapper().mapPackage("synthwave.models.mongodb.standalones");
        // Ensure database indexes by models
        datastore.ensureIndexes();

        // Create JsonTransformer
        final JsonTransformer toJson = new JsonTransformer();

        // Enable CORS
        CORS.enable(
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
                    new AuthorizationController(datastore, toJson).register();
                    new UserController(datastore, toJson).register();
                    new UserPropertyController(datastore, toJson).register();
                    new UserProfileController(datastore, toJson).register();
                    new UserRightsController(datastore, toJson).register();
                });
                // Grouped API routes for work with catalogs
                path(
                        RoutesPath.CATALOGS.getValue(), 
                        () -> new CatalogsController(datastore, toJson).register()
                );
                // Groupted API routes for categories
                path(
                        RoutesPath.CATEGORIES.getValue(), 
                        () -> new CategoriesController(datastore, toJson).register()
                );
                // Grouped API routes for companies
                path(
                        RoutesPath.COMPANIES.getValue(),
                        () -> new CompaniesController(datastore, toJson).register()
                );
                // Grouped API routes for tags
                path(
                        RoutesPath.TAGS.getValue(),
                        () -> new TagsController(datastore, toJson).register()
                );
                // Grouped API routes for work with products
                path(
                        RoutesPath.PRODUCTS.getValue(), 
                        () -> new ProductsController(datastore, toJson).register()
                );
            });
            // Callback after call all routes with /api/* pattern
            ResponseTypeJSON.afterCall();
        });

        // Errors handling
        ErrorsController.errors_InternalServerError();
        ErrorsController.errors_ExternalPackagesErrors();
        ErrorsController.errors_Custom();
    }
}
