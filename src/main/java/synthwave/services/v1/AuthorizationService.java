package synthwave.services.v1;

import platform.dto.RuleDTO;
import synthwave.dto.UserDTO;
import platform.exceptions.AuthorizationException;
import platform.exceptions.DataException;
import platform.exceptions.TokenException;
import synthwave.filters.UsersFilter;
import synthwave.models.mongodb.standalones.User;
import synthwave.services.core.CoreAuthorizationService;
import synthwave.repositories.mongodb.v1.UsersRepository;
import dev.morphia.Datastore;
import spark.Request;

/**
 * Class for authorization service
 * @author small-entropy
 */
public class AuthorizationService extends CoreAuthorizationService {
    
    public AuthorizationService(Datastore datastore) {
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
                new String[] { "password", "properties", "status"}
        );
    }

    /**
     * Method to autologin by token in header ot query params
     * @param request Spark request object
     * @param usersRepository source for work with users collection
     * @param rule rule data transfer object
     * @return result auth user
     * @throws AuthorizationException
     * 
     */
    public User autoLoginUser(
            Request request, 
            String right,
            String action
    ) throws AuthorizationException {
        RuleDTO rule = getRule(request, right, action);
        UsersFilter filterForReturn = new UsersFilter();
        UsersFilter filterForSearch = new UsersFilter();
        String[] excludes = getExcludes(
                rule, 
                true
        );
        filterForReturn.setExcludes(excludes);
        filterForSearch.setExcludes(getGlobalExcludes());
        // Get user document by token
        User user = autoLoginUser(
                request,
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
    public User loginUser(
            Request request, 
            String right,
            String action
    ) throws AuthorizationException {
        RuleDTO rule = getRule_byUsername(request, right, action);
        // Authorization user
        User user = loginUser(request);
        // Check user on exist
        if (user != null) {
            UsersFilter filter = getUsersFilter(user, rule);
            // Find & return document
            return getUserById(filter);
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
    public User registerUser(Request request) {
        // Crete user data transfer object from JSON
        UserDTO userDTO = UserDTO.build(request, UserDTO.class); 
        // Create user document in database
        User user = registerUser(userDTO);
        // Options for find in documents
        UsersFilter filter = new UsersFilter(
                user.getId(), 
                getPrivateExcludes()
        );
        return getRepository().findOneById(filter);
    }

    /**
     * Method for remove user token
     * @param request Spark request object
     * @param usersRepository source for work with users collection
     * @param rule rule data transfer object
     * @return user document
     * @throws AuthorizationException 
     */
    public User logoutUser(
            Request request, 
            String right,
            String action
    ) throws AuthorizationException {
        User user = logoutUser(request);
        if (user != null) {
            RuleDTO rule = getRule(request, right, action);
            UsersFilter filter = getUsersFilter(user, rule);
            // Get user document by user id with find options with rule 
            // excluded fields
            return getUserById(filter);
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
    public User changePassword(
            Request request, 
            String right,
            String action
    ) throws TokenException, DataException, AuthorizationException {
        RuleDTO rule = getRule(request, right, action);
        // Get user full document
        User user = getUserWithTrust(request);
        // Get user data transfer object
        UserDTO userDTO = UserDTO.build(request, UserDTO.class);
        // Call regenerate password for user
        user.reGeneratePassword(
                userDTO.getOldPassword(), 
                userDTO.getNewPassword()
        );
        // Save changes in document
        getRepository().save(user);
        
        // Create filter object
        UsersFilter filter = getUsersFilter(user, rule);
        // Return user by rule
        return getUserById(filter);
    }
}
