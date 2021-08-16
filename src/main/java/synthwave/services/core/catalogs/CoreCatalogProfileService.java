package synthwave.services.core.catalogs;

import java.util.Arrays;
import java.util.List;
import org.bson.types.ObjectId;

import dev.morphia.Datastore;
import core.exceptions.DataException;
import core.utils.ParamsManager;
import spark.Request;
import synthwave.filters.CatalogsFilter;
import core.models.morphia.embeddeds.EmbeddedProperty;
import synthwave.models.morphia.extend.Catalog;
import synthwave.repositories.morphia.CatalogProfileRepository;
import synthwave.repositories.morphia.CatalogsRepository;
import synthwave.services.abstracts.CRUDEmbeddedPropertyService;

/**
 * Base class for work with catalog profile property list
 * @author small-entropy
 */
public abstract class CoreCatalogProfileService 
	extends CRUDEmbeddedPropertyService<Catalog, CatalogsFilter, CatalogsRepository, CatalogProfileRepository> {
	
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

	/**
	 * Static method for get default catalog profile properties
	 * @return catalog profile default properties list 
	 */
	public static List<EmbeddedProperty> getDefaultProfile() {
		long currentDateTime = System.currentTimeMillis();
		EmbeddedProperty created = new EmbeddedProperty(
			"created", 
			currentDateTime
		);
		return Arrays.asList(created);

	}
}
