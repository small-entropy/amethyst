package Services;
import DTO.UserPropertyDTO;
import Exceptions.DataException;
import Exceptions.TokenException;
import Models.User;
import Models.UserProperty;
import Utils.Comparator;
import com.google.gson.Gson;
import dev.morphia.Datastore;
import org.bson.types.ObjectId;
import spark.Request;

import java.util.Arrays;
import java.util.List;

import static dev.morphia.query.experimental.filters.Filters.and;
import static dev.morphia.query.experimental.filters.Filters.eq;

/**
 * Class controller for work with user profile properties
 */
public class UserProfileService {

    /**
     * Method for get default profile properties list
     * @return list of user properties for profile
     */
    public static List<UserProperty> getDefaultProfile() {
        Long currentDateTime = System.currentTimeMillis();
        UserProperty registered = new UserProperty("registered", currentDateTime);
        return Arrays.asList(registered);
    }

    /**
     * Method for create user profile property
     * @param request Spark request object
     * @param datastore Morphia datastore object
     * @return user property
     */
    public static UserProperty createUserProfileProperty(Request request, Datastore datastore) throws TokenException, DataException {
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
                    user.getProfile().add(userProperty);
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
     * Method for get user profile properties
     * @param request Spark request object
     * @param datastore Morphia datastore object (connection)
     * @return list of user properties
     */
    public static List<UserProperty> getUserProfile(Request request, Datastore datastore) throws TokenException {
        // Get user ID param from request URL
        String idParam = request.params("id");
        ObjectId id = new ObjectId(idParam);
        User user = datastore
                .find(User.class)
                .filter(and(
                        eq("id", id),
                        eq("status", "active")
                ))
                .first();
        return (user != null) ? user.getProfile() : null;
    }

    /**
     * Method for get user profile property by id
     * @param request Spark request object
     * @param datastore Morphia datastore object
     * @return founded user property
     */
    public static UserProperty getUserProfilePropertyById(Request request, Datastore datastore) throws TokenException {
        List<UserProperty> profile = UserProfileService.getUserProfile(request, datastore);
        String propertyId = request.params("property_id");
        UserProperty result = null;
        if (profile != null && propertyId != null) {
            for (UserProperty profile_property : profile) {
                if (profile_property.getId().toString().equals(propertyId)) {
                    result = profile_property;
                }
            }
        }
        return result;
    }
}
