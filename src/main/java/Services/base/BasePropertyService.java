package Services.base;

import DataTransferObjects.v1.UserPropertyDTO;
import Exceptions.DataException;
import Models.Embeddeds.EmbeddedProperty;
import Repositories.Abstract.AbstractPropertyRepository;
import Utils.common.Searcher;
import java.util.List;
import org.bson.types.ObjectId;

/**
 * Abstract core class for work with user properties nested documents
 * @author small-entropy
 */
public abstract class BasePropertyService { 
    
    /**
     * Method for get properties list from user document by request id param
     * @param <R> type of source
     * @param userId user id as string
     * @param repository repository for work with users collection
     * @return user properties list
     * @throws DataException throw exception if user can not be found by request
     *                       params
     */
    protected static 
        <R extends AbstractPropertyRepository> List<EmbeddedProperty> 
        getPropertiesList(
                ObjectId userId,
                R repository
        ) throws DataException {
        return repository.getList(userId);
    }
    
    /**
     * Method for remove user property from list field
     * @param <R> type of source
     * @param propertyId
     * @param userId
     * @param repository data repository
     * @return current list of user properties
     * @throws DataException 
     */
    protected static 
        <R extends AbstractPropertyRepository> List<EmbeddedProperty> 
        removeFromList(
                ObjectId propertyId, 
                ObjectId userId, 
                R repository
        ) throws DataException {
        return repository.removeProperty(propertyId, userId);
    }

    /**
     * Method for get user property by id from properties list
     * @param propertyId property id as string
     * @param properties properties list
     * @return founded property
     */
    protected static EmbeddedProperty getPropertyById(
            ObjectId propertyId,
            List<EmbeddedProperty> properties
    ) {
        return Searcher
                .getUserPropertyByIdFromList(propertyId, properties);
    }

    /**
     * Method for get user property from document field by request params
     * @param <R> type of source
     * @param propertyId property id
     * @param userId user id
     * @param repository repository for work with users collection
     * @return founded user property
     * @throws DataException throw exception of some data not founded
     */
    protected static 
        <R extends AbstractPropertyRepository> EmbeddedProperty 
        getPropertyById(
                ObjectId propertyId, 
                ObjectId userId, 
                R repository
        ) throws DataException {
        return repository.getUserPropertyById(propertyId, userId);
    }

    /**
     * Method for create user property on document field by request body dta
     * @param <R> type of source
     * @param userId user id from request params
     * @param userPropertyDTO user property data transfer object
     * @param repository repository for work with users collection
     * @return created user property
     * @throws DataException throw exception if some data can not be founded
     */
    protected static <R extends AbstractPropertyRepository> EmbeddedProperty 
        createUserProperty(
                ObjectId userId,
                UserPropertyDTO userPropertyDTO,
                R repository
        ) throws DataException {
        return repository.createUserProperty(userId, userPropertyDTO);
    }

    /**
     * Method for update user property by request data
     * @param <R> type of source
     * @param propertyId proprety id from request params
     * @param userId user id from request params
     * @param repository repository for work with users collection
     * @param userPropertyDTO user property data transfer object
     * @return updated user property
     * @throws DataException throw exception if come data can not be founded
     */
    protected static <R extends AbstractPropertyRepository> EmbeddedProperty 
        updateUserProperty(
                ObjectId propertyId,
                ObjectId userId,
                UserPropertyDTO userPropertyDTO,
                R repository
        ) throws DataException {
        return repository.updateUserProperty(
                propertyId, 
                userId, 
                userPropertyDTO
        );
    }
}
