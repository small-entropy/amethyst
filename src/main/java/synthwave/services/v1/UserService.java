package synthwave.services.v1;
// Import models
import synthwave.dto.RuleDTO;
import platform.exceptions.DataException;
import platform.exceptions.TokenException;
import synthwave.models.mongodb.standalones.User;
import synthwave.services.core.CoreUserService;
import platform.utils.helpers.Comparator;
import platform.utils.helpers.ParamsManager;
import platform.utils.helpers.QueryManager;
import platform.utils.helpers.RequestUtils;
import dev.morphia.Datastore;
import java.util.List;
import org.bson.types.ObjectId;
import spark.Request;

/**
 * Class service for work with users collection
 */
public class UserService extends CoreUserService {
    
    public UserService(Datastore datastore) {
        super(
                datastore, 
                new String[] {}, 
                new String[] {
                    "issuedToken", 
                    "password", 
                    "properties", 
                    "rights", 
                    "version",
                    "status"
                },
                new String[] { "password", "properties", "status" }
        );
    }

    /**
     * Method for deactivate user account
     * @param request Spark request object
     * @param usersRepository
     * @return user object after saving
     * @throws TokenException
     * @throws DataException
     */
    public User markToRemove(Request request) 
            throws TokenException, DataException 
    {
        // Get token from request
        String token = RequestUtils.getTokenByRequest(request);
        // Check token on exist
        // If token send - try deactivate user
        // If token not send - throw exception
        if (token != null) {
            // Check ids on equals
            boolean isEqualsIds = Comparator.id_fromParam_fromToken(request);
            // If ids equals - deactivate user account and save it
            // If ids not equals - throw exception
            if (isEqualsIds) {
                // Get id from query params
                ObjectId userId = ParamsManager.getUserId(request);
                // Find user by id
                User user = getUserById(userId);
                // Check founded user
                // If user found - deactivate user & save changes
                // If user not found - return null
                if (user != null) {
                    // Deactivate user
                    user.deactivate();
                    // Save changes in database
                    getRepository().save(user);
                    // Return saved document
                    return user;
                } else {
                    // Return null if user not found
                    Error error = new Error("Can not find user for mark to remove");
                    throw new DataException("NotFound", error);
                }
            } else {
                Error error = new Error("Incorrect token");
                throw new TokenException("NotEquals", error);
            }
        } else {
            Error error = new Error("Token not send");
            throw new TokenException("NotSend", error);
        }
    }

    /**
     * Method for get user information by UUID.If user send token 
     * - get full document.If user not send token - get short document
     * @param request Spark request object
     * @param usersRepository
     * @return founded user document
     * @throws DataException
     */
    public User getUserById(Request request) throws DataException {
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        ObjectId userId = ParamsManager.getUserId(request);
        String[] excludes = isTrusted
                ? getPrivateExcludes()
                : getGlobalExcludes();
        return getUserById(userId, excludes);
    }
    
   /**
     * Method for get user list
     * @param request Spark request object
     * @return list of users documents
     * @throws DataException
     */
    public List<User> getList(
            Request request, 
            String right,
            String action
    ) throws DataException {
        int skip = QueryManager.getSkip(request);
        int limit = QueryManager.getLimit(request);
        RuleDTO rule = getRule(request, right, action);
        String[] excludes = getExcludes(
                rule,
                true
        );
        // Get users list
        return getList(skip, limit, excludes);
    }
}
