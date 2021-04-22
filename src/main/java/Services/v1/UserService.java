package Services.v1;
// Import models
import DTO.RuleDTO;
import Exceptions.DataException;
import Exceptions.TokenException;
import Models.User;
import Services.core.CoreUserService;
// Import utils for work with JWT
// Import utils for work with query params
// Import standard response class
// Import GSON class
import Utils.common.Comparator;
import Utils.common.RequestUtils;
import com.google.gson.Gson;
// Import Morphia classes
import dev.morphia.Datastore;
import dev.morphia.query.FindOptions;
// Import Spark classes
import spark.Request;
// Import Java standard classes
import java.util.List;
// Import Morphia filter criteria methods
import static dev.morphia.query.experimental.filters.Filters.eq;

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
     * @param datastore Morphia datastore (connection)
     * @return user object after saving
     */
    public static User markToRemove(Request request, Datastore datastore) throws TokenException, DataException {
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
                String paramId = request.params("id");
                // Find user by id
                User user = UserService.getUserById(paramId, datastore);
                // Check founded user
                // If user found - deactivate user & save changes
                // If user not found - return null
                if (user != null) {
                    // Deactivate user
                    user.deactivate();
                    // Save changes in database
                    datastore.save(user);
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
     * Method for get user information by UUID.
     * If user send token - get full document.
     * If user not send token - get short document
     * @param request Spark request object
     * @param datastore Morphia datastore (connection)
     * @return founded user document
     */
    public static User getUserById(Request request, Datastore datastore) {
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        String idParam = request.params("id");
        String[] excludes = isTrusted
                ? UserService.PUBLIC_AND_PRIVATE_ALLOWED
                : UserService.PUBLIC_ALLOWED;
        FindOptions findOptions = new FindOptions()
                    .projection()
                    .exclude(excludes);
        return UserService.getUserById(idParam, datastore, findOptions);
    }

    /**
     * Method for get user list
     * @param request Spark request object
     * @param datastore datastore (morphia connection)
     * @param rule user rule for this action
     * @return list of users documents
     */
    public static List<User> getList(Request request, Datastore datastore, RuleDTO rule) throws DataException {
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
        return UserService.getList(skip, limit, excludes, datastore);
    }
}
