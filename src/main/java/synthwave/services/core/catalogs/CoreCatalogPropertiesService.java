package synthwave.services.core.catalogs;

import java.util.List;

import org.bson.types.ObjectId;

import dev.morphia.Datastore;
import core.exceptions.DataException;
import core.utils.ParamsManager;
import spark.Request;
import synthwave.filters.CatalogsFilter;
import core.models.morphia.embeddeds.EmbeddedProperty;
import synthwave.models.morphia.extend.Catalog;
import synthwave.repositories.morphia.CatalogPropertiesRepository;
import synthwave.repositories.morphia.CatalogsRepository;
import synthwave.services.abstracts.CRUDEmbeddedPropertyService;

/**
 * Class with core methods for work with catalog properties field
 * @author small-entopy
 */
public abstract class CoreCatalogPropertiesService 
    extends CRUDEmbeddedPropertyService <Catalog, CatalogsFilter, CatalogsRepository, CatalogPropertiesRepository> {

    /**
     * Default constructor for service. Create by 
     * datastore & blacklist 
     * @param datastore Morphia datastore object
     * @param blacklist blacklist of fields
     */
    public CoreCatalogPropertiesService(
        Datastore datastore,
        List<String> blacklist
    ) {
        super(datastore,new CatalogPropertiesRepository(datastore, blacklist));
    }

    @Override
	protected ObjectId getEntityIdFromRequest(Request request) throws DataException {
		return ParamsManager.getCatalogId(request);
	}

    /**
     * Method for get default list catalog properties
     * @return list of default catalog properties
     */
    public static List<EmbeddedProperty> getDefaultCatalogProperties() {
        return null;
    }
}
