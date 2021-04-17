package Services;
// Import user model (class)
import DTO.RuleDTO;
import DTO.UserPropertyDTO;
import Exceptions.AccessException;
import Exceptions.DataException;
import Exceptions.TokenException;
import Models.User;
// Import user property model (class)
import Models.UserProperty;
import Utils.Comparator;
import com.google.gson.Gson;
import dev.morphia.Datastore;
import spark.Request;

import java.util.Arrays;
import java.util.List;

/**
 * Class controller for work with user properties
 */
public class UserPropertyService {

    public static List<UserProperty> getDefaultUserProperty() {
        UserProperty banned = new UserProperty("banned", false);
        return Arrays.asList(banned);
    }

    public static List<UserProperty> removeUserProperty(Request request, Datastore datastore) {
        return null;
    }

    /**
     * Method for create user property by request bode object in user document
     * @param request Spark request object
     * @param datastore Morphia datastore object
     * @param user user document
     * @return created user property document
     */
    private static UserProperty createUserPropertyFromRequest(Request request, Datastore datastore, User user) {
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
     * @param rule rule data transfer object
     * @return user property
     */
    public static UserProperty createUserProperty(Request request, Datastore datastore, RuleDTO rule) throws TokenException, DataException, AccessException {
        // Compare token ID in token and in params
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        // If id in token and id in params equals - get rule value for user own private fields.
        // If id in token and id in params not equals - get rule value for user other private fields.
        boolean hasAccess = (isTrusted) ? rule.isMyPrivate() : rule.isOtherPrivate();
        // Check user access on create private fields.
        // If user has access to create private fields - try do it.
        // If user has not access to create private fields - throw exception.
        if (hasAccess) {
            // Get user by id in request params
            User user = UserService.getUserById(request.params("id"), datastore);
            // Return result of method for create user property from request
            return UserPropertyService.createUserPropertyFromRequest(request, datastore, user);
        } else {
            String message = (isTrusted)
                    ? "Can crate user property for current user"
                    : "Can create user property for user";
            Error error = new Error(message);
            throw new AccessException("CanCreate", error);
        }
    }

    /**
     * Method for get user properties
     * @param request Spark request object
     * @param datastore Morphia datastore object (connection)
     * @param rule rule data transfer object
     * @return list of user properties
     */
    public static List<UserProperty> getUserProperties(Request request, Datastore datastore, RuleDTO rule) throws TokenException, DataException, AccessException {
        // Compare token ID in token and in params
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        // If id in token and id in params equals - get rule value for user own private fields.
        // If id in token and id in params not equals - get rule value for user other private fields.
        boolean hasAccess = (isTrusted) ? rule.isMyPrivate() : rule.isOtherPrivate();
        // Check user access on create private fields.
        // If user has access to create private fields - try do it.
        // If user has not access to create private fields - throw exception.
        if (hasAccess) {
            // Get user by id in request params
            User user = UserService.getUserById(request.params("id"), datastore);
            // Return value od properties field
            return (user != null) ? user.getProperties() : null;
        } else {
            String message = (isTrusted)
                    ? "Can not rights for read own private fields"
                    : "Can not rights for read other private fields";
            Error error = new Error(message);
            throw new AccessException("CanRead", error);
        }
    }

    /**
     * Method for get user property by id
     * @param request Spark request object
     * @param datastore Morphia datastore object
     * @param rule rule data transfer object
     * @return founded user property
     */
    public static UserProperty getUserPropertyById(Request request, Datastore datastore, RuleDTO rule) throws TokenException, DataException, AccessException {
        // Get user properties list
        List<UserProperty> properties = UserPropertyService.getUserProperties(request, datastore, rule);
        // Get user property id from request params
        String propertyId = request.params("property_id");
        // Init empty result
        UserProperty result = null;
        // If user properties list not empty & user property id not null -
        // find property in list by id, else - not do anything.
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
