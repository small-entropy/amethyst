package Services.v1;
import DTO.UserPropertyDTO;
import Exceptions.DataException;
import Exceptions.TokenException;
import Models.UserProperty;
import Services.core.CoreUserProfileService;
import Utils.common.Comparator;
import com.google.gson.Gson;
import dev.morphia.Datastore;
import org.bson.types.ObjectId;
import spark.Request;

import java.util.List;

/**
 * Class controller for work with user profile properties
 */
public class UserProfileService extends CoreUserProfileService {

    /**
     * Method for create user profile property
     * @param request Spark request object
     * @param datastore Morphia datastore object
     * @return user property
     */
    public static UserProperty createUserProfileProperty(Request request, Datastore datastore) throws TokenException, DataException {
        // Check equals id from request url & id from send token
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        // Check result compares ids.
        // If ids equals - try create user property.
        // If ids not equals - return null.
        if (isTrusted) {
            ObjectId id = new ObjectId(request.params("id"));
            UserPropertyDTO userPropertyDTO = new Gson().fromJson(request.body(), UserPropertyDTO.class);
            UserProperty property = UserProfileService.createUserProfileProperty(id, userPropertyDTO, datastore);
            if (property != null) {
                return property;
            } else {
                Error error = new Error("Can not create user profile property with this key");
                throw new DataException("CanNotCreate", error);
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
    public static List<UserProperty> getUserProfile(Request request, Datastore datastore) throws DataException {
        // Get user ID param from request URL
        String idParam = request.params("id");
        return UserProfileService.getUserProfile(idParam, datastore);
    }

    /**
     * Method for get user profile property by id
     * @param request Spark request object
     * @param datastore Morphia datastore object
     * @return founded user property
     */
    public static UserProperty getUserProfilePropertyById(Request request, Datastore datastore) throws DataException {
        String idParam = request.params("id");
        String propertyIdParam = request.params("property_id");
        return UserProfileService.getUserProfilePropertyById(idParam, propertyIdParam, datastore);
    }
}
