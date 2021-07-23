package synthwave.repositories.mongodb.v1;

import java.util.List;

import org.bson.types.ObjectId;

import dev.morphia.Datastore;
import synthwave.filters.CategoriesFilter;
import synthwave.models.mongodb.standalones.Category;
import synthwave.repositories.mongodb.base.BasePropertyRepository;

/**
 * Class of category profile repository
 * @author small-entropy
 */
public class CategoryProfileRepository 
    extends BasePropertyRepository<Category, CategoriesFilter, CategoriesRepository> {
    
    /**
     * Default constructor for category profile repository
     * @param datastore Morphia datastore object
     * @param blacklist blacklist of fields
     */
    public CategoryProfileRepository(
        Datastore datastore,
        List<String> blacklist
    ) {
        super("profile", blacklist, new CategoriesRepository(datastore));
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
