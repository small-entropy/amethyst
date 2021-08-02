package synthwave.controllers.v1.catalogs;

import dev.morphia.Datastore;
import platform.constants.DefaultRights;
import platform.utils.transformers.JsonTransformer;
import synthwave.controllers.base.EmbeddedPropertiesController;
import synthwave.controllers.messages.CatalogPropertiesMessages;
import synthwave.filters.CatalogsFilter;
import synthwave.models.mongodb.standalones.Catalog;
import synthwave.repositories.mongodb.v1.CatalogPropertiesRepository;
import synthwave.repositories.mongodb.v1.CatalogsRepository;
import synthwave.services.v1.catalogs.CatalogPropertiesService;

/**
 * Class for create controller works with catalog properties
 * @author small-entropy
 * @version 1
 */
public class CatalogPropertiesController 
    extends EmbeddedPropertiesController<
        Catalog,
        CatalogsFilter,
        CatalogsRepository,
        CatalogPropertiesRepository,
        CatalogPropertiesService> {
 
    /**
     * Default constructor for create catalog properties controller.
     * Create instance by datastore & response transformer
     * @param datastore Morphia datastore object
     * @param transformer response transformer
     */
    public CatalogPropertiesController(
        Datastore datastore,
        JsonTransformer transformer
    ) {
        super(
            new CatalogPropertiesService(datastore),
            transformer,
            DefaultRights.CATALOGS.getName(),
            "/:catalog_id/properties",
            "/:catalog_id/properties/:property_id"
        );
    }

    @Override
    protected String getSuccessMessage(String message) {
        return switch (message) {
            case "created" -> CatalogPropertiesMessages.CREATED.getMessage();
            case "list" -> CatalogPropertiesMessages.LIST.getMessage();
            case "entity" -> CatalogPropertiesMessages.ENTITY.getMessage();
            case "update" -> CatalogPropertiesMessages.UPDATED.getMessage();
            case "delete" -> CatalogPropertiesMessages.DELETED.getMessage();
            default -> "Successfully";
        };
    }
}
