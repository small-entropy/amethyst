package Services;
// Import user model (class)
import DTO.UserPropertyDTO;
import Exceptions.DataException;
import Exceptions.TokenException;
import Models.User;
// Import user property model (class)
import Models.UserProperty;
import Utils.Comparator;
import com.google.gson.Gson;
import dev.morphia.Datastore;
import spark.Request;
import java.util.List;

/**
 * Class controller for work with user properties
 */
public class UserPropertyService {

    public static List<UserProperty> removeUserProperty(Request request, Datastore datastore) {
        return null;
    }

    /**
     * Method for create user property
     * @param request Spark request object
     * @param datastore Morphia datastore object
     * @return user property
     */
    public static UserProperty createUserProperty(Request request, Datastore datastore) throws TokenException, DataException {
        // Get user with token
        User user = UserService.getUserWithTrust(request, datastore);
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
     * Method for get user properties
     * @param request Spark request object
     * @param datastore Morphia datastore object (connection)
     * @return list of user properties
     */
    public static List<UserProperty> getUserProperties(Request request, Datastore datastore) throws TokenException, DataException {
        // Get user with token
        User user = UserService.getUserWithTrust(request, datastore);
        return (user.getProperties() != null) ? user.getProperties() : null;
    }

    /**
     * Method for get user property by id
     * @param request Spark request object
     * @param datastore Morphia datastore object
     * @return founded user property
     */
    public static UserProperty getUserPropertyById(Request request, Datastore datastore) throws TokenException, DataException {
        List<UserProperty> properties = UserPropertyService.getUserProperties(request, datastore);
        String propertyId = request.params("property_id");
        UserProperty result = null;
        if (properties != null && propertyId != null) {
            for (UserProperty property : properties) {
                if (property.getId().toString().equals(propertyId)) {
                    result = property;
                }
            }
        }
        return result;
    }
}
