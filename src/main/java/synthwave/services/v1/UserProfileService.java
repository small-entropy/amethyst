package synthwave.services.v1;
import synthwave.dto.v1.RuleDTO;
import platform.exceptions.AccessException;
import platform.exceptions.DataException;
import platform.exceptions.TokenException;
import synthwave.models.mongodb.embeddeds.EmbeddedProperty;
import synthwave.services.core.CoreUserProfileService;
import platform.utils.helpers.Comparator;
import dev.morphia.Datastore;
import java.util.Arrays;
import java.util.List;
import spark.Request;

/**
 * Class controller for work with user profile properties
 */
public class UserProfileService extends CoreUserProfileService {
    
    public UserProfileService(Datastore datastore) {
        super(
                datastore,
                Arrays.asList("registered")
        );
    }

    /**
     * Method for create user profile property
     * @param request Spark request object
     * @param profileRepository source for work with users collection
     * @return user property
     * @throws DataException throw if user or property can not found
     * @throws TokenException throw if token not corect or not send
     */
    public EmbeddedProperty createUserProfileProperty(Request request) 
            throws DataException, TokenException {
        // Check equals id from request url & id from send token
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        // Check result compares ids.
        // If ids equals - try create user property.
        // If ids not equals - return null.
        if (isTrusted) {
            return createUserProperty(request);
        } else {
            Error error = new Error("Id from request not equal id from token");
            throw new TokenException("NotEquals", error);
        }
    }

    /**
     * Method for update user profile property
     * @param request Spark request object
     * @return updated user property
     * @throws AccessException throw exception if request not access for update action
     * @throws DataException throw exception if some data (property or user) not found
     */
    public EmbeddedProperty updateUserProperty(
            Request request,
            String right,
            String action
    ) throws AccessException, DataException {
        RuleDTO rule = getRule(request, right, action);
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        boolean hasAccess = (isTrusted) 
                ? rule.isMyPublic() 
                : rule.isOtherPublic();
        if (hasAccess) {
            return updateUserProperty(request);
        } else {
            Error error = new Error("Has no access to update user property");
            throw new AccessException("CanNotUpdate", error);
        }
    }

    /**
     * Method for remove user profile property by requst params with rule checks
     * @param request Spark request object 
     * @return actual list of profile properties
     * @throws AccessException throw if user hasn't rigth to remove profile proeprty document
     * @throws DataException  throw if user or property can not be found
     */
    public List<EmbeddedProperty> deleteProfileProperty(
            Request request,
            String right,
            String action
    ) throws AccessException, DataException {
        RuleDTO rule = getRule(request, right, action);
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        boolean hasAccess = (isTrusted) 
                ? rule.isMyPublic() 
                : rule.isOtherPublic();
        if (hasAccess) {
            return deleteUserProfileProperty(request);
        } else {
            Error error = new Error("Has no accesss to delete user property");
            throw new AccessException("CanNotDelete", error);
        }
    }
}
