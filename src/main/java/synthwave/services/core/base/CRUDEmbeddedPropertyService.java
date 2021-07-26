package synthwave.services.core.base;

import java.util.List;

import dev.morphia.Datastore;
import platform.exceptions.AccessException;
import platform.exceptions.DataException;
import platform.exceptions.TokenException;
import platform.filters.Filter;
import platform.models.mongodb.morphia.Standalone;
import spark.Request;
import synthwave.models.mongodb.embeddeds.EmbeddedProperty;
import synthwave.repositories.mongodb.base.BasePropertyRepository;
import synthwave.utils.helpers.Comparator;

/**
 * Abstract class to create CRUD service for embedded field profile or properties
 * @author small-entropy
 */
public abstract class CRUDEmbeddedPropertyService
    <M extends Standalone, F extends Filter, PR, R extends BasePropertyRepository<M, F, PR>> 
    extends BaseEmbeddedPropertiesService<M, F, PR, R> {
    
    /**
     * Default constructor for service
     * @param datastore Morphia datastore oebject
     * @param repository repository, contained data
     */
    public CRUDEmbeddedPropertyService(
        Datastore datastore,
        R repository
    ) {
        super(datastore, repository);
    }

    /**
     * List method for create embedded property in profile list
     * @param request Spark requeset object
     * @return created embedded property
     * @throws TokenException throw if id from request and id in token not equal
     * @throws DataException throw if can't be found some data
     */
    public EmbeddedProperty create(
        Request request
    ) throws TokenException, DataException {
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        if (isTrusted) {
            return createEmbeddedProperty(request);
        } else {
            Error error = new Error("Hasn't access to create profile property");
            throw new TokenException("NotEquals", error);
        }
    }

    /**
     * Method for create embedded property in properties field
     * @param request Spark request object
     * @param right name of right
     * @param action name of action
     * @return created embedded property
     * @throws AccessException throw if hasn't access to create property
     * @throws DataException throw if can't be found some data
     */
    public EmbeddedProperty create(
        Request request,
        String right,
        String action
    ) throws AccessException, DataException {
        boolean hasAccess = checkHasAccess(request, right, action);
        if (hasAccess) {
            return createEmbeddedProperty(request);
        } else {
            Error error = new Error("Hasn't access to create property");
            throw new AccessException("CanNotCreate", error);
        }
    }

    /**
     * Method for return profile
     * @param request Spark request object
     * @return list of profile embedded properties
     * @throws DataException throw if can't be found some data
     */
    public List<EmbeddedProperty> list(Request request) throws DataException {
        return getEmbeddedProperties(request);
    }
    
    /**
     * Method for get properties list
     * @param request Spark request object
     * @param right name of right
     * @param action name of action
     * @return properties list
     * @throws AccessException throw if hasn't access to get properties list
     * @throws DataException throw if can't be found some data
     */
    public List<EmbeddedProperty> list(
        Request request,
        String right,
        String action
    ) throws AccessException, DataException {
        boolean hasAccess = checkHasAccess(request, right, action);
        if (hasAccess) {
            return getEmbeddedProperties(request);
        } else {
            Error error = new Error("Hasn't access to get propertie list");
            throw new AccessException("CanNotRead", error);
        }
    }

    /**
     * Method for get profile property
     * @param request Spark request object
     * @return profile property
     * @throws DataException throw if can't be found some data
     */
    public EmbeddedProperty entity(Request request) throws DataException {
        return this.getEmbeddedProperty(request);
    }

    /**
     * Method for get property
     * @param request Spark reqeust object
     * @param right name of right
     * @param action name of action
     * @return founded property
     * @throws AccessException throw if hasn't access to get property
     * @throws DataException throw if can't be found some data
     */
    public EmbeddedProperty entity(
        Request request,
        String right,
        String action
    ) throws AccessException, DataException {
        boolean hasAccess = checkHasAccess(request, right, action);
        if (hasAccess) {
            return this.getEmbeddedProperty(request);
        } else {
            Error error = new Error("Hasn't access to get property");
            throw new AccessException("CanNotRead", error);
        }
    } 

    /**
     * Method for update embedded property
     * @param request Spark request object
     * @param right name of right
     * @param action name of action
     * @return updated embedded property
     * @throws AccessException throw if hasn't access to udate property
     * @throws DataException throw if can't be found some data
     */
    public EmbeddedProperty update(
        Request request,
        String right,
        String action
    ) throws AccessException, DataException {
        boolean hasAccess = checkHasAccess(request, right, action);
        if (hasAccess) {
            return this.updateEmbeddedProperty(request);
        } else {
            Error error = new Error("Hasn't access to update property");
            throw new AccessException("CanNotUpdate", error);
        }     
    }

    /**
     * Method for delete property from list
     * @param request Spark request object
     * @param right name of right
     * @param action name of action
     * @return updated properties list
     * @throws AccessException throw if hasn't access to delete property from list
     * @throws DataException throw if can't be found some data
     */
    public List<EmbeddedProperty> delete(
        Request request,
        String right,
        String action
    ) throws AccessException, DataException {
        boolean hasAccess = checkHasAccess(request, right, action);
        if (hasAccess) {
            return this.deletEmbeddedProperty(request);
        } else {
            Error error = new Error("Hasn't access for delete property from list");
            throw new AccessException("CanNotDelete", error);
        }
    }
}
