package Services.v1;

import DataTransferObjects.RuleDTO;
import Exceptions.AccessException;
import Exceptions.DataException;
import Exceptions.TokenException;
import Models.UserRight;
import Services.core.CoreRightService;
import Sources.RightsSource;
import Sources.UsersSource;
import Utils.common.Comparator;
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
     * @throws DataException throws if can not found user or user rights
     */
    public static List<UserRight> getUserRights(Request request, RightsSource source, RuleDTO rule) throws AccessException, DataException {
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        boolean hasAccess = (isTrusted) ? rule.isMyGlobal() : rule.isOtherGlobal();
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
     * Method for get user right by id from request params with rule check
     * @param request Spark request object;
     * @param source user right datasource
     * @param rule rule data transfer obejct
     * @return user right document
     * @throws DataException throw if can not found user or right
     * @throws TokenException throw if token not send or token incorrect
     * @throws AccessException  throw if user has not access for this method
     */
    public static UserRight getUserRightById(Request request, RightsSource source, RuleDTO rule) throws DataException, TokenException, AccessException {
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        boolean hasAccess = (isTrusted) ? rule.isMyGlobal(): rule.isOtherGlobal();
        if (hasAccess) {
            return getUserRightById(request, source);
        } else {
            String message = (isTrusted)
                    ? "Can not rights for read own private fields"
                    : "Can not rights for read other private fields";
            Error error = new Error(message);
            throw new AccessException("CanNotRead", error);
        }
    }
}
