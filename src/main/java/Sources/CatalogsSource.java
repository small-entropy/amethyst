package Sources;

import DataTransferObjects.CatalogDTO;
import Filters.CatalogsFilter;
import Models.Catalog;
import Models.CatalogOwner;
import Sources.Core.MorphiaSource;
import dev.morphia.Datastore;
import dev.morphia.query.FindOptions;
import static dev.morphia.query.experimental.filters.Filters.and;
import static dev.morphia.query.experimental.filters.Filters.eq;
import java.util.List;

/**
 * Class for datastore source for catalogs collection
 * @author small-entropy
 */
public class CatalogsSource extends MorphiaSource<Catalog, CatalogsFilter, CatalogDTO>{
    
    /**
     * Consturctor for datasource catalog collection
     * @param datastore Morphia datastore object
     */
    public CatalogsSource(Datastore datastore) {
        super(datastore, Catalog.class);
    }

    /**
     * Method for find one document by catalog name
     * @param filter catalogs filter object
     * @return catalog document
     */
    public Catalog findOneByName(CatalogsFilter filter) {
        FindOptions findOptions = new FindOptions()
                .projection()
                .exclude(filter.getExcludes());
        return getStore()
                .find(getModelClass())
                .filter(and(
                        eq("status", filter.getStatus()),
                        eq("name", filter.getName())
                ))
                .first(findOptions);
    }
    
    /**
     * Method for get catalog document by owner id and catalog id
     * @param filter catalog filter object
     * @return founded catalog document
     */
    public Catalog findOneByOwnerAndId(CatalogsFilter filter) {
        FindOptions findOptions = new FindOptions()
                .projection()
                .exclude(filter.getExcludes());
        return getStore()
                .find(getModelClass())
                .filter(and(
                        eq("status", filter.getStatus()),
                        eq("id", filter.getId()),
                        eq("owner.id", filter.getOwner())
                ))
                .first(findOptions);
    }
    
    /**
     * Method for get all catalogs by user id
     * @param filter catalogs filter obejct
     * @return list of user catalogs
     */
    public List<Catalog> findAllByOwnerId(CatalogsFilter filter) {
        // Craete find options by filter data
        FindOptions findOptions = new FindOptions()
                .projection()
                .exclude(filter.getExcludes())
                .skip(filter.getSkip())
                .limit(filter.getLimit());
        // Find in datastore by filter options & return list
        return getStore()
                .find(getModelClass())
                .filter(and(
                        eq("status", filter.getStatus()),
                        eq("owner.id", filter.getOwner())
                ))
                .iterator(findOptions)
                .toList();
    }
    
    /**
     * Method for create catalog document
     * @param catalogDTO catalog data transfer object
     * @return created catalog document
     */
    @Override
    public Catalog create(CatalogDTO catalogDTO) {
        CatalogOwner owner = new CatalogOwner(
                catalogDTO.getOwner().getPureId(),
                catalogDTO.getOwner().getUsername()
        );
        Catalog catalog = new Catalog(
                catalogDTO.getName(),
                catalogDTO.getTitle(),
                catalogDTO.getDescription(),
                owner
        );
        save(catalog);
        return catalog;
    }
}
