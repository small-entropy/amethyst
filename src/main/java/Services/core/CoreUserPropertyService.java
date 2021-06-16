package Services.core;

import DataTransferObjects.v1.UserPropertyDTO;
import Exceptions.DataException;
import Models.Embeddeds.EmbeddedProperty;
import Services.base.BasePropertyService;
import Repositories.v1.PropertiesRepository;
import Utils.common.ParamsManager;
import java.util.Arrays;
import java.util.List;
import org.bson.types.ObjectId;
import spark.Request;

/**
 * Base user properties service
 */
public abstract class CoreUserPropertyService extends BasePropertyService {

    /**
     * Method for get default properties for create user
     * @return list of default user properties
     */
    protected static List<EmbeddedProperty> getDefaultUserProperty() {
        EmbeddedProperty banned = new EmbeddedProperty("banned", false);
        return Arrays.asList(banned);
    }

    /**
     * Method for create user property
     * @param request Spark request object
     * @param propertiesRepository user property datasource
     * @return user property
     * @throws DataException throw if con not be found user or property document
     */
    protected static EmbeddedProperty createUserProperty(
            Request request, 
            PropertiesRepository propertiesRepository
    ) throws DataException {
        ObjectId userId = ParamsManager.getUserId(request);
        UserPropertyDTO userPropertyDTO =  UserPropertyDTO.build(
                request, 
                UserPropertyDTO.class
        );
        return createUserProperty(
                userId, 
                userPropertyDTO, 
                propertiesRepository
        );
    }

    /**
     * Get user properties by request
     * @param request Spark request object
     * @param propertiesRepository user property datasource
     * @return user properties list
     * @throws DataException throw if con not be found user or property document
     */
    protected static List<EmbeddedProperty> getUserProperties(
            Request request, 
            PropertiesRepository propertiesRepository
    ) throws DataException {
        ObjectId userId = ParamsManager.getUserId(request);
        return getPropertiesList(userId, propertiesRepository);
    }

    /**
     * Method for get user property by id
     * @param request Spark request object
     * @param propertiesRepository user property datasource
     * @return founded user property
     * @throws DataException throw if con not be found user or property document
     */
    protected static EmbeddedProperty getUserPropertyById(
            Request request, 
            PropertiesRepository propertiesRepository
    ) throws DataException {
        ObjectId userId = ParamsManager.getUserId(request);
        ObjectId propertyId = ParamsManager.getPropertyId(request);
        return getPropertyById(propertyId, userId, propertiesRepository);
    }

    /**
     * Method for update user property in user document
     * @param request Spark request object
     * @param propertiesRepository user property datasource
     * @return updated user property
     * @throws DataException throw if con not be found user or property document
     */
    protected static EmbeddedProperty updateUserProperty(
            Request request, 
            PropertiesRepository propertiesRepository
    ) throws DataException {
        UserPropertyDTO userPropertyDTO = UserPropertyDTO.build(
                request, 
                UserPropertyDTO.class
        );
        ObjectId propertyId = ParamsManager.getPropertyId(request);
        ObjectId userId = ParamsManager.getUserId(request);
        return updateUserProperty(
                propertyId, 
                userId, 
                userPropertyDTO, 
                propertiesRepository
        );
    }
    
    /**
     * Method for remove user property by request params
     * @param request Spark request object
     * @param propertiesRepository user property datasource
     * @return actual list of user properties
     * @throws DataException throw if con not be found user or property document
     */
    protected static List<EmbeddedProperty> deleteUserProperty(
            Request request, 
            PropertiesRepository propertiesRepository
    ) throws DataException {
        ObjectId userId = ParamsManager.getUserId(request);
        ObjectId propertyId = ParamsManager.getPropertyId(request);
        return propertiesRepository.removeProperty(propertyId, userId);
    }
}
