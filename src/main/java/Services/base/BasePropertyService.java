package Services.base;

import DataTransferObjects.v1.UserPropertyDTO;
import Exceptions.DataException;
import Models.Embeddeds.EmbeddedProperty;
import Repositories.Abstract.AbstractPropertyRepository;
import Utils.common.Searcher;
import java.util.List;

/**
 * Abstract core class for work with user properties nested documents
 * @author small-entropy
 */
public abstract class BasePropertyService { 
    
    /**
     * Method for get properties list from user document by request id param
     * @param <R> type of source
     * @param idParam user id as string
     * @param repository repository for work with users collection
     * @return user properties list
     * @throws DataException throw exception if user can not be found by request
     *                       params
     */
    protected static 
        <R extends AbstractPropertyRepository> List<EmbeddedProperty> 
        getPropertiesList(
                String idParam,
                R repository
        ) throws DataException {
        return repository.getList(idParam);
    }
    
    /**
     * Method for remove user property from list field
     * @param <R> type of source
     * @param propertyIdParam
     * @param idParam
     * @param repository data repository
     * @return current list of user properties
     * @throws DataException 
     */
    protected static 
        <R extends AbstractPropertyRepository> List<EmbeddedProperty> 
        removeFromList(
                String propertyIdParam, 
                String idParam, 
                R repository
        ) throws DataException {
        return repository.removeProperty(propertyIdParam, idParam);
    }

    /**
     * Method for get user property by id from properties list
     * @param propertyIdParam property id as string
     * @param properties properties list
     * @return founded property
     */
    protected static EmbeddedProperty getPropertyById(
            String propertyIdParam,
            List<EmbeddedProperty> properties
    ) {
        return Searcher
                .getUserPropertyByIdFromList(propertyIdParam, properties);
    }

    /**
     * Method for get user property from document field by request params
     * @param <R> type of source
     * @param propertyIdParam property id
     * @param idParam user id
     * @param repository repository for work with users collection
     * @return founded user property
     * @throws DataException throw exception of some data not founded
     */
    protected static 
        <R extends AbstractPropertyRepository> EmbeddedProperty 
        getPropertyById(
                String propertyIdParam, 
                String idParam, 
                R repository
        ) throws DataException {
        return repository.getUserPropertyById(propertyIdParam, idParam);
    }

    /**
     * Method for create user property on document field by request body dta
     * @param <R> type of source
     * @param idParam user id from request params
     * @param userPropertyDTO user property data transfer object
     * @param repository repository for work with users collection
     * @return created user property
     * @throws DataException throw exception if some data can not be founded
     */
    protected static <R extends AbstractPropertyRepository> EmbeddedProperty 
        createUserProperty(
                String idParam,
                UserPropertyDTO userPropertyDTO,
                R repository
        ) throws DataException {
        return repository.createUserProperty(idParam, userPropertyDTO);
    }

    /**
     * Method for update user property by request data
     * @param <R> type of source
     * @param propertyIdParam proprety id from request params
     * @param idParam user id from request params
     * @param repository repository for work with users collection
     * @param userPropertyDTO user property data transfer object
     * @return updated user property
     * @throws DataException throw exception if come data can not be founded
     */
    protected static <R extends AbstractPropertyRepository> EmbeddedProperty 
        updateUserProperty(
                String propertyIdParam,
                String idParam,
                UserPropertyDTO userPropertyDTO,
                R repository
        ) throws DataException {
        return repository.updateUserProperty(
                propertyIdParam, 
                idParam, 
                userPropertyDTO
        );
    }
}
