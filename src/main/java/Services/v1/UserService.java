package Services.v1;
// Import models
import DataTransferObjects.v1.RuleDTO;
import Exceptions.DataException;
import Exceptions.TokenException;
import Models.Standalones.User;
import Services.core.CoreUserService;
import Repositories.v1.UsersRepository;
import Utils.common.Comparator;
import Utils.common.ParamsManager;
import Utils.common.QueryManager;
import Utils.common.RequestUtils;
import java.util.Arrays;
import java.util.List;
import org.bson.types.ObjectId;
import spark.Request;

/**
 * Class service for work with users collection
 */
public class UserService extends CoreUserService {

    /**
     * Method for deactivate user account
     * @param request Spark request object
     * @param usersRepository
     * @return user object after saving
     * @throws TokenException
     * @throws DataException
     */
    public static User markToRemove(
            Request request, 
            UsersRepository usersRepository
    ) throws TokenException, DataException {
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
                User user = getUserById(userId, usersRepository);
                // Check founded user
                // If user found - deactivate user & save changes
                // If user not found - return null
                if (user != null) {
                    // Deactivate user
                    user.deactivate();
                    // Save changes in database
                    usersRepository.save(user);
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
    public static User getUserById(
            Request request, 
            UsersRepository usersRepository
    ) throws DataException {
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        ObjectId userId = ParamsManager.getUserId(request);
        String[] excludes = isTrusted
                ? PUBLIC_AND_PRIVATE_ALLOWED
                : PUBLIC_ALLOWED;
        return getUserById(userId, excludes, usersRepository);
    }
    
   /**
     * Method for get user list
     * @param request Spark request object
     * @param usersRepository
     * @param rule
     * @return list of users documents
     * @throws DataException
     */
    public static List<User> getList(
            Request request, 
            UsersRepository usersRepository, 
            RuleDTO rule
    ) throws DataException {
        int skip = QueryManager.getSkip(request);
        int limit = QueryManager.getLimit(request);
        String[] excludes = UserService.getExcludes(
                rule,
                true,
                ALL_ALLOWED,
                PUBLIC_ALLOWED,
                PUBLIC_AND_PRIVATE_ALLOWED
        );
        // Get users list
        return getList(skip, limit, excludes, usersRepository);
    }
}
