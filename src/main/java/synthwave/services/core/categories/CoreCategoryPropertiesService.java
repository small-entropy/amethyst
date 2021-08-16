package synthwave.services.core.categories;

import java.util.Arrays;
import java.util.List;

import org.bson.types.ObjectId;

import dev.morphia.Datastore;
import spark.Request;

import core.exceptions.DataException;
import core.utils.ParamsManager;

import synthwave.filters.CategoriesFilter;
import core.models.morphia.embeddeds.EmbeddedProperty;
import synthwave.models.morphia.extend.Category;
import synthwave.repositories.morphia.CategoriesRepository;
import synthwave.repositories.morphia.CategoryPropertiesRepository;
import synthwave.services.abstracts.CRUDEmbeddedPropertyService;

/**
 * Class with core methods for work with category properties field
 * @author small-entropy 
 */
public abstract class CoreCategoryPropertiesService 
    extends CRUDEmbeddedPropertyService<Category, CategoriesFilter, CategoriesRepository, CategoryPropertiesRepository> {
    
    /**
     * Default constructor for create category properties service
     * @param datastore Morphia datastore object
     * @param blacklist blacklist of fields
     */
    public CoreCategoryPropertiesService(
        Datastore datastore,
        List<String> blacklist
    ) {
        super(datastore, new CategoryPropertiesRepository(datastore, blacklist));
    }

    @Override 
    protected ObjectId getEntityIdFromRequest(Request request) throws DataException {
        return ParamsManager.getCategoryId(request);
    }

    /**
     * Method for get default list of category properties
     * @return list of default category properties
     */
    public static List<EmbeddedProperty> getDefaultProperties() {
        int defaultCount = 0;
        EmbeddedProperty count = new EmbeddedProperty("count", defaultCount);
        return Arrays.asList(count);
    }
}
