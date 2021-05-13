package Controllers.v1;

import DataTransferObjects.RuleDTO;
import Models.Catalog;
import Responses.SuccessResponse;
import Services.v1.CatalogService;
import Sources.CatalogsSource;
import Sources.UsersSource;
import Transformers.JsonTransformer;
import Utils.constants.DefaultActions;
import Utils.constants.DefaultRights;
import Utils.v1.RightManager;
import dev.morphia.Datastore;
import java.util.List;

import static spark.Spark.*;

/**
 * Class controller for work with catalogs routes
 * @author small-entropy
 */
public class CatalogsController {
    /**
     * Messages for success answers catalogs routes
     */
    private enum Messages {
        CATALOGS("Successfully get list of catalogs"),
        CATALOG("Successfully get catalog"),
        CREATED("Successfully created catalog"),
        UPDATED("Successfully catapog updated"),
        DELETED("Successfully deleted catalog");
        // MEssage property
        private final String message;

        /**
         * Default contructor for message enum
         * @param message 
         */
        Messages(String message) {
            this.message = message;
        }

        /**
         * Getter for message property
         * @return message text
         */
        public String getMessage() {
            return message;
        }
    }
    
    /**
     * Static method for initialize catalogs routes
     * @param store Morphia datastore object
     * @param transformer converter to JSON
     */
    public static void routes(Datastore store, JsonTransformer transformer) {
        // Create catalog datastore source
        CatalogsSource catalogsSource = new CatalogsSource(store);
        // Create user datastore source
        UsersSource userSource = new UsersSource(store);
        
        // Route for work with catalog list
        get("", (req, res) -> {
            List<Catalog> catalogs = CatalogService.getCatalogs(req, catalogsSource);
            return new SuccessResponse<>(Messages.CATALOGS.getMessage(), catalogs);
        }, transformer);

        // Route for create catalog
        post("/users/:id", (req, res) -> {
            RuleDTO rule = RightManager.getRuleByRequest_Token(req, userSource, DefaultRights.CATALOGS.getName(), DefaultActions.CREATE.getName());
            Catalog catalog = CatalogService.createCatalog(req, catalogsSource, userSource, rule);
            return new SuccessResponse<>(Messages.CREATED.getMessage(), catalog);
        }, transformer);
        
        // Route for create catalog document
        get("/users/:id", (req, res) -> {
            List<Catalog> catalogs = CatalogService.getCatalogsByUser(req, catalogsSource);
            return new SuccessResponse<>(Messages.CATALOGS.getMessage(), catalogs);
        }, transformer);
    }
}
