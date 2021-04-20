package Services.v1;
// Import user model (class)
import DTO.RuleDTO;
import Exceptions.AccessException;
import Exceptions.DataException;
import Exceptions.TokenException;
// Import user property model (class)
import Models.UserProperty;
import Services.core.CoreUserPropertyService;
import Utils.common.Comparator;
import dev.morphia.Datastore;
import spark.Request;

import java.util.List;

/**
 * Class controller for work with user properties
 */
public class UserPropertyService extends CoreUserPropertyService {

    public static List<UserProperty> removeUserProperty(Request request, Datastore datastore) {
        return null;
    }


    /**
     * Method for create user property
     * @param request Spark request object
     * @param datastore Morphia datastore object
     * @param rule rule data transfer object
     * @return user property
     * @throws AccessException user access exception
     */
    public static UserProperty createUserProperty(Request request, Datastore datastore, RuleDTO rule) throws AccessException {
        // Compare token ID in token and in params
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        // If id in token and id in params equals - get rule value for user own private fields.
        // If id in token and id in params not equals - get rule value for user other private fields.
        boolean hasAccess = (isTrusted) ? rule.isMyPrivate() : rule.isOtherPrivate();
        // Check user access on create private fields.
        // If user has access to create private fields - try do it.
        // If user has not access to create private fields - throw exception.
        if (hasAccess) {
            return UserPropertyService.createUserProperty(request, datastore);
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
            return UserPropertyService.getUserProperties(request, datastore);
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
    public static UserProperty getUserPropertyById(Request request, Datastore datastore, RuleDTO rule) throws AccessException {
        // Compare token ID in token and in params
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        // If id in token and id in params equals - get rule value for user own private fields.
        // If id in token and id in params not equals - get rule value for user other private fields.
        boolean hasAccess = (isTrusted) ? rule.isMyPrivate() : rule.isOtherPrivate();
        // Check user access on create private fields.
        // If user has access to create private fields - try do it.
        // If user has not access to create private fields - throw exception.
        if (hasAccess) {
            return UserPropertyService.getUserPropertyById(request, datastore);
        } else {
            String message = (isTrusted)
                    ? "Can not rights for read own private fields"
                    : "Can not rights for read other private fields";
            Error error = new Error(message);
            throw new AccessException("CanRead", error);
        }
    }
}
