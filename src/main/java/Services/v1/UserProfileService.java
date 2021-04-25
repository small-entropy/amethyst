package Services.v1;
import DTO.RuleDTO;
import DTO.UserPropertyDTO;
import Exceptions.AccessException;
import Exceptions.DataException;
import Exceptions.TokenException;
import Models.UserProperty;
import Services.core.CoreUserProfileService;
import Utils.common.Comparator;
import Utils.constants.UsersParams;
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
    public static UserProperty createUserProfileProperty(Request request, Datastore datastore) throws DataException, TokenException {
        // Check equals id from request url & id from send token
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        // Check result compares ids.
        // If ids equals - try create user property.
        // If ids not equals - return null.
        if (isTrusted) {
            UserProperty property = UserProfileService.createUserProfilePropertyByRequest(request, datastore);
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
        String idParam = request.params(UsersParams.ID.getName());
        return UserProfileService.getUserProfile(idParam, datastore);
    }

    /**
     * Method for get user profile property by id
     * @param request Spark request object
     * @param datastore Morphia datastore object
     * @return founded user property
     */
    public static UserProperty getUserProfilePropertyById(Request request, Datastore datastore) throws DataException {
        String idParam = request.params(UsersParams.ID.getName());
        String propertyIdParam = request.params(UsersParams.PROPERTY_ID.getName());
        return UserProfileService.getUserProfilePropertyById(idParam, propertyIdParam, datastore);
    }

    /**
     * Method for update user profile property
     * @param request Spark request object
     * @param datastore Morphia datastore object
     * @param rule rule data transfer object
     * @return updated user property
     * @throws AccessException throw exception if request not access for update action
     * @throws DataException throw exception if some data (property or user) not found
     */
    public static UserProperty updateUserProperty(Request request, Datastore datastore, RuleDTO rule) throws AccessException, DataException {
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        boolean hasAccess = (isTrusted) ? rule.isMyPublic() : rule.isOtherPublic();
        if (hasAccess) {
            UserPropertyDTO userPropertyDTO = new Gson().fromJson(request.body(), UserPropertyDTO.class);
            return CoreUserProfileService.updateUserProperty(request, datastore, userPropertyDTO);
        } else {
            Error error = new Error("Has no access to update user property");
            throw new AccessException("CanNotUpdate", error);
        }
    }
}
