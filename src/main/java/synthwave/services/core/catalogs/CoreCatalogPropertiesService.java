package synthwave.services.core.catalogs;

import java.util.List;

import org.bson.types.ObjectId;

import dev.morphia.Datastore;
import platform.exceptions.DataException;
import platform.utils.helpers.ParamsManager;
import spark.Request;
import synthwave.filters.CatalogsFilter;
import synthwave.models.mongodb.standalones.Catalog;
import synthwave.repositories.mongodb.v1.CatalogPropertiesRepository;
import synthwave.repositories.mongodb.v1.CatalogsRepository;
import synthwave.services.core.base.BaseEmbeddedPropertiesService;

/**
 * Class with core functions for work with catalog properties field
 * @author small-entopy
 */
public abstract class CoreCatalogPropertiesService 
    extends BaseEmbeddedPropertiesService <Catalog, CatalogsFilter, CatalogsRepository, CatalogPropertiesRepository> {

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
        super(
            datastore,
            new CatalogPropertiesRepository(datastore, blacklist)
        );
    }

    @Override
	protected ObjectId getEntityIdFromRequest(Request request) throws DataException {
		return ParamsManager.getCatalogId(request);
	}
}
