package synthwave.services.v1.categories;

import java.util.Arrays;
import java.util.List;

import spark.Request;

import dev.morphia.Datastore;
import platform.dto.RuleDTO;
import platform.exceptions.AccessException;
import platform.exceptions.DataException;

import synthwave.models.mongodb.embeddeds.EmbeddedProperty;
import synthwave.services.core.categories.CoreCategoryPropertiesService;

/**
 * Class or create service for work with category propeties
 * @author small-entropy
 * @version 1
 */
public class CategoryPropertiesService extends CoreCategoryPropertiesService {

    /**
     * Default constructore for category properties service. Create
     * instnce by datastore
     * @param datastore Morphia datastore object
     */
    public CategoryPropertiesService(Datastore datastore) {
        super(datastore, Arrays.asList("count"));
    }
    
    /**
     * Nethod for create category property
     * @param request Spark request object
     * @param right name of right 
     * @param action name of action
     * @return created category property
     * @throws AccessException throw if hasn't access to create category property
     * @throws DataException thrwo if can't be found some data
     */
    public EmbeddedProperty createCategoryProperty(
        Request request,
        String right,
        String action
    ) throws AccessException, DataException {
        boolean hasAccess = checkHasAccess(request, right, action);
        if (hasAccess) {
            return createEmbeddedProperty(request);
        } else {
            Error error = new Error("Hasn't access to create category property");
            throw new AccessException("CanNotCreate", error);
        }
    }

    /**
     * Method for get list of category properties
     * @param request Spark request object
     * @param right name of right 
     * @param action name of action
     * @return list of category properties
     * @throws AccessException throw if hasn't access to get list of category propeties
     * @throws DataException throw if can't be found some data
     */
    public List<EmbeddedProperty> getCategoryProperties(
        Request request,
        String right,
        String action
    ) throws AccessException, DataException {
        boolean hasAccess = checkHasAccess(request, right, action);
        if (hasAccess) {
            return getEmbeddedProperties(request);
        } else {
            Error error = new Error("Hasn't access to get category properties");
            throw new AccessException("CanNotRead", error);
        }
    }

    /**
     * Method for get category property by id
     * @param request Spark request object
     * @param right name of right 
     * @param action name of action
     * @return category property
     * @throws AccessException throw if hasn't access to get category propety
     * @throws DataException throw if can't be found some data
     */
    public EmbeddedProperty getCategoryPropertyById(
        Request request,
        String right,
        String action
    ) throws AccessException, DataException {
        boolean hasAccess = checkHasAccess(request, right, action);
        if (hasAccess) {
            return getEmbeddedProperty(request);
        } else {
            Error error = new Error("Hasn't access to get category property");
            throw new AccessException("CanNotRead", error);
        }
    }

    /**
     * Nethod for update category proprety by id
     * @param request Spark request object
     * @param right name of right 
     * @param action name of action
     * @return updated category property
     * @throws AccessException throw if hasn't access to update category property
     * @throws DataException throw if can't be found some data
     */
    public EmbeddedProperty updateCategoryProperty(
        Request request,
        String right,
        String action
    ) throws AccessException, DataException {
        boolean hasAccess = checkHasAccess(request, right, action);
        if (hasAccess) {
            return updateEmbeddedProperty(request);
        } else {
            Error error = new Error("Hasn't accesss to update category property");
            throw new AccessException("CanNotUpdate", error);
        }
    }

    /**
     * Method for delete category propety
     * @param request Spark request object
     * @param right name of right 
     * @param action name of action
     * @return updated list of category properties
     * @throws AccessException throw if hasn't access to delete category property
     * @throws DataException throw if can't be found some data
     */
    public List<EmbeddedProperty> deleteCategoryProperty(
        Request request,
        String right,
        String action
    ) throws AccessException, DataException {
        boolean hasAccess = checkHasAccess(request, right, action);
        if (hasAccess) {
            return deletEmbeddedProperty(request);
        } else {
            Error error = new Error("Hasn't accesss for delete category property");
            throw new AccessException("CanNotUpdate", error);
        }
    }

    @Override
    protected boolean checkExistHasAccess(
        RuleDTO rule,
        boolean isTrusted
    ) {
        return (isTrusted) ? rule.isMyGlobal() : rule.isOtherGlobal();
    }
}
