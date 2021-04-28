package Services.v1;
// Import user model (class)
import DataTransferObjects.RuleDTO;
import DataTransferObjects.UserPropertyDTO;
import Exceptions.AccessException;
import Exceptions.DataException;
import Exceptions.TokenException;
import Models.UserProperty;
import Services.core.CoreUserPropertyService;
import Sources.UsersSource;
import Utils.common.Comparator;
import com.google.gson.Gson;
import java.util.List;
import spark.Request;

/**
 * Class controller for work with user properties
 */
public class UserPropertyService extends CoreUserPropertyService {

    /**
     * Method for remove user property service
     * @param request Spark requet object
     * @param source source for work with users collection
     * @return list of users properties
     */
    public static List<UserProperty> removeUserProperty(Request request, UsersSource source) {
        return null;
    }

    /**
     * Method for create user property
     * @param request Spark request object
     * @param source users data source
     * @param rule rule data transfer object
     * @return user property
     * @throws AccessException user access exception
     * @throws DataException
     */
    public static UserProperty createUserProperty(Request request, UsersSource source, RuleDTO rule) throws AccessException, DataException {
        // Compare token ID in token and in params
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        // If id in token and id in params equals - get rule value for user own private fields.
        // If id in token and id in params not equals - get rule value for user other private fields.
        boolean hasAccess = (isTrusted) ? rule.isMyPrivate() : rule.isOtherPrivate();
        // Check user access on create private fields.
        // If user has access to create private fields - try do it.
        // If user has not access to create private fields - throw exception.
        if (hasAccess) {
            UserProperty property = UserPropertyService.createUserProperty(request, source);
            if (property != null) {
                return property;
            } else {
                Error error = new Error("Can not create user property");
                throw new DataException("CanNotCreate", error);
            }
        } else {
            String message = (isTrusted)
                    ? "Can crate user property for current user"
                    : "Can create user property for user";
            Error error = new Error(message);
            throw new AccessException("CanNotCreate", error);
        }
    }

    /**
     * Method for get user properties
     * @param request Spark request object
     * @param source users data source
     * @param rule rule data transfer object
     * @return list of user properties
     * @throws TokenException
     * @throws DataException
     * @throws AccessException
     */
    public static List<UserProperty> getUserProperties(Request request, UsersSource source, RuleDTO rule) throws TokenException, DataException, AccessException {
        // Compare token ID in token and in params
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        // If id in token and id in params equals - get rule value for user own private fields.
        // If id in token and id in params not equals - get rule value for user other private fields.
        boolean hasAccess = (isTrusted) ? rule.isMyPrivate() : rule.isOtherPrivate();
        // Check user access on create private fields.
        // If user has access to create private fields - try do it.
        // If user has not access to create private fields - throw exception.
        if (hasAccess) {
            return UserPropertyService.getUserProperties(request, source);
        } else {
            String message = (isTrusted)
                    ? "Can not rights for read own private fields"
                    : "Can not rights for read other private fields";
            Error error = new Error(message);
            throw new AccessException("CanNotRead", error);
        }
    }

    /**
     * Method for get user property by id
     * @param request Spark request object
     * @param source users data source
     * @param rule rule data transfer object
     * @return founded user property
     * @throws AccessException
     * @throws DataException
     */
    public static UserProperty getUserPropertyById(Request request, UsersSource source, RuleDTO rule) throws AccessException, DataException {
        // Compare token ID in token and in params
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        // If id in token and id in params equals - get rule value for user own private fields.
        // If id in token and id in params not equals - get rule value for user other private fields.
        boolean hasAccess = (isTrusted) ? rule.isMyPrivate() : rule.isOtherPrivate();
        // Check user access on create private fields.
        // If user has access to create private fields - try do it.
        // If user has not access to create private fields - throw exception.
        if (hasAccess) {
            return UserPropertyService.getUserPropertyById(request, source);
        } else {
            String message = (isTrusted)
                    ? "Can not rights for read own private fields"
                    : "Can not rights for read other private fields";
            Error error = new Error(message);
            throw new AccessException("CanNotRead", error);
        }
    }

    /**
     * Method for update user property
     * @param request Spark request object
     * @param source source for work with users collection
     * @param rule rule data transfer object
     * @return updated user property document
     * @throws AccessException
     * @throws DataException 
     */
    public static UserProperty updateProperty(Request request, UsersSource source, RuleDTO rule) throws AccessException, DataException {
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        boolean hasAccess = (isTrusted) ? rule.isMyPrivate() : rule.isOtherPrivate();
        if (hasAccess) {
            UserPropertyDTO userPropertyDTO = new Gson().fromJson(request.body(), UserPropertyDTO.class);
            return CoreUserPropertyService.updateUserProperty(request, source, userPropertyDTO);
        } else {
            Error error = new Error("Has no access to update user properties");
            throw new AccessException("CanNotUpdate", error);
        }
    }
}
