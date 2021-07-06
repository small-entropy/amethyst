package synthwave.services.core;

import platform.dto.RuleDTO;
import synthwave.dto.UserDTO;
import synthwave.filters.UsersFilter;
import synthwave.models.mongodb.standalones.User;
import synthwave.utils.jwt.JsonWebToken;
import platform.utils.helpers.RequestUtils;
import synthwave.utils.access.RightManager;
import dev.morphia.Datastore;
import java.util.Arrays;
import spark.Request;

/**
 * Class for base functions for create authorization service
 * @author small-entropy
 */
public abstract class CoreAuthorizationService 
        extends CoreUserService {
    
	/**
	 * Default constructor for core authorization service
	 * @param datastore Morphia datastore object 
	 * @param globalExcludes global exclude fields
	 * @param publicExcludes public exclude fields
	 * @param privateExcludes private exclude fields
	 */
    public CoreAuthorizationService(
            Datastore datastore,
            String[] globalExcludes,
            String[] publicExcludes,
            String[] privateExcludes
    ) {
        super(
                datastore,
                globalExcludes,
                publicExcludes,
                privateExcludes
        );
    }
    
    /**
     * Method for get rule by username from request body
     * @param request Spark request object
     * @param right right name
     * @param action action name
     * @return 
     */
    protected RuleDTO getRule_byUsername(
            Request request,
            String right,
            String action
    ) {
        return RightManager.getRuleByRequest_Username(
                request, 
                getRepository(), 
                right, 
                action
        );
    }

    /**
     * Method for login user by token
     * @param request Spark request object
     * @param filterForReturn filter object for return answer
     * @param filterForSearch filter object for search user
     * @return founded user document
     */
    protected User autoLoginUser(
            Request request, 
            UsersFilter filterForReturn, 
            UsersFilter filterForSearch
    ) {
        // Get user document by token
        User user = getUserByToken(
                request,
                filterForSearch
        );
        // Get token from request
        String token = RequestUtils.getTokenByRequest(request);
        // Check user on exist & check issuedToken (token must be contains 
        // in this field)
        // If all check right - return user object,
        // else - return null.
        return (user != null
                && token != null
                && user.getIssuedTokens() != null
                && user.getIssuedTokens().contains(token))
                ? getUserByToken(
                        request,
                        filterForReturn
                )
                : null;
    }


    /**
     * Method for authorization user
     * @param request Spark request object
     * @param usersRepository source for work with users collection
     * @return user document
     */
    protected User loginUser(Request request) {
        // Transform JSON object from body to Map
        UserDTO userDTO = getUserDtoFromBody(request);
        // Get field "password" from map
        String password = userDTO.getPassword();
        // Find user by username from request body
        User user = getUserByUsername(userDTO);
        // Verified user password:
        // if user not find - false,
        // if user send wrong password - false,
        // if user send correct password - true
        boolean verified = user != null && user.verifyPassword(password).verified;
        // Check verified result
        if (verified) {
            // Get user generated token
            var tokens = user.getIssuedTokens();
            // Check list of user generated tokens.
            // If user have a generated token - get it.
            // If user haven't a generated token - generate new token
            String token;
            if (tokens == null || tokens.isEmpty()) {
                token = JsonWebToken.encode(user);
                user.setIssuedTokens(Arrays.asList(token));
                getRepository().save(user);
            }
            return user;
        } else {
            return null;
        }
    }

    /**
     * Base method for register user
     * @param userDTO user data transfer object
     * @param usersRepository source for work with users collection
     * @return user document
     */
    protected User registerUser(UserDTO userDTO) {
        // Create user document
        userDTO.setProperties(CoreUserPropertyService.getDefaultUserProperty());
        userDTO.setProfile(CoreUserProfileService.getDefaultProfile());
        userDTO.setRights(CoreRightService.getDefaultRightList());
        return getRepository().create(userDTO);
    }

    /**
     * Method for remove user token
     * @param request Spark request object
     * @param usersRepository source for work with users collection
     * @return user document
     */
    protected User logoutUser(Request request) {
        // Not include in token value
        final int NOT_IN_LIST = -1;
        // Create filter object
        UsersFilter filter = new UsersFilter();
        // Set exclude fields for filter
        filter.setExcludes(getGlobalExcludes());
        // Get user document from database (full document)
        User user = getUserByToken(
                request, 
                getRepository(), 
                filter
        );
        // Check user document on exist
        // If founded user document not equal null - remove token from issued
        // token list, save changes & return saved document.
        // If founded user document equal null - return null.
        if (user != null) {
            // Get token from request
            String token = RequestUtils.getTokenByRequest(request);
            // Get user token index
            int tokenIndex = user.getIssuedTokens().indexOf(token);
            // Check token index on list contains
            // If token index not contains in list - return null.
            // If token index contains in list - save changes, and return
            // saved document from database
            if (tokenIndex != NOT_IN_LIST) {
                // Remove from issued tokens list token from request
                user.getIssuedTokens().remove(tokenIndex);
                // Save changes in database
                getRepository().save(user);
                // return user document
                return user;
            } else {
                // If token not include in issued token list
                // return null
                return null;
            }
        } else {
            // Return null if user not found
            return null;
        }
    }
    
    /**
     * Method for get users filter object by user document &
     * rule data transfer object
     * @param user user document
     * @param rule rule data transfer object
     * @return user filter object
     */
    protected UsersFilter getUsersFilter(User user, RuleDTO rule) {
        String[] excludes = getExcludes(
                rule, 
                true
        );
        
        return new UsersFilter(user.getId(), excludes);
    }
}
