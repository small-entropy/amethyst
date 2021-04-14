package Services;
// Import models
import DTO.RuleDTO;
import DTO.UserDTO;
import Exceptions.DataException;
import Exceptions.TokenException;
import Models.User;
// Import utils for request headers
import Models.UserProperty;
import Models.UserRight;
import Utils.*;
// Import utils for work with JWT
// Import utils for work with query params
// Import standard response class
// Import GSON class
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
// Import Morphia classes
import dev.morphia.Datastore;
import dev.morphia.query.FindOptions;
// Import Spark classes
import org.bson.types.ObjectId;
import spark.Request;
// Import Java standard classes
import java.util.Arrays;
import java.util.List;
// Import Morphia filter criteria methods
import static dev.morphia.query.experimental.filters.Filters.eq;
import static dev.morphia.query.experimental.filters.Filters.and;

/**
 * Class service for work with users collection
 */
public class UserService {

    /**
     * Method for update user document
     * @return updated user document
     */
    public static String updateUser() {
        return "Update user data";
    }

    /**
     * Method for get user document, when send token in request headers or request params
     * @param request Spark request object
     * @param datastore Morphia datastore object
     * @return user document
     * @throws TokenException exception for errors with user token
     * @throws DataException  exception for errors with founded data
     */
    public static User getUserWithTrust(Request request, Datastore datastore) throws TokenException, DataException {
    	// User id from URL params
        String idParams = request.params("id");
        // Result of check on trust
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        // Check trust result.
        // If token and URL have a equals user ID - get full user document.
        // If token and URL haven't equals user ID - throw error. 
        if (isTrusted) {
        	// Generate Object ID from string
            ObjectId id = new ObjectId(idParams);
            // Get user document from database
            User user = datastore
                    .find(User.class)
                    .filter(and(
                            eq("id", id),
                            eq("status", "active")
                    ))
                    .first();
            // Check result on exist.
            // If user exist - return user document.
            // If user not exist (equals null) - throw exception.
            if (user != null) {
                return user;
            } else {
                Error error = new Error("User not found. Send not valid id");
                throw new DataException("NotFound", error);
            }
        } else {
            Error error = new Error("Id from request not equal id from token");
            throw new TokenException("NotEquals", error);
        }
    }

    /**
     * Method for deactivate user account
     * @param request Spark request object
     * @param datastore Morphia datastore (connection)
     * @return user object after saving
     */
    public static User markToRemove(Request request, Datastore datastore) throws TokenException {
        // Get token from request
        String token = RequestUtils.getTokenByRequest(request);
        // Check token on exist
        // If token send - try deactivate user
        // If token not send - throw exception
        if (token != null) {
            // Decode token
            DecodedJWT decoded = JsonWebToken.decode(token);
            // Get id from token
            String decodedId = decoded.getClaim("id").asString();
            // Get id from query params
            String paramId = request.params("id");
            // Check ids on equals
            boolean isEqualsIds = decodedId.equals(paramId);
            // If ids equals - deactivate user account and save it
            // If ids not equals - throw exception
            if (isEqualsIds) {
                // Convert string to ObjectId
                ObjectId id = new ObjectId(paramId);
                // Find user by id
                User user = datastore
                        .find(User.class)
                        .filter(eq("id", id))
                        .first();
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
                    return null;
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
        ObjectId id = new ObjectId(idParam);
        FindOptions findOptions = isTrusted
                ? new FindOptions()
                : new FindOptions()
                    .projection()
                    .exclude("issuedToken", "password", "status", "properties");
        return datastore
                .find(User.class)
                .filter(and(
                        eq("id", id),
                        eq("status", "active")
                ))
                .first(findOptions);
    }

    /**
     * Method for login user by token
     * @param request Spark request object
     * @param datastore datastore (Morphia connection)
     * @return user object
     */
    public static User getUserByToken(Request request, Datastore datastore) {
        // Get token from request headers
        String header = HeadersUtils.getTokenFromHeaders(request);
        // Get token from request query params
        String queryParam = QueryUtils.getTokenFromQuery(request);
        // Check token from exist
        // If token exist in headers or query params - find in database
        // If token not exist in headers or query params - return null
        if (header != null || queryParam != null) {
            // Get token
            // If token exist in headers - set it as value
            // If token not exist in headers - set it as value
            String token = (header != null) ? header : queryParam;
            // Try decode token
            DecodedJWT decoded = JsonWebToken.decode(token);
            // Get user UUID from decoded token as string
            String idString = decoded.getClaim("id").asString();
            // Create ObjectId from user UUID string
            ObjectId id = new ObjectId(idString);
            // Return result from find in database by user UUID
            // (find only active users)
            return datastore
                    .find(User.class)
                    .filter(and(
                        eq("id", id),
                        eq("status", "active")
                    ))
                    .first();
        } else {
            return null;
        }
    }

    /**
     * Method to autologin by token in header ot query params
     * @param request Spark request object
     * @param datastore datastore to work with data (Morphia connection)
     * @return result auth user
     */
    public static User autoLoginUser(Request request, Datastore datastore) {
        User user = UserService.getUserByToken(request, datastore);
        String token = RequestUtils.getTokenByRequest(request);
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
    public static User loginUser(Request request, Datastore datastore) {
        // Transform JSON object from body to Map
        UserDTO userDTO = new Gson().fromJson(request.body(), UserDTO.class);
        // Get field "password" from map
        String password = userDTO.getPassword();
        // Get field "username" from map
        String username = userDTO.getUsername();
        // Find user by username from request body
        User user = datastore
                .find(User.class)
                .filter(and(
                        eq("username", username),
                        eq("status", "active")
                ))
                .first();
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
     * Method for register user
     * @param request Spark request object
     * @param datastore datastore (morphia connection)
     * @return user document
     */
    public static User registerUser(Request request, Datastore datastore) {
        // Crete user data transfer object from JSON
        UserDTO userDTO = new Gson().fromJson(request.body(), UserDTO.class);
        User user = new User(
                userDTO.getUsername(),
                userDTO.getPassword()
        );
        // Save user
        datastore.save(user);
        // Generate JWT token
        String token = JsonWebToken.encode(user);
        // Get tokens list
        List<String> tokens = Arrays.asList(token);
        // Set issued tokens list
        user.setIssuedTokens(tokens);
        // Get defaults user properties & set it in user document
        List<UserProperty> properties = UserPropertyService.getDefaultUserProperty();
        user.setProperties(properties);
        // Get default user profile properties & set it in user document
        List<UserProperty> profile = UserProfileService.getDefaultProfile();
        user.setProfile(profile);
        // Get defaults user rights & set it in user document
        List<UserRight> rights = UserRightService.getDefaultRightList();
        user.setRights(rights);
        // Save changes in database
        datastore.save(user);
        // Set token in headers
        return user;
    }

    /**
     * Method for remove user token
     * @param request Spa   rk request object
     * @param datastore datastore (Morphia connection)
     * @return user document
     */
    public static User logoutUser(Request request, Datastore datastore) {
        // Not include in token value
        final int NOT_IN_LIST = -1;
        // Get user document from database
        User user = UserService.getUserByToken(request, datastore);
        // Check user document on exist
        // If founded user document not equal null - remove token from issued token list,
        // save changes & return saved document.
        // If founded user document equal null - return null.
        if (user != null) {
            // Get token from request
            String token = RequestUtils.getTokenByRequest(request);
            // Get user token index
            int tokenIndex = user.getIssuedTokens().indexOf(token);
            if (tokenIndex != NOT_IN_LIST) {
                // Remove from issued tokens list token from request
                user.getIssuedTokens().remove(tokenIndex);
                // Save changes in database
                datastore.save(user);
                // Return saved document
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
     * Method for get user list
     * @param request Spark request object
     * @param datastore datastore (morphia connection)
     * @return list of users documents
     */
    public static List<User> getList(Request request, Datastore datastore, RuleDTO rule) {
        // Set default values for some params
        final int DEFAULT_SKIP = 0;
        final int DEFAULT_LIMIT = 10;
        // Keys in query params
        final String SKIP_FIELD = "skip";
        final String LIMIT_FIELD = "limit";

        String[] DEFAULT_FIND_EXCLUDE = new String[]{ "issuedToken", "password", "properties", "status", "rights" };
        // Set skip value from request query
        String qSkip = request.queryMap().get(SKIP_FIELD).value();
        int skip = (qSkip == null) ? DEFAULT_SKIP : Integer.parseInt(qSkip);
        // Set limit value from request query
        String qLimit = request.queryMap().get(LIMIT_FIELD).value();
        int limit = (qLimit == null) ? DEFAULT_LIMIT : Integer.parseInt(qLimit);
        // Create find options for iterator
        String[] args = (rule != null)
                ? switch (rule.getOtherAccess()) {
                    case "Full" -> new String[]{};
                    case "PublicAndPrivate" -> new String[]{ "issuedToken", "status", "password" };
                    default -> DEFAULT_FIND_EXCLUDE;
                }
                : DEFAULT_FIND_EXCLUDE;
        FindOptions findOptions = new FindOptions()
                    .projection()
                    .exclude(args)
                    .skip(skip)
                    .limit(limit);
        // Return result as list of users document
        return datastore
                .find(User.class)
                .filter(
                        eq("status", "active")
                )
                .iterator(findOptions)
                .toList();
    }
}
