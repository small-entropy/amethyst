package synthwave.controllers.v1;

import synthwave.controllers.messages.CatalogsMessages;
import synthwave.models.mongodb.standalones.Catalog;
import synthwave.services.v1.catalogs.CatalogService;
import platform.constants.DefaultRights;
import platform.controllers.BaseController;
import platform.utils.responses.SuccessResponse;
import platform.utils.transformers.JsonTransformer;
import dev.morphia.Datastore;
import java.util.List;

import static spark.Spark.*;

/**
 * Class controller for work with catalogs routes
 * @author small-entropy
 * @version 1
 */
public class CatalogsController 
	extends BaseController<CatalogService, JsonTransformer> {
    
	/**
	 * Default constructor for catalogs controller object
	 * @param datastore Morphia datastore
	 * @param transformer response transformer object
	 */
	public CatalogsController(
			Datastore datastore,
			JsonTransformer transformer
	) {
		super(
				new CatalogService(datastore), 
				transformer,
				DefaultRights.CATALOGS.getName()
		);
	}
	
	/**
	 * Method for get catalogs entities
	 */
	protected void getCatalogstRoute() {
		get("", (request, ressponse) -> {
            List<Catalog> catalogs = getService().getCatalogs(
            		request, 
            		getRight(), 
            		getReadActionName()
            );
            return new SuccessResponse<>(
            		CatalogsMessages.LIST.getMessage(), 
            		catalogs
            );
        }, getTransformer());
	}
	
	/**
	 * Method for create catalog entity
	 */
	protected void createCatalogRoute() {
		 post("/owner/:user_id", (requser, response) -> {
	            Catalog catalog = getService().createCatalog(
	            		requser, 
	            		getRight(), 
	            		getCreateActionName()
	            );
	            return new SuccessResponse<>(
	            		CatalogsMessages.CREATED.getMessage(), 
	            		catalog
	            );
	        }, getTransformer());
	}
	
	/**
	 * Method for get catalogs entities by owner 
	 */
	protected void getCatalogsByOwnerRoute() {
		get("/owner/:user_id", (request, ressponse) -> {
            List<Catalog> catalogs = getService().getCatalogsByUser(
                    request, 
                    getRight(), 
                    getReadActionName()
            );
            return new SuccessResponse<>(
            		CatalogsMessages.ENTITY.getMessage(), 
            		catalogs
            );
        }, getTransformer());
	}
	
	/**
	 * Method for get catalog entity by owner id and entity id
	 */
	protected void getCatalogByOwnerAndIdRoute() {
		get("/owner/:user_id/catalog/:catalog_id", (request, ressponse) -> {
            Catalog catalog = getService().getCatalogById(
            		request, 
            		getRight(), 
            		getReadActionName()
            );
            return new SuccessResponse<>(
            		CatalogsMessages.ENTITY.getMessage(), 
            		catalog
            );
        }, getTransformer());
	}
	
	/**
	 * Method for update catalog entity
	 */
	protected void updateCatalogRoute() {
		put("/owner/:user_id/catalog/:catalog_id", (request, response) -> {
            Catalog catalog = getService().updateCatalog(
            		request, 
            		getRight(), 
            		getUpdateActionName()
            );
            return new SuccessResponse<>(
            		CatalogsMessages.UPDATED.getMessage(), 
            		catalog
            );
        }, getTransformer());
	}
	
	/**
	 * Method for deactivate catalog entity
	 */
	protected void deleteCatalogRoute() {
		delete("/owner/:user_id/catalog/:catalog_id", (request, response) -> {
            Catalog catalog = getService().deleteCatalog(
            		request, 
            		getRight(), 
            		getDeleteActionName()
            );
            return new SuccessResponse<>(
            		CatalogsMessages.DELETED.getMessage(), 
            		catalog
            );
        }, getTransformer());
	}
	
    /**
     * Method for initialize catalogs routes
     * @param store Morphia datastore object
     * @param transformer converter to JSON
     */
	@Override
    public void register() { 
        createCatalogRoute();
    	getCatalogstRoute();
        getCatalogsByOwnerRoute();
        getCatalogByOwnerAndIdRoute();
        updateCatalogRoute();
        deleteCatalogRoute();
    }
}
