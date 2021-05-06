package Services.v1;
import DataTransferObjects.RuleDTO;
import Exceptions.AccessException;
import Exceptions.DataException;
import Exceptions.TokenException;
import Models.UserProperty;
import Services.core.CoreUserProfileService;
import Sources.ProfileSource;
import Utils.common.Comparator;
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
     * @throws DataException throw if user or property can not found
     * @throws TokenException throw if token not corect or not send
     */
    public static UserProperty createUserProfileProperty(Request request, ProfileSource source) throws DataException, TokenException {
        // Check equals id from request url & id from send token
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        // Check result compares ids.
        // If ids equals - try create user property.
        // If ids not equals - return null.
        if (isTrusted) {
            return createUserProperty(request, source);
        } else {
            Error error = new Error("Id from request not equal id from token");
            throw new TokenException("NotEquals", error);
        }
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
    public static UserProperty updateUserProperty(Request request, ProfileSource source, RuleDTO rule) throws AccessException, DataException {
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        boolean hasAccess = (isTrusted) ? rule.isMyPublic() : rule.isOtherPublic();
        if (hasAccess) {
            return updateUserProperty(request, source);
        } else {
            Error error = new Error("Has no access to update user property");
            throw new AccessException("CanNotUpdate", error);
        }
    }

    /**
     * Method for remove user profile property by requst params with rule checks
     * @param request Spark request object 
     * @param source profile datasource
     * @param rule rule data transfer object
     * @return actual list of profile properties
     * @throws AccessException throw if user hasn't rigth to remove profile proeprty document
     * @throws DataException  throw if user or property can not be found
     */
    public static List<UserProperty> deleteProfileProperty(Request request, ProfileSource source, RuleDTO rule) throws AccessException, DataException {
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        boolean hasAccess = (isTrusted) ? rule.isMyPublic() : rule.isOtherPublic();
        if (hasAccess) {
            return deleteUserProfileProperty(request, source);
        } else {
            Error error = new Error("Has no accesss to delete user property");
            throw new AccessException("CanNotDelete", error);
        }
    }
}
