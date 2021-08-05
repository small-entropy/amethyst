package synthwave.controllers.v1.catalogs;

import platform.constants.DefaultRights;
import platform.utils.transformers.JsonTransformer;

import synthwave.controllers.base.RESTController;
import synthwave.controllers.messages.CatalogsMessages;
import synthwave.models.mongodb.standalones.Catalog;
import synthwave.repositories.mongodb.v1.CatalogsRepository;
import synthwave.services.v1.catalogs.CatalogService;

import dev.morphia.Datastore;
/**
 * Class controller for work with catalogs routes
 * @author small-entropy
 * @version 1
 */
public class CatalogsController 
	extends RESTController<Catalog, CatalogsRepository, CatalogService> {
    
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
				DefaultRights.CATALOGS.getName(),
				null,
				"",
				"/owner/:user_id/catalog/:catalog_id",
				"/owner/:user_id",
				true,
				true,
				true,
				true,
				true,
				CatalogsMessages.CREATED.getMessage(),
				CatalogsMessages.LIST.getMessage(),
				CatalogsMessages.ENTITY.getMessage(),
				CatalogsMessages.UPDATED.getMessage(),
				CatalogsMessages.DELETED.getMessage()
		);
	}
}
