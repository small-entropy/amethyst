package synthwave.services.v1;

import synthwave.dto.RuleDTO;
import platform.exceptions.AccessException;
import platform.exceptions.DataException;
import platform.exceptions.TokenException;
import synthwave.models.mongodb.embeddeds.EmbeddedRight;
import synthwave.services.core.CoreRightService;
import platform.utils.helpers.Comparator;
import dev.morphia.Datastore;
import java.util.Arrays;
import java.util.List;
import spark.Request;

/**
 * Class service for work with user right document
 */
public class UserRightService extends CoreRightService {
    
    public UserRightService(Datastore datastore) {
        super(
                datastore,
                Arrays.asList("users_right", "catalogs_right")
        );
    }

    public EmbeddedRight updateRight(
            Request request,  
            String right,
            String action
    ) throws DataException, AccessException {
        RuleDTO rule = getRule(request, right, action);
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        boolean hasAccess = (isTrusted) 
                ? rule.isMyGlobal()
                : rule.isOtherGlobal();
        if (hasAccess) {
            return updateRight(request);
        } else {
            String message = (isTrusted) 
                    ? "Has not rights to create right document for own user document"
                    : "Has not rughts to reate right document for other user document";
            Error error = new Error(message);
            throw new AccessException("CanNotCreate", error);
        }
    }

    public List<EmbeddedRight> deleteRight(
            Request request,
            String right,
            String action
    ) throws DataException, AccessException {
        RuleDTO rule = getRule(request, right, action);
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        boolean hasAccess = (isTrusted) 
                ? rule.isMyGlobal() 
                : rule.isOtherGlobal();
        if (hasAccess) {
            return deleteRights(request);
        } else {
            String message = (isTrusted) 
                    ? "Has not rights to create right document for own user document"
                    : "Has not rughts to reate right document for other user document";
            Error error = new Error(message);
            throw new AccessException("CanNotCreate", error);
        }
    }

    public EmbeddedRight createUserRight(
            Request request, 
            String right,
            String action
    ) throws AccessException, DataException {
        RuleDTO rule = getRule(request, right, action);
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        boolean hasAccess = (isTrusted) 
                ? rule.isMyGlobal() 
                : rule.isOtherGlobal();
        if (hasAccess) {
            return createUserRight(request);
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
     * @return user document rights list
     * @throws AccessException no access exception
     * @throws DataException throws if can not found user or user rights
     */
    public List<EmbeddedRight> getUserRights(
            Request request, 
            String right,
            String action
    ) throws AccessException, DataException {
        RuleDTO rule = getRule(request, right, action);
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        boolean hasAccess = (isTrusted)
                ? rule.isMyGlobal() : rule.isOtherGlobal();
        if (hasAccess) {
            return getUserRights(request);
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
     * @return user right document
     * @throws DataException throw if can not found user or right
     * @throws TokenException throw if token not send or token incorrect
     * @throws AccessException  throw if user has not access for this method
     */
    public EmbeddedRight getUserRightById(
            Request request, 
            String right,
            String action
    ) throws DataException, TokenException, AccessException {
        RuleDTO rule = getRule(request, right, action);
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        boolean hasAccess = (isTrusted)
                ? rule.isMyGlobal()
                : rule.isOtherGlobal();
        if (hasAccess) {
            return getUserRightById(request);
        } else {
            String message = (isTrusted)
                    ? "Has not rights for read own private fields"
                    : "Has not rights for read other private fields";
            Error error = new Error(message);
            throw new AccessException("CanNotRead", error);
        }
    }
}
