package Services.v1;

import DataTransferObjects.v1.RuleDTO;
import DataTransferObjects.v1.UserDTO;
import Exceptions.AuthorizationException;
import Exceptions.DataException;
import Exceptions.TokenException;
import Filters.common.UsersFilter;
import Models.Standalones.User;
import Services.core.CoreAuthorizationService;
import Repositories.v1.UsersRepository;
import spark.Request;

/**
 * Class for authorization service
 * @author small-entropy
 */
public class AuthorizationService extends CoreAuthorizationService {

    /**
     * Method to autologin by token in header ot query params
     * @param request Spark request object
     * @param usersRepository source for work with users collection
     * @param rule rule data transfer object
     * @return result auth user
     * @throws AuthorizationException
     * 
     */
    public static User autoLoginUser(
            Request request, 
            UsersRepository usersRepository, 
            RuleDTO rule
    ) throws AuthorizationException {
        UsersFilter filterForReturn = new UsersFilter();
        UsersFilter filterForSearch = new UsersFilter();
        String[] excludes = AuthorizationService.getExcludes(
                rule, 
                true, 
                UserService.ALL_ALLOWED, 
                UserService.PUBLIC_AND_PRIVATE_ALLOWED, 
                UserService.PUBLIC_ALLOWED
        );
        filterForReturn.setExcludes(excludes);
        filterForSearch.setExcludes(UserService.ALL_ALLOWED);
        // Get user document by token
        User user = autoLoginUser(
                request, 
                usersRepository, 
                filterForReturn, 
                filterForSearch
        );
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
     * @param usersRepository source for work with users collection
     * @param rule rule data transfer object
     * @return user document
     * @throws AuthorizationException
     */
    public static User loginUser(
            Request request, 
            UsersRepository usersRepository, 
            RuleDTO rule
    ) throws AuthorizationException {
        // Authorization user
        User user = AuthorizationService.loginUser(request, usersRepository);
        // Check user on exist
        if (user != null) {
            UsersFilter filter = getUsersFilter(user, rule);
            // Find & return document
            return UserService.getUserById(filter, usersRepository);
        } else {
            Error error = new Error("Can not find user with for authorization");
            throw new AuthorizationException("UserNotFound", error);
        }
    }

    /**
     * Method for register user
     * @param request Spark request object
     * @param usersRepository source for work with users collection
     * @return user document
     */
    public static User registerUser(
            Request request, 
            UsersRepository usersRepository
    ) {
        // Crete user data transfer object from JSON
        UserDTO userDTO = UserDTO.build(request, UserDTO.class); 
        // Create user document in database
        User user = AuthorizationService.registerUser(userDTO, usersRepository);
        // Options for find in documents
        UsersFilter filter = new UsersFilter(
                user.getId(), 
                UserService.PUBLIC_AND_PRIVATE_ALLOWED
        );
        return UserService.getUserById(filter, usersRepository);
    }

    /**
     * Method for remove user token
     * @param request Spark request object
     * @param usersRepository source for work with users collection
     * @param rule rule data transfer object
     * @return user document
     * @throws AuthorizationException 
     */
    public static User logoutUser(
            Request request, 
            UsersRepository usersRepository, 
            RuleDTO rule
    ) throws AuthorizationException {
        User user = AuthorizationService.logoutUser(request, usersRepository);
        if (user != null) {
            UsersFilter filter = getUsersFilter(user, rule);
            // Get user document by user id with find options with rule 
            // excluded fields
            return UserService.getUserById(filter, usersRepository);
        } else {
            Error error = new Error("Can not find user for logout");
            throw new AuthorizationException("UserNotFound", error);
        }
    }
    
    

    /**
     * Method for change user password
     * @param request Spark request object
     * @param usersRepository source for users collection
     * @param rule rule data transfer object
     * @return user document
     * @throws TokenException throw if token not send or token incorrect
     * @throws DataException throw is user not found
     * @throws AuthorizationException throw if can not authotize user by token
     */
    public static User changePassword(
            Request request, 
            UsersRepository usersRepository, 
            RuleDTO rule
    ) throws TokenException, DataException, AuthorizationException {
        // Get user full document
        User user = UserService.getUserWithTrust(request, usersRepository);
        // Get user data transfer object
        UserDTO userDTO = UserDTO.build(request, UserDTO.class);
        // Call regenerate password for user
        user.reGeneratePassword(
                userDTO.getOldPassword(), 
                userDTO.getNewPassword()
        );
        // Save changes in document
        usersRepository.save(user);
        
        // Create filter object
        UsersFilter filter = getUsersFilter(user, rule);
        // Return user by rule
        return UserService.getUserById(filter, usersRepository);
    }
}
