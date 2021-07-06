package synthwave.repositories.mongodb.v1;

import synthwave.dto.CatalogDTO;
import synthwave.filters.CatalogsFilter;
import synthwave.models.mongodb.standalones.Catalog;
import synthwave.models.mongodb.embeddeds.EmbeddedOwner;
import platform.repositories.mongodb.orm.MorphiaRepository;
import dev.morphia.Datastore;

/**
 * Class for datastore source for catalogs collection
 * @author small-entropy
 */
public class CatalogsRepository 
        extends MorphiaRepository<Catalog, CatalogsFilter, CatalogDTO>{
    
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
     * Handler for method update catalog document
     * @param catalogDTO catalog data transfer object
     * @param catalog catalog document
     * @return result of changed
     */
    @Override
    protected boolean updateHandler(CatalogDTO catalogDTO, Catalog catalog) {
        boolean changed = false;
        var description = catalogDTO.getDescription();
        var title = catalogDTO.getTitle();
        if (title != null && (catalog.getTitle() == null
                || !catalog.getTitle().equals(title))) {
            catalog.setTitle(title);
            changed = true;
        }
        if (description != null && (catalog.getDescription() == null
                || !catalog.getDescription().equals(description))) {
            catalog.setDescription(description);
            if(!changed) {
                changed = true;
            }
        }
        return changed;
    }
}
