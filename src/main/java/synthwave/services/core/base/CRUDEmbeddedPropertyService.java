package synthwave.services.core.base;

import java.util.List;

import dev.morphia.Datastore;
import platform.exceptions.DataException;
import platform.exceptions.TokenException;
import platform.filters.Filter;
import platform.models.mongodb.morphia.Standalone;
import spark.Request;
import synthwave.models.mongodb.embeddeds.EmbeddedProperty;
import synthwave.repositories.mongodb.base.BasePropertyRepository;

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
    public EmbeddedProperty create(Request request) throws DataException {
        return createEmbeddedProperty(request);
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
     * Method for get profile property
     * @param request Spark request object
     * @return profile property
     * @throws DataException throw if can't be found some data
     */
    public EmbeddedProperty entity(Request request) throws DataException {
        return this.getEmbeddedProperty(request);
    }

    /**
     * Method for update embedded property
     * @param request Spark request object
     * @return updated embedded property
     * @throws DataException throw if can't be found some data
     */
    public EmbeddedProperty update(Request request) throws DataException {
        return this.updateEmbeddedProperty(request);   
    }

    /**
     * Method for delete property from list
     * @param request Spark request object
     * @return updated properties list
     * @throws DataException throw if can't be found some data
     */
    public List<EmbeddedProperty> delete(Request request) throws DataException {
        return this.deletEmbeddedProperty(request);
    }
}
