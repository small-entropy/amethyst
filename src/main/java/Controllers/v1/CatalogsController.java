package Controllers.v1;

import Controllers.core.AbstractController;
import DataTransferObjects.RuleDTO;
import Models.Standalones.Catalog;
import Responses.SuccessResponse;
import Services.v1.CatalogService;
import Sources.CatalogsSource;
import Sources.UsersSource;
import Transformers.JsonTransformer;
import Utils.constants.DefaultRights;
import Utils.constants.ResponseMessages;
import dev.morphia.Datastore;
import java.util.List;

import static spark.Spark.*;

/**
 * Class controller for work with catalogs routes
 * @author small-entropy
 */
public class CatalogsController extends AbstractController {
    
    private static final String RIGHT = DefaultRights.CATALOGS.getName();
    
    private static final String MSG_LIST = ResponseMessages.CATALOGS_LIST.getMessage();
    private static final String MSG_ENTITY = ResponseMessages.CATALOG.getMessage();
    private static final String MSG_CREATED = ResponseMessages.CATALOG_CREATED.getMessage();
    private static final String MSG_UPDATED = ResponseMessages.CATALOG_UPDATED.getMessage();
    private static final String MSG_DELETED = ResponseMessages.CATALOG_DELETED.getMessage();
    
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
            RuleDTO rule = getRule(req, userSource, RIGHT, READ);
            List<Catalog> catalogs = CatalogService.getCatalogs(
                    req, 
                    catalogsSource, 
                    rule
            );
            return new SuccessResponse<>(MSG_LIST, catalogs);
        }, transformer);

        // Route for create catalog
        post("/owner/:user_id", (req, res) -> {
            RuleDTO rule = getRule(req, userSource, RIGHT, CREATE);
            Catalog catalog = CatalogService.createCatalog(
                    req, 
                    catalogsSource, 
                    userSource, 
                    rule
            );
            return new SuccessResponse<>(MSG_CREATED, catalog);
        }, transformer);
        
        // Route for create catalog document
        get("/owner/:user_id", (req, res) -> {
            RuleDTO rule = getRule(req, userSource, RIGHT, READ);
            List<Catalog> catalogs = CatalogService.getCatalogsByUser(
                    req, 
                    catalogsSource, 
                    rule
            );
            return new SuccessResponse<>(MSG_LIST, catalogs);
        }, transformer);
        
        // Route for get user catalog by id
        get("/owner/:user_id/catalog/:catalog_id", (req, res) -> {
            RuleDTO rule = getRule(req, userSource, RIGHT, READ);
            Catalog catalog = CatalogService.getCatalogById(
                    req, 
                    catalogsSource, 
                    rule
            );
            return new SuccessResponse<>(MSG_ENTITY, catalog);
        }, transformer);
        
        // Route for update catalog
        put("/owner/:user_id/catalog/:catalog_id", (req, res) -> {
            RuleDTO rule = getRule(req, userSource, RIGHT, UPDATE);
            Catalog catalog = CatalogService.updateCatalog(
                    req, 
                    catalogsSource, 
                    rule
            );
            return new SuccessResponse<>(MSG_UPDATED, catalog);
        }, transformer); 
        
        // Route for delete catalog
        delete("/owner/:user_id/catalog/:catalog_id", (req, res) -> {
            RuleDTO rule = getRule(req, userSource, RIGHT, DELETE);
            Catalog catalog = CatalogService.deleteCatalog(
                    req, 
                    catalogsSource, 
                    rule
            );
            return new SuccessResponse<>(MSG_DELETED, catalog);
        }, transformer);
    }
}
