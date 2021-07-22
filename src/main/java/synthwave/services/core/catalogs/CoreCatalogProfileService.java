package synthwave.services.core.catalogs;

import java.util.List;
import org.bson.types.ObjectId;

import dev.morphia.Datastore;
import platform.exceptions.DataException;
import platform.utils.helpers.ParamsManager;
import spark.Request;
import synthwave.filters.CatalogsFilter;
import synthwave.models.mongodb.standalones.Catalog;
import synthwave.repositories.mongodb.v1.CatalogProfileRepository;
import synthwave.repositories.mongodb.v1.CatalogsRepository;
import synthwave.services.core.base.BaseEmbeddedPropertiesService;

/**
 * Base class for work with catalog profile property list
 * @author small-entropy
 */
public abstract class CoreCatalogProfileService 
	extends BaseEmbeddedPropertiesService<Catalog, CatalogsFilter, CatalogsRepository, CatalogProfileRepository> {
	
	/**
	 * Default core catalog profile service constructor. Create 
	 * instance by datastore & blacklist
	 * @param datastore Morphia datastore object
	 * @param blacList  blacklist
	 */
	public CoreCatalogProfileService(
			Datastore datastore,
			List<String> blacList
	) {
		super(
				datastore, 
				new CatalogProfileRepository(datastore, blacList)
		);
	}
	
	/**
	 * Method for get entity id from request
	 * @param request Spark request object
	 * @return founded id
	 */
    @Override
	protected ObjectId getEntityIdFromRequest(Request request) throws DataException {
		return ParamsManager.getCatalogId(request);
	}
}
