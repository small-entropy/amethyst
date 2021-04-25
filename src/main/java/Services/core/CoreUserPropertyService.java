package Services.core;

import DTO.UserPropertyDTO;
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
public abstract class CoreUserPropertyService {

    /**
     * Method for get default properties for create user
     * @return list of default user properties
     */
    protected static List<UserProperty> getDefaultUserProperty() {
        UserProperty banned = new UserProperty("banned", false);
        return Arrays.asList(banned);
    }

    /**
     * Method for create user property by request bode object in user document
     * @param request Spark request object
     * @param datastore Morphia datastore object
     * @param user user document
     * @return created user property document
     */
    protected static UserProperty createUserPropertyFromRequest(Request request, Datastore datastore, User user) {
        // Create data transfer object for user property from request body
        UserPropertyDTO userPropertyDTO = new Gson().fromJson(request.body(), UserPropertyDTO.class);
        // Check property on exist
        final boolean hasProperty = Comparator.keyProperty_fromUser(userPropertyDTO.getKey(), user);
        // If property not exist - add property to user
        // If property exist - return null.
        if (!hasProperty) {
            // Create user property by data transfer object
            UserProperty userProperty = new UserProperty(
                    userPropertyDTO.getKey(),
                    userPropertyDTO.getValue()
            );
            // Add property to user document
            user.getProperties().add(userProperty);
            // Save changes in database
            datastore.save(user);
            // Return created user property
            return userProperty;
        } else {
            return null;
        }
    }

    /**
     * Method for create user property
     * @param request Spark request object
     * @param datastore Morphia datastore object
     * @return user property
     */
    protected static UserProperty createUserProperty(Request request, Datastore datastore) {
        // Get user
        User user = CoreUserService.getUserById(request.params(UsersParams.ID.getName()), datastore);
        // Return result of method for create user property from request
        return CoreUserPropertyService.createUserPropertyFromRequest(request, datastore, user);
    }

    /**
     * Get user properties by request
     * @param request Spark request object
     * @param datastore Morphia datastore
     * @return user properties list
     */
    protected static List<UserProperty> getUserProperties(Request request, Datastore datastore) {
        // Get user by id in request params
        User user = CoreUserService.getUserById(request.params(UsersParams.ID.getName()), datastore);
        // Return value od properties field
        return (user != null) ? user.getProperties() : null;
    }

    /**
     * Method for get user property by id
     * @param request Spark request object
     * @param datastore Morphia datastore object
     * @return founded user property
     */
    protected static UserProperty getUserPropertyById(Request request, Datastore datastore) {
        // Get property ID from request params
        String propertyId = request.params(UsersParams.PROPERTY_ID.getName());
        // Get user properties from user document
        List<UserProperty> properties = CoreUserPropertyService.getUserProperties(request, datastore);
        // Init empty result
        UserProperty result = null;
        // Find user property if exist data
        if (properties != null && propertyId != null) {
            for (UserProperty property : properties) {
                if (property.getId().toString().equals(propertyId)) {
                    result = property;
                    break;
                }
            }
        }
        // Return result
        return result;
    }
}
