package Services.core;

import DataTransferObjects.UserPropertyDTO;
import Exceptions.DataException;
import Models.UserProperty;
import Services.base.BasePropertyService;
import Sources.PropertiesSource;
import Utils.constants.UsersParams;
import com.google.gson.Gson;
import java.util.Arrays;
import java.util.List;
import spark.Request;

/**
 * Base user properties service
 */
public abstract class CoreUserPropertyService extends BasePropertyService {

    /**
     * Method for get default properties for create user
     * @return list of default user properties
     */
    protected static List<UserProperty> getDefaultUserProperty() {
        UserProperty banned = new UserProperty("banned", false);
        return Arrays.asList(banned);
    }

    /**
     * Method for create user property
     * @param request Spark request object
     * @param source user property datasource
     * @return user property
     * @throws DataException throw if con not be found user or property document
     */
    protected static UserProperty createUserProperty(Request request, PropertiesSource source) throws DataException {
        String idParam = request.params(UsersParams.ID.getName());
        UserPropertyDTO userPropertyDTO = new Gson().fromJson(request.body(), UserPropertyDTO.class);
        return createUserProperty(idParam, userPropertyDTO, source);
    }

    /**
     * Get user properties by request
     * @param request Spark request object
     * @param source user property datasource
     * @return user properties list
     * @throws DataException throw if con not be found user or property document
     */
    protected static List<UserProperty> getUserProperties(Request request, PropertiesSource source) throws DataException {
        String idParam = request.params(UsersParams.ID.getName());
        return getPropertiesList(idParam, source);
    }

    /**
     * Method for get user property by id
     * @param request Spark request object
     * @param source user property datasource
     * @return founded user property
     * @throws DataException throw if con not be found user or property document
     */
    protected static UserProperty getUserPropertyById(Request request, PropertiesSource source) throws DataException {
        String idParam = request.params(UsersParams.ID.getName());
        String propertyIdParam = request.params(UsersParams.PROPERTY_ID.getName());
        return getPropertyById(propertyIdParam, idParam, source);
    }

    /**
     * Method for update user property in user document
     * @param request Spark request object
     * @param source user property datasource
     * @return updated user property
     * @throws DataException throw if con not be found user or property document
     */
    protected static UserProperty updateUserProperty(Request request, PropertiesSource source) throws DataException {
        UserPropertyDTO userPropertyDTO = new Gson().fromJson(request.body(), UserPropertyDTO.class);
        String propertyIdParam = request.params(UsersParams.PROPERTY_ID.getName());
        String idParams = request.params(UsersParams.ID.getName());
        return updateUserProperty(propertyIdParam, idParams, userPropertyDTO, source);
    }
    
    /**
     * Method for remove user property by request params
     * @param request Spark request object
     * @param source user property datasource
     * @return actual list of user properties
     * @throws DataException throw if con not be found user or property document
     */
    protected static List<UserProperty> deleteUserProperty(Request request, PropertiesSource source) throws DataException {
        String idParam = request.params(UsersParams.ID.getName());
        String propertyIdParam = request.params(UsersParams.PROPERTY_ID.getName());
        return source.removeProperty(propertyIdParam, idParam);
    }
}
