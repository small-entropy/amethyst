package Services.v1;

import DataTransferObjects.v1.RuleDTO;
import Exceptions.AccessException;
import Exceptions.DataException;
import Exceptions.TokenException;
import Models.Embeddeds.EmbeddedRight;
import Services.core.CoreRightService;
import Repositories.v1.RightsRepository;
import Utils.common.Comparator;
import java.util.List;
import spark.Request;

/**
 * Class service for work with user right document
 */
public class UserRightService extends CoreRightService {

    public static EmbeddedRight updateRight(
            Request request, 
            RightsRepository rightsRepository, 
            RuleDTO rule
    ) throws DataException, AccessException {
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        boolean hasAccess = (isTrusted) ? rule.isMyGlobal() : rule.isOtherGlobal();
        if (hasAccess) {
            return updateRight(request, rightsRepository);
        } else {
            String message = (isTrusted) 
                    ? "Has not rights to create right document for own user document"
                    : "Has not rughts to reate right document for other user document";
            Error error = new Error(message);
            throw new AccessException("CanNotCreate", error);
        }
    }

    public static List<EmbeddedRight> deleteRight(
            Request request, 
            RightsRepository rightsRepository, 
            RuleDTO rule
    ) throws DataException, AccessException {
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        boolean hasAccess = (isTrusted) ? rule.isMyGlobal() : rule.isOtherGlobal();
        if (hasAccess) {
            return deleteRights(request, rightsRepository);
        } else {
            String message = (isTrusted) 
                    ? "Has not rights to create right document for own user document"
                    : "Has not rughts to reate right document for other user document";
            Error error = new Error(message);
            throw new AccessException("CanNotCreate", error);
        }
    }

    public static EmbeddedRight createUserRight(
            Request request, 
            RightsRepository rightsRepository, 
            RuleDTO rule
    ) throws AccessException, DataException {
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        boolean hasAccess = (isTrusted) ? rule.isMyGlobal() : rule.isOtherGlobal();
        if (hasAccess) {
            return createUserRight(request, rightsRepository);
        } else {
            String message = (isTrusted) 
                    ? "Has not rights to create right document for own user document"
                    : "Has not rughts to reate right document for other user document";
            Error error = new Error(message);
            throw new AccessException("CanNotCreate", error);
        }
    }

    /**
     * Method for get user right list
     * @param request Spark request object
     * @param rightsRepository user data source
     * @param rule rule data transfer object
     * @return user document rights list
     * @throws AccessException no access exception
     * @throws DataException throws if can not found user or user rights
     */
    public static List<EmbeddedRight> getUserRights(
            Request request, 
            RightsRepository rightsRepository, 
            RuleDTO rule
    ) throws AccessException, DataException {
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        boolean hasAccess = (isTrusted) ? rule.isMyGlobal() : rule.isOtherGlobal();
        if (hasAccess) {
            return getUserRights(request, rightsRepository);
        } else {
            String message = (isTrusted)
                    ? "Has not rights for read own private fields"
                    : "HAs not rights for read other private fields";
            Error error = new Error(message);
            throw new AccessException("CanNotRead", error);
        }
    }

    /**
     * Method for get user right by id from request params with rule check
     * @param request Spark request object;
     * @param rightsRepository user right datasource
     * @param rule rule data transfer obejct
     * @return user right document
     * @throws DataException throw if can not found user or right
     * @throws TokenException throw if token not send or token incorrect
     * @throws AccessException  throw if user has not access for this method
     */
    public static EmbeddedRight getUserRightById(
            Request request, 
            RightsRepository rightsRepository, 
            RuleDTO rule
    ) throws DataException, TokenException, AccessException {
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        boolean hasAccess = (isTrusted) ? rule.isMyGlobal(): rule.isOtherGlobal();
        if (hasAccess) {
            return getUserRightById(request, rightsRepository);
        } else {
            String message = (isTrusted)
                    ? "Has not rights for read own private fields"
                    : "Has not rights for read other private fields";
            Error error = new Error(message);
            throw new AccessException("CanNotRead", error);
        }
    }
}
