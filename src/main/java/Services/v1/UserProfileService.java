package Services.v1;
import DataTransferObjects.RuleDTO;
import DataTransferObjects.UserPropertyDTO;
import Exceptions.AccessException;
import Exceptions.DataException;
import Exceptions.TokenException;
import Models.UserProperty;
import Services.core.CoreUserProfileService;
import Sources.UsersSource;
import Utils.common.Comparator;
import Utils.constants.UsersParams;
import com.google.gson.Gson;
import java.util.List;
import spark.Request;

/**
 * Class controller for work with user profile properties
 */
public class UserProfileService extends CoreUserProfileService {

    /**
     * Method for create user profile property
     * @param request Spark request object
     * @param source source for work with users collection
     * @return user property
     * @throws DataException
     * @throws TokenException
     */
    public static UserProperty createUserProfileProperty(Request request, UsersSource source) throws DataException, TokenException {
        // Check equals id from request url & id from send token
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        // Check result compares ids.
        // If ids equals - try create user property.
        // If ids not equals - return null.
        if (isTrusted) {
            UserProperty property = UserProfileService.createUserProfilePropertyByRequest(request, source);
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
     * @param source source for work with users collection
     * @return list of user properties
     * @throws DataException
     */
    public static List<UserProperty> getUserProfile(Request request, UsersSource source) throws DataException {
        // Get user ID param from request URL
        String idParam = request.params(UsersParams.ID.getName());
        return UserProfileService.getUserProfile(idParam, source);
    }

    /**
     * Method for get user profile property by id
     * @param request Spark request object
     * @param source source for work with users collection
     * @return founded user property
     * @throws DataException
     */
    public static UserProperty getUserProfilePropertyById(Request request, UsersSource source) throws DataException {
        String idParam = request.params(UsersParams.ID.getName());
        String propertyIdParam = request.params(UsersParams.PROPERTY_ID.getName());
        return UserProfileService.getUserProfilePropertyById(idParam, propertyIdParam, source);
    }

    /**
     * Method for update user profile property
     * @param request Spark request object
     * @param source source for work with users collection
     * @param rule rule data transfer object
     * @return updated user property
     * @throws AccessException throw exception if request not access for update action
     * @throws DataException throw exception if some data (property or user) not found
     */
    public static UserProperty updateUserProperty(Request request, UsersSource source, RuleDTO rule) throws AccessException, DataException {
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        boolean hasAccess = (isTrusted) ? rule.isMyPublic() : rule.isOtherPublic();
        if (hasAccess) {
            UserPropertyDTO userPropertyDTO = new Gson().fromJson(request.body(), UserPropertyDTO.class);
            return CoreUserProfileService.updateUserProperty(request, source, userPropertyDTO);
        } else {
            Error error = new Error("Has no access to update user property");
            throw new AccessException("CanNotUpdate", error);
        }
    }
}
