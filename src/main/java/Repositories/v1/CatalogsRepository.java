package Repositories.v1;

import DataTransferObjects.CatalogDTO;
import Exceptions.DataException;
import Filters.CatalogsFilter;
import Models.Standalones.Catalog;
import Models.Embeddeds.EmbeddedOwner;
import Repositories.Core.MorphiaRpository;
import dev.morphia.Datastore;

/**
 * Class for datastore source for catalogs collection
 * @author small-entropy
 */
public class CatalogsRepository 
        extends MorphiaRpository<Catalog, CatalogsFilter, CatalogDTO>{
    
    /**
     * Consturctor for datasource catalog collection
     * @param datastore Morphia datastore object
     */
    public CatalogsRepository(Datastore datastore) {
        super(datastore, Catalog.class);
    }

    
    /**
     * Method for create catalog document
     * @param catalogDTO catalog data transfer object
     * @return created catalog document
     */
    @Override
    public Catalog create(CatalogDTO catalogDTO) {
        EmbeddedOwner owner = new EmbeddedOwner(
                catalogDTO.getOwner().getId(),
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

    /**
     * Method for update catalog document
     * @param catalogDTO catalog data transfer object
     * @param filter catalog filter document
     * @return updated catalog document
     * @throws DataException 
     */
    public Catalog update(
            CatalogDTO catalogDTO,
            CatalogsFilter filter
    ) throws DataException {
        Catalog catalog = findOneByOwnerAndId(filter);
        var description = catalogDTO.getDescription();
        var title = catalogDTO.getTitle();
        if (catalog != null) {
            if (title != null && (catalog.getTitle() == null 
                    || !catalog.getTitle().equals(title))) {
                catalog.setTitle(title);
            }
            if (description != null && (catalog.getDescription() == null
                    || !catalog.getDescription().equals(description))) {
                catalog.setDescription(description);
            }
            save(catalog);
            return catalog;
        } else {
            Error error = new Error("Can not find catalog document");
            throw new DataException("NotFound", error);
        }
    }

    /**
     * Method for deactivate catalog
     * @param filter catalog filter object
     * @return deactivated document
     * @throws DataException
     */
    public Catalog deactivate(CatalogsFilter filter) throws DataException {
        Catalog catalog = findOneByOwnerAndId(filter);
        if (catalog != null) {
            catalog.deactivate();
            save(catalog);
            return catalog;
        } else {
            Error error = new Error("Can not find catalog document");
            throw new DataException("NotDounf", error);
        }
    }
}
