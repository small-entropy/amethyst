package synthwave.controllers.v1.categories;

import dev.morphia.Datastore;
import platform.constants.DefaultRights;
import platform.utils.transformers.JsonTransformer;
import synthwave.controllers.base.EmbeddedPropertiesController;
import synthwave.controllers.messages.CategoryProfileMessages;
import synthwave.filters.CategoriesFilter;
import synthwave.models.mongodb.standalones.Category;
import synthwave.repositories.mongodb.v1.CategoriesRepository;
import synthwave.repositories.mongodb.v1.CategoryProfileRepository;
import synthwave.services.v1.categories.CategoryProfileService;

/**
 * Class for create controller works with category profile.
 * Create instance by datastore & transformer.
 * @author small-entropy
 * @version 1
 */
public class CategoryProfileController 
    extends EmbeddedPropertiesController<
        Category,
        CategoriesFilter,
        CategoriesRepository,
        CategoryProfileRepository,
        CategoryProfileService>{
    
    /**
     * Default constructor for create controller instance by
     * datastore & response transfomer
     * @param datastore Morphia datastore
     * @param transformer response transformer
     */
    public CategoryProfileController(
        Datastore datastore,
        JsonTransformer transformer
    ) {
        super(
            new CategoryProfileService(datastore),
            transformer,
            DefaultRights.CATEGORIES.getName(),
            "/:category_id/profile",
            "/:category_id/profile/:property_id",
            false,
            false
        );
    }

    @Override
	protected String getSuccessMessage(String message) {
        return switch (message) {
            case "created" -> CategoryProfileMessages.CREATED.getMessage();
            case "list" -> CategoryProfileMessages.LIST.getMessage();
            case "entity" -> CategoryProfileMessages.ENTITY.getMessage();
            case "update" -> CategoryProfileMessages.UPDATED.getMessage();
            case "delete" -> CategoryProfileMessages.DELETED.getMessage();
            default -> "Successfully";
        };
    }
}
