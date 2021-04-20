package Services.core;

import DTO.UserDTO;
import Models.User;
import Services.v1.UserService;
import Utils.common.JsonWebToken;
import Utils.common.RequestUtils;
import dev.morphia.Datastore;
import dev.morphia.query.FindOptions;
import org.bson.types.ObjectId;
import spark.Request;

import java.util.Arrays;
import java.util.List;

public abstract class CoreAuthorizationService extends CoreService {
    protected static User autoLoginUser(Request request, Datastore datastore, FindOptions findOptions) {
        // Get user document by token
        User user = UserService.getUserByToken(request, datastore, findOptions);
        // Get token from request
        String token = RequestUtils.getTokenByRequest(request);
        // Check user on exist & check issuedToken (token must be contains in this field)
        // If all check right - return user object,
        // else - return null.
        return (user != null
                && token != null
                && user.getIssuedTokens() != null
                && user.getIssuedTokens().contains(token))
                ? user
                : null;
    }


    /**
     * Method for auth user
     * @param request Spark request object
     * @param datastore datastore (morphia connection)
     * @return user document
     */
    protected static User loginUser(Request request, Datastore datastore) {
        // Transform JSON object from body to Map
        UserDTO userDTO = CoreUserService.getUserDtoFromBody(request);
        // Get field "password" from map
        String password = userDTO.getPassword();
        // Find user by username from request body
        User user = CoreUserService.getUserByUsername(userDTO, datastore);
        // Verified user password:
        // if user not find - false,
        // if user send wrong password - false,
        // if user send correct password - true
        boolean verified = user != null && user.verifyPassword(password).verified;
        // Check verified result
        if (verified) {
            // Get user generated token
            List<String> tokens = user.getIssuedTokens();
            // Check list of user generated tokens.
            // If user have a generated token - get it.
            // If user haven't a generated token - generate new token
            String token;
            if (tokens == null || tokens.size() == 0) {
                token = JsonWebToken.encode(user);
                user.setIssuedTokens(Arrays.asList(token));
                datastore.save(user);
            }
            return user;
        } else {
            return null;
        }
    }

    /**
     * Base method for register user
     * @param userDTO user data transfer obejct
     * @param datastore Morphia datastore object
     * @return user document
     */
    protected static User registerUser(UserDTO userDTO, Datastore datastore) {
        // Create user document
        User user = new User(
                new ObjectId(),
                userDTO.getUsername(),
                userDTO.getPassword(),
                CoreUserPropertyService.getDefaultUserProperty(),
                CoreUserProfileService.getDefaultProfile(),
                CoreRightService.getDefaultRightList()
        );
        // Generate JWT token
        String token = JsonWebToken.encode(user);
        // Get tokens list
        List<String> tokens = Arrays.asList(token);
        // Set issued tokens list
        user.setIssuedTokens(tokens);
        // Save user document in database
        datastore.save(user);
        // Return user document
        return user;
    }

    /**
     * Method for remove user token
     * @param request Spark request object
     * @param datastore datastore (Morphia connection)
     * @return user document
     */
    protected static User logoutUser(Request request, Datastore datastore) {
        // Not include in token value
        final int NOT_IN_LIST = -1;
        // Get user document from database (full document)
        User user = CoreUserService.getUserByToken(request, datastore);
        // Check user document on exist
        // If founded user document not equal null - remove token from issued token list,
        // save changes & return saved document.
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
                datastore.save(user);
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
}
