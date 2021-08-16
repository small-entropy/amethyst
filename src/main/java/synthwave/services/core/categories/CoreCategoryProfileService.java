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
import synthwave.repositories.morphia.CategoryProfileRepository;
import synthwave.services.abstracts.CRUDEmbeddedPropertyService;

/**
 * Base class for work with category profile property list
 * @author small-entropy
 */
public abstract class CoreCategoryProfileService 
    extends CRUDEmbeddedPropertyService<Category, CategoriesFilter, CategoriesRepository, CategoryProfileRepository> {

    /**
     * Default core category profile service constructor. Create
     * instance by database & blacklist
     * @param datastore Modphia datastore object
     * @param blacklist blaclist of fileds
     */
    public CoreCategoryProfileService(
        Datastore datastore,
        List<String> blacklist
    ) {
        super(datastore, new CategoryProfileRepository(datastore, blacklist));
    }

    @Override
    protected ObjectId getEntityIdFromRequest(Request request) throws DataException {
        return ParamsManager.getCategoryId(request);
    }

    /**
     * Static method for get default category profile
     * @return default category profile
     */
    public static List<EmbeddedProperty> getDefaultProfile() {
        long currendDateAndTime = System.currentTimeMillis();
        EmbeddedProperty created = new EmbeddedProperty(
            "created",
            currendDateAndTime 
        );
        return Arrays.asList(created);
    }
}
