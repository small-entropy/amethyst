package Controllers.v1;

import Controllers.base.BaseCatalogsController;
import Models.Standalones.Catalog;
import Utils.responses.SuccessResponse;
import Services.v1.CatalogService;
import Utils.transformers.JsonTransformer;
import dev.morphia.Datastore;
import java.util.List;

import static spark.Spark.*;

/**
 * Class controller for work with catalogs routes
 * @author small-entropy
 */
public class CatalogsController extends BaseCatalogsController {
    
    /**
     * Static method for initialize catalogs routes
     * @param store Morphia datastore object
     * @param transformer converter to JSON
     */
    public static void routes(Datastore datastore, JsonTransformer transformer) {
        CatalogService service = new CatalogService(datastore);
        
        // Route for work with catalog list
        get("", (req, res) -> {
            List<Catalog> catalogs = service.getCatalogs(req, RIGHT, READ);
            return new SuccessResponse<>(MSG_LIST, catalogs);
        }, transformer);

        // Route for create catalog
        post("/owner/:user_id", (req, res) -> {
            Catalog catalog = service.createCatalog(req, RIGHT, CREATE);
            return new SuccessResponse<>(MSG_CREATED, catalog);
        }, transformer);
        
        // Route for create catalog document
        get("/owner/:user_id", (req, res) -> {
            List<Catalog> catalogs = service.getCatalogsByUser(
                    req, 
                    RIGHT, 
                    READ
            );
            return new SuccessResponse<>(MSG_LIST, catalogs);
        }, transformer);
        
        // Route for get user catalog by id
        get("/owner/:user_id/catalog/:catalog_id", (req, res) -> {
            Catalog catalog = service.getCatalogById(req, RIGHT, READ);
            return new SuccessResponse<>(MSG_ENTITY, catalog);
        }, transformer);
        
        // Route for update catalog
        put("/owner/:user_id/catalog/:catalog_id", (req, res) -> {
            Catalog catalog = service.updateCatalog(req, RIGHT, UPDATE);
            return new SuccessResponse<>(MSG_UPDATED, catalog);
        }, transformer); 
        
        // Route for delete catalog
        delete("/owner/:user_id/catalog/:catalog_id", (req, res) -> {
            Catalog catalog = service.deleteCatalog(req, RIGHT, DELETE);
            return new SuccessResponse<>(MSG_DELETED, catalog);
        }, transformer);
    }
}
