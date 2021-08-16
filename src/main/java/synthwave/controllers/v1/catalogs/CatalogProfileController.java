package synthwave.controllers.v1.catalogs;

import dev.morphia.Datastore;
import core.constants.DefaultRights;
import core.response.transformers.JsonTransformer;
import synthwave.controllers.abstracts.EmbeddedPropertiesController;
import synthwave.constants.CatalogProfileMessages;
import synthwave.filters.CatalogsFilter;
import synthwave.models.morphia.extend.Catalog;
import synthwave.repositories.morphia.CatalogProfileRepository;
import synthwave.repositories.morphia.CatalogsRepository;
import synthwave.services.v1.catalogs.CatalogProfileService;

/**
 * Class for create controller works with catalog profile.
 * @author small-entropy
 * @version 1
 */
public class CatalogProfileController 
    extends EmbeddedPropertiesController<
        Catalog,
        CatalogsFilter,
        CatalogsRepository,
        CatalogProfileRepository,
        CatalogProfileService>{
    
    /**
     * Default constructor for create controllser instance by
     * datastore & response transformer
     * @param datastore Morphia datastore object
     * @param transformer response transformer
     */
    public CatalogProfileController(
        Datastore datastore,
        JsonTransformer transformer
    ) {
        super(
            new CatalogProfileService(datastore),
            transformer,
            DefaultRights.CATALOGS.getName(),
            "/:catalog_id/profile",
            "/:catalog_id/profile/:property_id",
            false,
            false
        );
    }

    @Override
    protected String getSuccessMessage(String message) {
        return switch (message) {
            case "created" -> CatalogProfileMessages.CREATED.getMessage();
            case "list" -> CatalogProfileMessages.LIST.getMessage();
            case "entity" -> CatalogProfileMessages.ENTITY.getMessage();
            case "update" -> CatalogProfileMessages.UPDATED.getMessage();
            case "delete" -> CatalogProfileMessages.DELETED.getMessage();
            default -> "Unknown success action";
        };
    }
}
