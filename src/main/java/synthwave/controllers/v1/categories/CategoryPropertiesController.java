package synthwave.controllers.v1.categories;

import dev.morphia.Datastore;
import core.constants.DefaultRights;
import core.response.transformers.JsonTransformer;
import synthwave.controllers.abstracts.EmbeddedPropertiesController;
import synthwave.constants.CategoryPropertiesMessages;
import synthwave.filters.CategoriesFilter;
import synthwave.models.morphia.extend.Category;
import synthwave.repositories.morphia.CategoriesRepository;
import synthwave.repositories.morphia.CategoryPropertiesRepository;
import synthwave.services.v1.categories.CategoryPropertiesService;

/**
 * Class for create controller works with category properties.
 * Create instance by datastore & transformer.
 * @author small-entropy
 * @version 1
 */
public class CategoryPropertiesController 
    extends EmbeddedPropertiesController<
        Category,
        CategoriesFilter,
        CategoriesRepository,
        CategoryPropertiesRepository,
        CategoryPropertiesService>{
    
    /**
     * Default contructor for create controller instance by
     * datastore & transformer
     * @param datastore Morphia datastore object
     * @param transformer response transformer
     */
    public CategoryPropertiesController(
        Datastore datastore,
        JsonTransformer transformer
    ) {
        super(
            new CategoryPropertiesService(datastore),
            transformer,
            DefaultRights.CATEGORIES.getName(),
            "/:category_id/properties",
            "/:category_id/properties/:property_id",
            false,
            false
        );
    }

    @Override
	protected String getSuccessMessage(String message) {
        return switch (message) {
            case "created" -> CategoryPropertiesMessages.CREATED.getMessage();
            case "list" -> CategoryPropertiesMessages.LIST.getMessage();
            case "entity" -> CategoryPropertiesMessages.ENTITY.getMessage();
            case "update" -> CategoryPropertiesMessages.UPDATED.getMessage();
            case "delete" -> CategoryPropertiesMessages.DELETED.getMessage();
            default -> "Successfully";
        };
    }
}
