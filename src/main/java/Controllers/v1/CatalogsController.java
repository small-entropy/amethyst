package Controllers.v1;

import Controllers.base.BaseCatalogsController;
import DataTransferObjects.v1.RuleDTO;
import Models.Standalones.Catalog;
import Utils.responses.SuccessResponse;
import Services.v1.CatalogService;
import Repositories.v1.CatalogsRepository;
import Repositories.v1.UsersRepository;
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
    public static void routes(Datastore store, JsonTransformer transformer) {
        // Create catalog datastore source
        CatalogsRepository catalogsRepository = new CatalogsRepository(store);
        // Create user datastore source
        UsersRepository usersRepository = new UsersRepository(store);
        
        // Route for work with catalog list
        get("", (req, res) -> {
            RuleDTO rule = getRule(req, usersRepository, RIGHT, READ);
            List<Catalog> catalogs = CatalogService.getCatalogs(
                    req, 
                    catalogsRepository, 
                    rule
            );
            return new SuccessResponse<>(MSG_LIST, catalogs);
        }, transformer);

        // Route for create catalog
        post("/owner/:user_id", (req, res) -> {
            RuleDTO rule = getRule(req, usersRepository, RIGHT, CREATE);
            Catalog catalog = CatalogService.createCatalog(
                    req, 
                    catalogsRepository, 
                    usersRepository, 
                    rule
            );
            return new SuccessResponse<>(MSG_CREATED, catalog);
        }, transformer);
        
        // Route for create catalog document
        get("/owner/:user_id", (req, res) -> {
            RuleDTO rule = getRule(req, usersRepository, RIGHT, READ);
            List<Catalog> catalogs = CatalogService.getCatalogsByUser(
                    req, 
                    catalogsRepository, 
                    rule
            );
            return new SuccessResponse<>(MSG_LIST, catalogs);
        }, transformer);
        
        // Route for get user catalog by id
        get("/owner/:user_id/catalog/:catalog_id", (req, res) -> {
            RuleDTO rule = getRule(req, usersRepository, RIGHT, READ);
            Catalog catalog = CatalogService.getCatalogById(
                    req, 
                    catalogsRepository, 
                    rule
            );
            return new SuccessResponse<>(MSG_ENTITY, catalog);
        }, transformer);
        
        // Route for update catalog
        put("/owner/:user_id/catalog/:catalog_id", (req, res) -> {
            RuleDTO rule = getRule(req, usersRepository, RIGHT, UPDATE);
            Catalog catalog = CatalogService.updateCatalog(
                    req, 
                    catalogsRepository, 
                    rule
            );
            return new SuccessResponse<>(MSG_UPDATED, catalog);
        }, transformer); 
        
        // Route for delete catalog
        delete("/owner/:user_id/catalog/:catalog_id", (req, res) -> {
            RuleDTO rule = getRule(req, usersRepository, RIGHT, DELETE);
            Catalog catalog = CatalogService.deleteCatalog(
                    req, 
                    catalogsRepository, 
                    rule
            );
            return new SuccessResponse<>(MSG_DELETED, catalog);
        }, transformer);
    }
}
