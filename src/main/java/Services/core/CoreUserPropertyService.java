package Services.core;

import DataTransferObjects.UserPropertyDTO;
import Exceptions.DataException;
import Models.UserProperty;
import Services.base.BasePropertyService;
import Sources.UsersSource;
import Utils.constants.UsersParams;
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
     * @param datastore Morphia datastore object
     * @return user property
     */
    protected static UserProperty createUserProperty(Request request, UsersSource source) throws DataException {
        return CoreUserPropertyService.createUserProperty("properties", request, source);
    }

    /**
     * Get user properties by request
     * @param request Spark request object
     * @param datastore Morphia datastore
     * @return user properties list
     */
    protected static List<UserProperty> getUserProperties(Request request, UsersSource source) throws DataException {
        String idParam = request.params(UsersParams.ID.getName());
        return CoreUserPropertyService.getPropertiesList("properties", idParam, source);
    }

    /**
     * Method for get user property by id
     * @param request Spark request object
     * @param datastore Morphia datastore object
     * @return founded user property
     */
    protected static UserProperty getUserPropertyById(Request request, UsersSource source) throws DataException {
        String idParam = request.params(UsersParams.ID.getName());
        String propertyIdParam = request.params(UsersParams.PROPERTY_ID.getName());
        return CoreUserPropertyService.getPropertyById("properties", propertyIdParam, idParam, source);
    }

    /**
     * Method for update user property in user document
     * @param request Spark request object
     * @param datastore Morphia datastore object
     * @param userPropertyDTO user property data transfer object
     * @return updated user property
     */
    protected static UserProperty updateUserProperty(Request request, UsersSource source, UserPropertyDTO userPropertyDTO) throws DataException {
        return CoreUserPropertyService.updateUserProperty(request, source, userPropertyDTO, "properties");
    }
}
