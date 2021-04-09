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
import dev.morphia.query.FindOptions;
import org.bson.types.ObjectId;
import spark.Request;

import java.util.List;

import static dev.morphia.query.experimental.filters.Filters.and;
import static dev.morphia.query.experimental.filters.Filters.eq;

/**
 * Class controller for work with user properties
 */
public class UserPropertyService {

    /**
     * Method for create user property
     * @param request Spark request object
     * @param datastore Morphia datastore object
     * @return user property
     */
    public static UserProperty createUserProperty(Request request, Datastore datastore) throws TokenException, DataException {
        // Get user id from params
        String idParam = request.params("id");
        // Check equals id from request url & id from send token
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        // Check result compares ids.
        // If ids equals - try create user property.
        // If ids not equals - return null.
        if (isTrusted) {
            // Create ObjectID from string
            ObjectId id = new ObjectId(idParam);
            // Find use by id
            User user = datastore
                    .find(User.class)
                    .filter(and(
                            eq("id", id),
                            eq("status", "active")
                    ))
                    .first();
            // Check founded user.
            // If user not equal null - try create user property and add to document.
            // If user equal null - throw error.
            if (user != null) {
                UserPropertyDTO userPropertyDTO = new Gson().fromJson(request.body(), UserPropertyDTO.class);
                final boolean hasProperty = Comparator.keyProperty_fromUser(userPropertyDTO.getKey(), user);
                // Check result compare key property and key from user properties.
                // If user document has property with this key - throw error.
                // If user document has not property with this - create it.
                if (!hasProperty) {
                    // Create user property
                    UserProperty userProperty = new UserProperty(
                            userPropertyDTO.getKey(),
                            userPropertyDTO.getValue()
                    );
                    user.getProperties().add(userProperty);
                    datastore.save(user);
                    return userProperty;
                } else {
                    return null;
                }
            } else {
                Error error = new Error("User not found. Send not valid id");
                throw new DataException("NotFound", error);
            }
        } else {
            Error error = new Error("Id from request not equal id from token");
            throw new TokenException("NotEquals", error);
        }
    }

    /**
     * Method for get user properties
     * @param request Spark request object
     * @param datastore Morphia datastore object (connection)
     * @return list of user properties
     */
    public static List<UserProperty> getUserProperties(Request request, Datastore datastore) throws TokenException {
        // Get user ID param from request URL
        String idParam = request.params("id");
        // Is trusted request
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        if (isTrusted) {
            // Create ObjectID from
            ObjectId id = (idParam != null) ? new ObjectId(idParam) : null;
            // Create find options
            FindOptions findOptions = new FindOptions()
                    .projection()
                    .include("properties");
            // Find user document
            User user = (id != null)
                    ? datastore
                    .find(User.class)
                    .filter(and(
                            eq("id", id),
                            eq("status", "active")
                    ))
                    .first(findOptions)
                    : null;
            // Get user properties from user document
            return (user != null) ? user.getProperties() : null;
        } else {
            Error error = new Error("Id from request not equal id from token");
            throw new TokenException("NotEquals", error);
        }
    }

    /**
     * Method for get user property by id
     * @param request Spark request object
     * @param datastore Morphia datastore object
     * @return founded user property
     */
    public static UserProperty getUserPropertyById(Request request, Datastore datastore) throws TokenException {
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
