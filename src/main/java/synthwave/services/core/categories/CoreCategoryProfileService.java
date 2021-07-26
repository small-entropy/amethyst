package synthwave.services.core.categories;

import java.util.Arrays;
import java.util.List;

import org.bson.types.ObjectId;

import dev.morphia.Datastore;
import spark.Request;

import platform.exceptions.DataException;
import platform.utils.helpers.ParamsManager;

import synthwave.filters.CategoriesFilter;
import synthwave.models.mongodb.embeddeds.EmbeddedProperty;
import synthwave.models.mongodb.standalones.Category;
import synthwave.repositories.mongodb.v1.CategoriesRepository;
import synthwave.repositories.mongodb.v1.CategoryProfileRepository;
import synthwave.services.core.base.CRUDEmbeddedPropertyService;

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
