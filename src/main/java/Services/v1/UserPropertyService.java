package Services.v1;
// Import user model (class)
import DataTransferObjects.RuleDTO;
import Exceptions.AccessException;
import Exceptions.DataException;
import Exceptions.TokenException;
import Models.Embeddeds.UserProperty;
import Services.core.CoreUserPropertyService;
import Sources.PropertiesSource;
import Utils.common.Comparator;
import java.util.List;
import spark.Request;

/**
 * Class controller for work with user properties
 */
public class UserPropertyService extends CoreUserPropertyService {

    /**
     * Method for create user property
     * @param request Spark request object
     * @param source users data source
     * @param rule rule data transfer object
     * @return user property
     * @throws AccessException user access exception
     * @throws DataException throw if some data can not found or can not create user property
     */
    public static UserProperty createUserProperty(Request request, PropertiesSource source, RuleDTO rule) throws AccessException, DataException {
        // Compare token ID in token and in params
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        // If id in token and id in params equals - get rule value for user own private fields.
        // If id in token and id in params not equals - get rule value for user other private fields.
        boolean hasAccess = (isTrusted) ? rule.isMyPrivate() : rule.isOtherPrivate();
        // Check user access on create private fields.
        // If user has access to create private fields - try do it.
        // If user has not access to create private fields - throw exception.
        if (hasAccess) {
            return createUserProperty(request, source);
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
     * @throws TokenException throw if token not valid or not send
     * @throws DataException throw if some data can not found
     * @throws AccessException throw if user han't access to field
     */
    public static List<UserProperty> getUserProperties(Request request, PropertiesSource source, RuleDTO rule) throws TokenException, DataException, AccessException {
        // Compare token ID in token and in params
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        // If id in token and id in params equals - get rule value for user own private fields.
        // If id in token and id in params not equals - get rule value for user other private fields.
        boolean hasAccess = (isTrusted) ? rule.isMyPrivate() : rule.isOtherPrivate();
        // Check user access on create private fields.
        // If user has access to create private fields - try do it.
        // If user has not access to create private fields - throw exception.
        if (hasAccess) {
            return getUserProperties(request, source);
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
     * @throws AccessException throw if user han't access to field
     * @throws DataException throw if user or property can not found
     */
    public static UserProperty getUserPropertyById(Request request, PropertiesSource source, RuleDTO rule) throws AccessException, DataException {
        // Compare token ID in token and in params
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        // If id in token and id in params equals - get rule value for user own private fields.
        // If id in token and id in params not equals - get rule value for user other private fields.
        boolean hasAccess = (isTrusted) ? rule.isMyPrivate() : rule.isOtherPrivate();
        // Check user access on create private fields.
        // If user has access to create private fields - try do it.
        // If user has not access to create private fields - throw exception.
        if (hasAccess) {
            return getUserPropertyById(request, source);
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
     * @throws AccessException throw if user han't access to field
     * @throws DataException throw if user or property can not found
     */
    public static UserProperty updateProperty(Request request, PropertiesSource source, RuleDTO rule) throws AccessException, DataException {
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        boolean hasAccess = (isTrusted) ? rule.isMyPrivate() : rule.isOtherPrivate();
        if (hasAccess) {
            return updateUserProperty(request, source);
        } else {
            Error error = new Error("Has no access to update user properties");
            throw new AccessException("CanNotUpdate", error);
        }
    }
    
    /**
     * Method for remove user property fro list by request params with check rule
     * @param request Spark request data
     * @param source user property datasource
     * @param rule rule data transfer object
     * @return actual value of user properties
     * @throws AccessException throw if user hasn't access to remove document
     * @throws DataException throw if user or property can not found
     */
    public static List<UserProperty> deleteUserProperty(Request request, PropertiesSource source, RuleDTO rule) throws AccessException, DataException {
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        boolean hasAccess = (isTrusted) ? rule.isMyPrivate() : rule.isOtherPrivate();
        if (hasAccess) {
            return deleteUserProperty(request, source);
        } else {
            Error error = new Error("Has no access to delete user property");
            throw new AccessException("CanNotDelete", error);
        }
    }
}
