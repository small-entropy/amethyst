package synthwave.services.v1.catalogs;

import java.util.Arrays;
import java.util.List;

import dev.morphia.Datastore;
import platform.dto.RuleDTO;
import platform.exceptions.AccessException;
import platform.exceptions.DataException;
import spark.Request;
import synthwave.models.mongodb.embeddeds.EmbeddedProperty;
import synthwave.services.core.catalogs.CoreCatalogPropertiesService;

/**
 * Class for create catalog properties service
 * @author small-entopy
 * @version 1
 */
public class CatalogPropertiesService extends CoreCatalogPropertiesService {
    
    /**
     * Default constructor for servis works with 
     * catalog properties field. Create by datastore
     * @param datastore Morphia datastore object
     */
    public CatalogPropertiesService(Datastore datastore) {
        super(
            datastore,
            Arrays.asList("count")
        );
    }

    /**
     * Method for create catalog property
     * @param request Spark request object
     * @param right name of right
     * @param action name of action
     * @return created catalog property
     * @throws AccessException throw if user hasn't access 
     *                         to create catalog property
     * @throws DataException throw if can't be found some data
     */
    public EmbeddedProperty createCatalogProperty(
        Request request,
        String right,
        String action
    ) throws AccessException, DataException {
        boolean hasAccess = checkHasAccess(request, right, action);
        if (hasAccess) {
            return createEmbeddedProperty(request);
        } else {
            Error error = new Error("Has no access for create catalog property");
            throw new AccessException("CanNotCreate", error);
        }
    }

    /**
     * Method fot get catalog properties list
     * @param request Spark request object
     * @param right name of right
     * @param action name of action
     * @return catalog properties list
     * @throws DataException throw if can't be found some data 
     * @throws AccessException throw if user hasn't access to 
     *                         read properties field
     */
    public List<EmbeddedProperty> getCatalogProperties(
        Request request,
        String right,
        String action
    ) throws DataException, AccessException {
        boolean hasAccess = checkHasAccess(request, right, action);
        if (hasAccess) {
            return getEmbeddedProperties(request);
        } else {
            Error error = new Error("Has no access to get catalog properties");
            throw new AccessException("CanNotRead", error);
        }
    }

    /**
     * Metod for get catalog property by id
     * @param request Spark request object
     * @param right name of right
     * @param action name of action
     * @return founded catalog property
     * @throws DataException throw if can't find some data
     * @throws AccessException throw if user hasn't access to read
     *                         catalog properties field
     */
    public EmbeddedProperty getCatalogPropertyById(
        Request request,
        String right,
        String action
    ) throws DataException, AccessException {
        boolean hasAccess = checkHasAccess(request, right, action);
        if (hasAccess) {
            return getEmbeddedProperty(request);
        } else {
            Error error = new Error("Has no access to get catalog property");
            throw new AccessException("CanNotRead", error);
        }
    }

    /**
     * Method for update catalog property embedded document
     * @param request Spark request object
     * @param right name of right
     * @param action name of action
     * @return updated catalog property
     * @throws DataException throw if can't be find some data
     * @throws AccessException throw if user hasnt't access to
     *                         update catalog properties field
     */
    public EmbeddedProperty updateCatalogProperty(
        Request request,
        String right,
        String action
    ) throws DataException, AccessException {
        boolean hasAccess = checkHasAccess(request, right, action);
        if (hasAccess) {
            return updateEmbeddedProperty(request);
        } else {
            Error error = new Error("Has no access to update catalog properties");
            throw new AccessException("CanNotUpdate", error);
        }
    }

    /**
     * Method for delete catalog property from 
     * catalog properties list
     * @param request Spark request object
     * @param right name of right
     * @param action name of action
     * @return updated properties list
     * @throws DataException throw if can't find some data
     * @throws AccessException throw if user hasn't access
     *                         to read catalog properties field
     */
    public List<EmbeddedProperty> deleteCatalogProperty(
        Request request,
        String right,
        String action
    ) throws DataException, AccessException {
        boolean hasAccess = checkHasAccess(request, right, action);
        if (hasAccess) {
            return deletEmbeddedProperty(request);
        } else {
            Error error = new Error("Has no access to delete entity");
            throw new AccessException("CanNotDelete", error);
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
