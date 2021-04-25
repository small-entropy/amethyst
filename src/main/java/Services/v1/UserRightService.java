package Services.v1;

import DTO.RuleDTO;
import Exceptions.AccessException;
import Exceptions.DataException;
import Exceptions.TokenException;
import Models.UserRight;
import Services.core.CoreRightService;
import Utils.common.Comparator;
import Utils.constants.UsersParams;
import dev.morphia.Datastore;
import spark.Request;

import java.util.List;

/**
 * Class service for work with user right document
 */
public class UserRightService extends CoreRightService {

    public static String updateRight(Request request, Datastore datastore) {
        return "Update user right";
    }

    public static String deleteRight(Request request, Datastore datastore) {
        return "Delete user right";
    }

    public static String createRight(Request request, Datastore datastore) {
        return "Create user right";
    }

    /**
     * Method for get user right list
     * @param request Spark request object
     * @param datastore Morphia datastore object
     * @param rule rule data transfer object
     * @return user document rights list
     * @throws AccessException no access exception
     */
    public static List<UserRight> getUserRights(Request request, Datastore datastore, RuleDTO rule) throws AccessException {
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        boolean hasAccess = (isTrusted) ? rule.isMyPrivate() : rule.isOtherPrivate();
        if (hasAccess) {
            return UserRightService.getUserRights(request, datastore);
        } else {
            String message = (isTrusted)
                    ? "Can not rights for read own private fields"
                    : "Can not rights for read other private fields";
            Error error = new Error(message);
            throw new AccessException("CanNotRead", error);
        }
    }

    /**
     * Method for get user right by id
     * @param request Spark request object
     * @param datastore Morphia datastore (connection) object
     * @return user right document
     * @throws DataException exception for errors with data
     * @throws TokenException exception for errors of token
     */
    public static UserRight getUserRightById(Request request, Datastore datastore) throws DataException, TokenException {
        List<UserRight> rights = UserRightService.getUserRights(request, datastore);
        String rightId = request.params(UsersParams.RIGHT_ID.getName());
        UserRight result = null;
        if (rights != null && rightId != null) {
            for (UserRight right : rights) {
                if (right.getId().toString().equals(rightId)) {
                    result = right;
                }
            }
        }
        return result;
    }
}
