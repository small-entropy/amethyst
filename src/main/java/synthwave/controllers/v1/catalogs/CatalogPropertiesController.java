package synthwave.controllers.v1.catalogs;

import dev.morphia.Datastore;
import core.constants.DefaultRights;
import core.response.transformers.JsonTransformer;
import synthwave.controllers.abstracts.EmbeddedPropertiesController;
import synthwave.constants.CatalogPropertiesMessages;
import synthwave.filters.CatalogsFilter;
import synthwave.models.morphia.extend.Catalog;
import synthwave.repositories.morphia.CatalogPropertiesRepository;
import synthwave.repositories.morphia.CatalogsRepository;
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
