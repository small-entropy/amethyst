package synthwave.repositories.morphia;

import java.util.List;

import org.bson.types.ObjectId;

import dev.morphia.Datastore;
import synthwave.filters.CategoriesFilter;
import synthwave.models.morphia.extend.Category;
import engine.repositories.morphia.BasePropertyRepository;

/**
 * Class for create instance properties
 * @author small-entropy
 */
public class CategoryPropertiesRepository 
    extends BasePropertyRepository<Category, CategoriesFilter, CategoriesRepository> {
    
    /**
     * Default constructor for catalogs properties repository
     * @param datastore Morphia datastore object
     * @param blacklist blacklist of fields
     */
    public CategoryPropertiesRepository(
        Datastore datastore,
        List<String> blacklist
    ) {
        super("properties", blacklist, new CategoriesRepository(datastore));
    }

    @Override
    public Category findOneById(ObjectId entityId, String[] excludes) {
        CategoriesFilter filter = new CategoriesFilter(entityId, excludes);
        return this.getRepository().findOneById(filter);
    }

    @Override
    public void save(Category category) {
        this.getRepository().save(category);
    }
}
