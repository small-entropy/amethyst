package Services.base;

import DataTransferObjects.UserPropertyDTO;
import Exceptions.DataException;
import Models.Embeddeds.EmbeddedProperty;
import Repositories.Abstract.AbstractPropertyRepository;
import Utils.common.Searcher;
import java.util.List;

/**
 * Abstract core class for work with user properties nested documents
 * @author entropy
 */
public abstract class BasePropertyService { 
    
    /**
     * Method for get properties list from user document by request id param
     * @param <S> type of source
     * @param idParam user id as string
     * @param source source for work with users collection
     * @return user properties list
     * @throws DataException throw exception if user can not be found by request params
     */
    protected static <S extends AbstractPropertyRepository> List<EmbeddedProperty> getPropertiesList(String idParam, S source) throws DataException {
        return source.getList(idParam);
    }
    
    /**
     * Method for remove user property from list field
     * @param <S> type of source
     * @param source data source
     * @return current list of user properties
     * @throws DataException 
     */
    protected static <S extends AbstractPropertyRepository> List<EmbeddedProperty> removeFromList(String propertyIdParam, String idParam, S source) throws DataException {
        return source.removeProperty(propertyIdParam, idParam);
    }

    /**
     * Method for get user property by id from properties list
     * @param propertyIdParam property id as string
     * @param properties properties list
     * @return founded property
     */
    protected static EmbeddedProperty getPropertyById(String propertyIdParam, List<EmbeddedProperty> properties) {
        return Searcher.getUserPropertyByIdFromList(propertyIdParam, properties);
    }

    /**
     * Method for get user property from document field by request params
     * @param <S> type of source
     * @param propertyIdParam property id
     * @param idParam user id
     * @param source source for work with users collection
     * @return founded user property
     * @throws DataException throw exception of some data not founded
     */
    protected static <S extends AbstractPropertyRepository> EmbeddedProperty getPropertyById(String propertyIdParam, String idParam, S source) throws DataException {
        return source.getUserPropertyById(propertyIdParam, idParam);
    }

    /**
     * Method for create user property on document field by request body dta
     * @param <S> type of source
     * @param idParam user id from request params
     * @param userPropertyDTO user property data transfer object
     * @param source source for work with users collection
     * @return created user property
     * @throws DataException throw exception if some data can not be founded
     */
    protected static <S extends AbstractPropertyRepository> EmbeddedProperty createUserProperty(String idParam, UserPropertyDTO userPropertyDTO, S source) throws DataException {
        return source.createUserProperty(idParam, userPropertyDTO);
    }

    /**
     * Method for update user property by request data
     * @param <S> type of source
     * @param propertyIdParam proprety id from request params
     * @param idParam user id from request params
     * @param source source for work with users collection
     * @param userPropertyDTO user property data transfer object
     * @return updated user property
     * @throws DataException throw exception if come data can not be founded
     */
    protected static <S extends AbstractPropertyRepository> EmbeddedProperty updateUserProperty(String propertyIdParam, String idParam, UserPropertyDTO userPropertyDTO, S source) throws DataException {
        return source.updateUserProperty(propertyIdParam, idParam, userPropertyDTO);
    }
}
