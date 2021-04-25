package Services.core;

import DTO.UserPropertyDTO;
import Exceptions.DataException;
import Models.User;
import Models.UserProperty;
import Utils.common.Comparator;
import Utils.constants.UsersParams;
import com.google.gson.Gson;
import dev.morphia.Datastore;
import spark.Request;

import java.util.Arrays;
import java.util.List;

/**
 * Base user properties service
 */
public abstract class CoreUserPropertyService extends CorePropertyService {

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
    protected static UserProperty createUserProperty(Request request, Datastore datastore) throws DataException {
        return CoreUserPropertyService.createUserProperty("properties", request, datastore);
    }

    /**
     * Get user properties by request
     * @param request Spark request object
     * @param datastore Morphia datastore
     * @return user properties list
     */
    protected static List<UserProperty> getUserProperties(Request request, Datastore datastore) throws DataException {
        String idParam = request.params(UsersParams.ID.getName());
        return CoreUserPropertyService.getPropertiesList("properties", idParam, datastore);
    }

    /**
     * Method for get user property by id
     * @param request Spark request object
     * @param datastore Morphia datastore object
     * @return founded user property
     */
    protected static UserProperty getUserPropertyById(Request request, Datastore datastore) throws DataException {
        String idParam = request.params(UsersParams.ID.getName());
        String propertyIdParam = request.params(UsersParams.PROPERTY_ID.getName());
        return CoreUserPropertyService.getPropertyById("properties", propertyIdParam, idParam, datastore);
    }

    /**
     * Method for update user property in user document
     * @param request Spark request object
     * @param datastore Morphia datastore object
     * @param userPropertyDTO user property data transfer object
     * @return updated user property
     */
    protected static UserProperty updateUserProperty(Request request, Datastore datastore, UserPropertyDTO userPropertyDTO) throws DataException {
        return CoreUserPropertyService.updateUserProperty(request, datastore, userPropertyDTO, "properties");
    }
}
