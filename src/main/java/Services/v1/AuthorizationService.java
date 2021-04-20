package Services.v1;

import DTO.RuleDTO;
import DTO.UserDTO;
import Models.User;
import Services.core.CoreAuthorizationService;
import Utils.common.RequestUtils;
import com.google.gson.Gson;
import dev.morphia.Datastore;
import dev.morphia.query.FindOptions;
import spark.Request;

public class AuthorizationService extends CoreAuthorizationService {

    /**
     * Method to autologin by token in header ot query params
     * @param request Spark request object
     * @param datastore datastore to work with data (Morphia connection)
     * @param rule rule data transfer object
     * @return result auth user
     */
    public static User autoLoginUser(Request request, Datastore datastore, RuleDTO rule) {
        // Create finding options for find user with current rule
        FindOptions findOptions = new FindOptions()
                .projection()
                .exclude(AuthorizationService.getMyFindOptionsArgs(rule));
        return AuthorizationService.autoLoginUser(request, datastore, findOptions);
    }

    /**
     * Method for auth user
     * @param request Spark request object
     * @param datastore datastore (morphia connection)
     * @return user document
     */
    public static User loginUser(Request request, Datastore datastore, RuleDTO rule) {
        // Authorization user
        User user = AuthorizationService.loginUser(request, datastore);
        // Check user on exist
        if (user != null) {
            // Create find options by roles
            FindOptions findOptions = new FindOptions()
                    .projection()
                    .exclude(AuthorizationService.getMyFindOptionsArgs(rule));
            // Find & return document
            return UserService.getUserById(user.getPureId(), datastore, findOptions);
        } else {
            return null;
        }
    }

    /**
     * Method for register user
     * @param request Spark request object
     * @param datastore datastore (morphia connection)
     * @return user document
     */
    public static User registerUser(Request request, Datastore datastore) {
        // Crete user data transfer object from JSON
        UserDTO userDTO = new Gson().fromJson(request.body(), UserDTO.class);
        // Create user document in database
        User user = AuthorizationService.registerUser(userDTO, datastore);
        // Options for find in documents
        FindOptions findOptions = new FindOptions()
                .projection()
                .exclude(UserService.PUBLIC_AND_PRIVATE_ALLOWED);
        // Set token in headers
        return UserService.getUserById(user.getPureId(), datastore, findOptions);
    }

    /**
     * Method for remove user token
     * @param request Spark request object
     * @param datastore datastore (Morphia connection)
     * @param rule rule data transfer object
     * @return user document
     */
    public static User logoutUser(Request request, Datastore datastore, RuleDTO rule) {
        User user = AuthorizationService.logoutUser(request, datastore);
        if (user != null) {
            // Crate find options
            FindOptions findOptions = new FindOptions()
                    .projection()
                    .exclude(AuthorizationService.getMyFindOptionsArgs(rule));
            // Get user document by user id with find options with rule excluded fields
            return UserService.getUserById(user.getPureId(), datastore, findOptions);
        } else {
            return null;
        }
    }
}
