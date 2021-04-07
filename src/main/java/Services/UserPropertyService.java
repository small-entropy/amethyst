package Services;
// Import user model (class)
import Models.User;
// Import user property model (class)
import Models.UserProperty;
import Utils.JsonWebToken;
import Utils.RequestUtils;
import com.auth0.jwt.interfaces.DecodedJWT;
import dev.morphia.Datastore;
import dev.morphia.query.FindOptions;
import org.bson.types.ObjectId;
import spark.Request;

import java.util.List;
import java.util.stream.Collectors;

import static dev.morphia.query.experimental.filters.Filters.and;
import static dev.morphia.query.experimental.filters.Filters.eq;

/**
 * Class controller for work with user properties
 */
public class UserPropertyService {

    /**
     * Method for get user properties
     * @param request Spark request object
     * @param datastore Morphia datastore object (connection)
     * @return list of user properties
     */
    public static List<UserProperty> getUserProperties(Request request, Datastore datastore) {
        // Get token from request
        String token = RequestUtils.getTokenByRequest(request);
        // Get user ID param from request URL
        String idParam = request.params("id");
        // Is trusted request
        boolean isTrusted;
        if (token != null) {
            DecodedJWT decoded = JsonWebToken.decode(token);
            String decodedId = decoded.getClaim("id").asString();
            isTrusted = idParam.equals(decodedId);
        } else {
            isTrusted = false;
        }

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
                            eq("active", true)
                    ))
                    .first(findOptions)
                : null;
        // Get user properties from user document
        List<UserProperty> properties = (user != null) ? user.getProperties() : null;
        // Return answer by isTrust result
        if (properties != null) {
            return (isTrusted)
                    ? properties
                    : properties
                        .stream()
                        .filter(property -> !property.isNotPublic())
                        .collect(Collectors.toList());
        } else {
            return null;
        }
    }

    /**
     * Method for get user property by id
     * @param request Spark request object
     * @param datastore Morphia datastore object
     * @return founded user property
     */
    public static UserProperty getUserPropertyById(Request request, Datastore datastore) {
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
