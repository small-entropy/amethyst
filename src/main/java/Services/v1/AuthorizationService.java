package Services.v1;

import DataTransferObjects.RuleDTO;
import DataTransferObjects.UserDTO;
import Exceptions.AuthorizationException;
import Exceptions.DataException;
import Exceptions.TokenException;
import Filters.UsersFilter;
import Models.User;
import Services.core.CoreAuthorizationService;
import Sources.UsersSource;
import com.google.gson.Gson;
import spark.Request;

public class AuthorizationService extends CoreAuthorizationService {

    /**
     * Method to autologin by token in header ot query params
     * @param request Spark request object
     * @param source source for work with users collection
     * @param rule rule data transfer object
     * @return result auth user
     * @throws AuthorizationException
     * 
     */
    public static User autoLoginUser(Request request, UsersSource source, RuleDTO rule) throws AuthorizationException {
        UsersFilter filter = new UsersFilter();
        filter.setExcludes(AuthorizationService.getMyFindOptionsArgs(rule));
        // Get user document by token
        User user = autoLoginUser(request, source, filter);
        if (user != null) {
            return user;
        } else {
            Error error = new Error("Can not find user for login by token");
            throw new AuthorizationException("UserNotFound", error);
        }
    }

    /**
     * Method for auth user
     * @param request Spark request object
     * @param source source for work with users collection
     * @param rule rule data transfer object
     * @return user document
     * @throws AuthorizationException
     */
    public static User loginUser(Request request, UsersSource source, RuleDTO rule) throws AuthorizationException {
        // Authorization user
        User user = AuthorizationService.loginUser(request, source);
        // Check user on exist
        if (user != null) {
            // Create find options by roles
            UsersFilter filter = new UsersFilter(user.getPureId(), AuthorizationService.getMyFindOptionsArgs(rule));
            // Find & return document
            return UserService.getUserById(filter, source);
        } else {
            Error error = new Error("Can not find user with for authorization");
            throw new AuthorizationException("UserNotFound", error);
        }
    }

    /**
     * Method for register user
     * @param request Spark request object
     * @param source source for work with users collection
     * @return user document
     */
    public static User registerUser(Request request, UsersSource source) {
        // Crete user data transfer object from JSON
        UserDTO userDTO = new Gson().fromJson(request.body(), UserDTO.class);
        // Create user document in database
        User user = AuthorizationService.registerUser(userDTO, source);
        // Options for find in documents
        UsersFilter filter = new UsersFilter();
        filter.setId(user.getPureId());
        filter.setExcludes(UserService.PUBLIC_AND_PRIVATE_ALLOWED);
        return UserService.getUserById(filter, source);
    }

    /**
     * Method for remove user token
     * @param request Spark request object
     * @param source source for work with users collection
     * @param rule rule data transfer object
     * @return user document
     * @throws AuthorizationException 
     */
    public static User logoutUser(Request request, UsersSource source, RuleDTO rule) throws AuthorizationException {
        User user = AuthorizationService.logoutUser(request, source);
        if (user != null) {
            // Crate find options
            UsersFilter filter = new UsersFilter(user.getId(), AuthorizationService.getMyFindOptionsArgs(rule));
            // Get user document by user id with find options with rule excluded fields
            return UserService.getUserById(filter, source);
        } else {
            Error error = new Error("Can not find user for logout");
            throw new AuthorizationException("UserNotFound", error);
        }
    }

    public static User changePassword(Request request, UsersSource source, RuleDTO rule) throws TokenException, DataException, AuthorizationException {
        User user = UserService.getUserWithTrust(request, source);
        UserDTO userDTO = new Gson().fromJson(request.body(), UserDTO.class);
        user.reGeneratePassword(userDTO.getOldPassword(), userDTO.getNewPassword());
        source.save(user);
        UsersFilter filter = new UsersFilter(user.getId(), AuthorizationService.getMyFindOptionsArgs(rule));
        return UserService.getUserById(filter, source);
    }
}
