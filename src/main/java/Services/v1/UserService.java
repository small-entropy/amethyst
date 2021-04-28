package Services.v1;
// Import models
import DataTransferObjects.RuleDTO;
import Exceptions.DataException;
import Exceptions.TokenException;
import Models.User;
import Services.core.CoreUserService;
import Sources.UsersSource;
import Utils.common.Comparator;
import Utils.common.RequestUtils;
import Utils.constants.UsersParams;
import java.util.List;
import spark.Request;

/**
 * Class service for work with users collection
 */
public class UserService extends CoreUserService {

    /**
     * Method for update user document
     * @return updated user document
     */
    public static String updateUser() {
        return "Update user data";
    }

    /**
     * Method for deactivate user account
     * @param request Spark request object
     * @param source
     * @return user object after saving
     * @throws TokenException
     * @throws DataException
     */
    public static User markToRemove(Request request, UsersSource source) throws TokenException, DataException {
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
                String paramId = request.params(UsersParams.ID.getName());
                // Find user by id
                User user = getUserById(paramId, source);
                // Check founded user
                // If user found - deactivate user & save changes
                // If user not found - return null
                if (user != null) {
                    // Deactivate user
                    user.deactivate();
                    // Save changes in database
                    source.save(user);
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
     * @param source
     * @return founded user document
     */
    public static User getUserById(Request request, UsersSource source) {
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        String idParam = request.params(UsersParams.ID.getName());
        String[] excludes = isTrusted
                ? UserService.PUBLIC_AND_PRIVATE_ALLOWED
                : UserService.PUBLIC_ALLOWED;
        return getUserById(idParam, excludes, source);
    }
    
   /**
     * Method for get user list
     * @param request Spark request object
     * @param source
     * @param rule
     * @return list of users documents
     * @throws DataException
     */
    public static List<User> getList(Request request, UsersSource source, RuleDTO rule) throws DataException {
        // Set default values for some params
        final int DEFAULT_SKIP = 0;
        final int DEFAULT_LIMIT = 10;
        // Keys in query params
        final String SKIP_FIELD = "skip";
        final String LIMIT_FIELD = "limit";
        // Set skip value from request query
        String qSkip = request.queryMap().get(SKIP_FIELD).value();
        int skip = (qSkip == null) ? DEFAULT_SKIP : Integer.parseInt(qSkip);
        // Set limit value from request query
        String qLimit = request.queryMap().get(LIMIT_FIELD).value();
        int limit = (qLimit == null) ? DEFAULT_LIMIT : Integer.parseInt(qLimit);
        String[] excludes = UserService.getOtherFindOptionsArgs(rule);
        // Get users list
        return getList(skip, limit, excludes, source);
    }
}
