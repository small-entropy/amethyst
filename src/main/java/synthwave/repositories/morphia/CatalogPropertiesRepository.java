package synthwave.repositories.morphia;

import java.util.List;

import org.bson.types.ObjectId;

import dev.morphia.Datastore;
import synthwave.filters.CatalogsFilter;
import synthwave.models.morphia.extend.Catalog;
import engine.repositories.morphia.BasePropertyRepository;

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
