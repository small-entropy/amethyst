package synthwave.repositories.mongodb.v1;

import java.util.List;

import org.bson.types.ObjectId;

import dev.morphia.Datastore;
import synthwave.filters.CatalogsFilter;
import synthwave.models.mongodb.standalones.Catalog;
import synthwave.repositories.mongodb.base.BasePropertyRepository;

/**
 * Class for create instance catalog properties repository
 * @author small-entropy
 */
public class CatalogPropertiesRepository 
    extends BasePropertyRepository<Catalog, CatalogsFilter, CatalogsRepository> {

    /**
     * Default constructor for catalogs properties repository
     * @param datastore Morphia datastore object
     * @param blacklist blacklist of fields
     */
    public CatalogPropertiesRepository(
        Datastore datastore,
        List<String> blacklist
    ) {
        super("properties", blacklist, new CatalogsRepository(datastore));
    }

    @Override
    public Catalog findOneById(ObjectId entityId, String[] excludes) {
        CatalogsFilter filter = new CatalogsFilter(entityId, excludes);
        return this.getRepository().findOneById(filter);
    }

    @Override
    public void save(Catalog catalog) {
        this.getRepository().save(catalog);
    }
}
