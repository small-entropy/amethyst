package Services.v1;

import DataTransferObjects.RuleDTO;
import Exceptions.AccessException;
import Exceptions.DataException;
import Exceptions.TokenException;
import Models.UserRight;
import Services.core.CoreRightService;
import Sources.UsersSource;
import Utils.common.Comparator;
import Utils.constants.UsersParams;
import java.util.List;
import spark.Request;

/**
 * Class service for work with user right document
 */
public class UserRightService extends CoreRightService {

    public static String updateRight(Request request, UsersSource source) {
        return "Update user right";
    }

    public static String deleteRight(Request request, UsersSource source) {
        return "Delete user right";
    }

    public static String createRight(Request request, UsersSource source) {
        return "Create user right";
    }

    /**
     * Method for get user right list
     * @param request Spark request object
     * @param source user data source
     * @param rule rule data transfer object
     * @return user document rights list
     * @throws AccessException no access exception
     */
    public static List<UserRight> getUserRights(Request request, UsersSource source, RuleDTO rule) throws AccessException {
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        boolean hasAccess = (isTrusted) ? rule.isMyPrivate() : rule.isOtherPrivate();
        if (hasAccess) {
            return UserRightService.getUserRights(request, source);
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
     * @param source users data source
     * @return user right document
     * @throws DataException exception for errors with data
     * @throws TokenException exception for errors of token
     */
    public static UserRight getUserRightById(Request request, UsersSource source) throws DataException, TokenException {
        List<UserRight> rights = UserRightService.getUserRights(request, source);
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
