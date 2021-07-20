package synthwave.services.v1.users;
import platform.dto.RuleDTO;
import platform.exceptions.AccessException;
import platform.exceptions.DataException;
import platform.exceptions.TokenException;
import synthwave.models.mongodb.embeddeds.EmbeddedProperty;
import synthwave.services.core.users.CoreUserProfileService;
import synthwave.utils.helpers.Comparator;
import dev.morphia.Datastore;
import java.util.Arrays;
import java.util.List;
import spark.Request;

/**
 * Class controller for work with user profile properties
 * @author small-entropy
 * @version 1
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
    public EmbeddedProperty createUserProperty(Request request) 
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
    	boolean hasAccess = checkHasAccess(request, right, action);
        if (hasAccess) {
            return updateProperty(request);
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
    public List<EmbeddedProperty> deleteUserProperty(
            Request request,
            String right,
            String action
    ) throws AccessException, DataException {
        boolean hasAccess = checkHasAccess(request, right, action);
        if (hasAccess) {
            return deleteProperty(request);
        } else {
            Error error = new Error("Has no accesss to delete user property");
            throw new AccessException("CanNotDelete", error);
        }
    }
    
    @Override
    protected boolean checkExistHasAccess(RuleDTO rule, boolean isTrusted) {
		return (isTrusted) ? rule.isMyPublic() : rule.isOtherPublic();
	}
}
