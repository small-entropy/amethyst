package Services;
// Import models
import DTO.RuleDTO;
import DTO.UserDTO;
import Exceptions.DataException;
import Exceptions.TokenException;
import Models.User;
import Utils.*;
// Import utils for work with JWT
// Import utils for work with query params
// Import standard response class
// Import GSON class
import com.google.gson.Gson;
// Import Morphia classes
import dev.morphia.Datastore;
import dev.morphia.query.FindOptions;
// Import Spark classes
import org.bson.types.ObjectId;
import spark.Request;
// Import Java standard classes
import javax.xml.crypto.Data;
import java.util.Arrays;
import java.util.List;
// Import Morphia filter criteria methods
import static dev.morphia.query.experimental.filters.Filters.eq;
import static dev.morphia.query.experimental.filters.Filters.and;

/**
 * Class service for work with users collection
 */
public class UserService {

    // Fields for only public field by rights for users collection
    private final static String[] PUBLIC_ALLOWED = new String[]{ "issuedToken", "password", "properties", "status", "rights" };
    // Fields for public and private fields by rights for users collection
    private final static String[] PUBLIC_AND_PRIVATE_ALLOWED = new String[]{ "status", "password" };
    // Fields for all fields by rights for users collection
    private final static String[] ALL_ALLOWED = new String[]{};
    /**
     * Method for update user document
     * @return updated user document
     */
    public static String updateUser() {
        return "Update user data";
    }

    /**
     * Method for find user by username from request
     * Method return document with all fields
     * @param request Spark request object
     * @param datastore Morphia datastore object
     * @return user document
     */
    public static User getUserByUsername(Request request, Datastore datastore) {
        UserDTO userDTO = UserService.getUserDtoFromBody(request);
        return UserService.getUserByUsername(userDTO, datastore);
    }

    /**
     * Method for find user by username from user data transfer object
     * @param userDTO user data transfer object
     * @param datastore Morphia datastore
     * @return user document
     */
    public static User getUserByUsername(UserDTO userDTO, Datastore datastore) {
        FindOptions findOptions = new FindOptions()
                .projection()
                .exclude(UserService.ALL_ALLOWED);
        return UserService.getUserByUsername(userDTO, datastore, findOptions);
    }

    /**
     * Method fo find user by username from user data transfer object.
     * In this method user document can exclude fields by find options
     * @param userDTO user data transfer object
     * @param datastore Morphia datastore object
     * @param findOptions find options object
     * @return user document
     */
    public static User getUserByUsername(UserDTO userDTO, Datastore datastore, FindOptions findOptions) {
        return datastore.find(User.class)
                .filter(and(
                        eq("username", userDTO.getUsername()),
                        eq("status", "active")
                ))
                .first(findOptions);
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
        // Result of check on trust
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        // Check trust result.
        // If token and URL have a equals user ID - get full user document.
        // If token and URL haven't equals user ID - throw error. 
        if (isTrusted) {
            // User id from URL params
            String idParams = request.params("id");
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
        FindOptions findOptions = isTrusted
                ? new FindOptions()
                    .projection()
                    .exclude(UserService.PUBLIC_AND_PRIVATE_ALLOWED)
                : new FindOptions()
                    .projection()
                    .exclude(UserService.PUBLIC_ALLOWED);
        return UserService.getUserById(idParam, datastore, findOptions);
    }

    /**
     * Method for find user by id (if id is ObjectId type)
     * @param id user id (ObjectID)
     * @param datastore Morphia datastore object
     * @param findOptions find options
     * @return user document
     */
    public static User getUserById(ObjectId id, Datastore datastore, FindOptions findOptions) {
        return datastore
                .find(User.class)
                .filter(and(
                        eq("id", id),
                        eq("status", "active")
                ))
                .first(findOptions);
    }

    /**
     * Method for find user by param id (id is string)
     * @param paramId user id as string
     * @param datastore Morphia datastore object
     * @param findOptions find options
     * @return user document
     */
    public static User getUserById(String paramId, Datastore datastore, FindOptions findOptions) {
        ObjectId id = new ObjectId(paramId);
        return UserService.getUserById(id, datastore, findOptions);
    }

    /**
     * Method for get user document by param id (id as string).
     * This method return document with all fields
     * @param paramId user id as string
     * @param datastore Morphia datastore
     * @return user document
     */
    public static User getUserById(String paramId, Datastore datastore) {
        FindOptions findOptions = new FindOptions()
                .projection()
                .exclude(UserService.ALL_ALLOWED);
        return UserService.getUserById(paramId, datastore, findOptions);
    }

    /**
     * Method for login user by token
     * @param request Spark request object
     * @param datastore datastore (Morphia connection)
     * @return user object
     */
    public static User getUserByToken(Request request, Datastore datastore, FindOptions findOptions) {
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
            // Get user id from token
            ObjectId id = JsonWebToken.getIdFromToken(token);
            // Find & return user document
            return UserService.getUserById(id, datastore, findOptions);
        } else {
            return null;
        }
    }

    /**
     * Method for get full user document without rule object
     * @param request Spark request object
     * @param datastore Morphia datastore object
     * @return user document
     */
    public static User getUserByToken(Request request, Datastore datastore) {
        // Create find options
        FindOptions findOptions = new FindOptions()
                .projection()
                .exclude(UserService.ALL_ALLOWED);
        // Return user document
        return UserService.getUserByToken(request, datastore, findOptions);
    }

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
                .exclude(UserService.getMyFindOptionsArgs(rule));
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
     * Method for create user data transfer object from request body
     * @param request Spark request object
     * @return user data transfer object
     */
    private static UserDTO getUserDtoFromBody(Request request) {
        return new Gson().fromJson(request.body(), UserDTO.class);
    }

    /**
     * Method for get find options argument by rule my documents access state
     * @param rule user rule
     * @return arguments for find options
     */
    private static String[] getMyFindOptionsArgs(RuleDTO rule) {
        return (rule != null)
                ? switch (rule.getMyAccess()) {
                    case "Full" -> UserService.ALL_ALLOWED;
                    case "PublicAndPrivate" -> UserService.PUBLIC_AND_PRIVATE_ALLOWED;
                    default -> UserService.PUBLIC_ALLOWED;
                }
                : UserService.PUBLIC_ALLOWED;
    }

    /**
     * Method for get find options argument by rule other documents access state
     * @param rule user rule
     * @return arguments for find options
     */
    private static String[] getOtherFindOptionsArgs(RuleDTO rule) {
        return (rule != null)
                ? switch (rule.getOtherAccess()) {
            case "Full" -> UserService.ALL_ALLOWED;
            case "PublicAndPrivate" -> UserService.PUBLIC_AND_PRIVATE_ALLOWED;
            default -> UserService.PUBLIC_ALLOWED;
        }
                : UserService.PUBLIC_ALLOWED;
    }

    /**
     * Method for auth user
     * @param request Spark request object
     * @param datastore datastore (morphia connection)
     * @return user document
     */
    public static User loginUser(Request request, Datastore datastore, RuleDTO rule) {
        // Transform JSON object from body to Map
        UserDTO userDTO = UserService.getUserDtoFromBody(request);
        // Get field "password" from map
        String password = userDTO.getPassword();
        // Find user by username from request body
        User user = UserService.getUserByUsername(userDTO, datastore);
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
            // Create find options
            FindOptions findOptions = new FindOptions()
                    .projection()
                    .exclude(UserService.getMyFindOptionsArgs(rule));
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
        // Create user document
        User user = new User(
                new ObjectId(),
                userDTO.getUsername(),
                userDTO.getPassword(),
                UserPropertyService.getDefaultUserProperty(),
                UserProfileService.getDefaultProfile(),
                UserRightService.getDefaultRightList()
        );
        // Generate JWT token
        String token = JsonWebToken.encode(user);
        // Get tokens list
        List<String> tokens = Arrays.asList(token);
        // Set issued tokens list
        user.setIssuedTokens(tokens);
        // Save user document in database
        datastore.save(user);
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
     * @return user document
     */
    public static User logoutUser(Request request, Datastore datastore, RuleDTO rule) {
        // Not include in token value
        final int NOT_IN_LIST = -1;
        // Get user document from database (full document)
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
            // Check token index on list contains
            // If token index not contains in list - return null.
            // If token index contains in list - save changes, and return
            // saved document from database
            if (tokenIndex != NOT_IN_LIST) {
                // Remove from issued tokens list token from request
                user.getIssuedTokens().remove(tokenIndex);
                // Save changes in database
                datastore.save(user);
                // Crate find options
                FindOptions findOptions = new FindOptions()
                        .projection()
                        .exclude(UserService.getMyFindOptionsArgs(rule));
                // Get user document by user id with find options with rule excluded fields
                return UserService.getUserById(user.getPureId(), datastore, findOptions);
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
     * @param rule user rule for this action
     * @return list of users documents
     */
    public static List<User> getList(Request request, Datastore datastore, RuleDTO rule) {
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
        // Create find options
        FindOptions findOptions = new FindOptions()
                    .projection()
                    .exclude(UserService.getOtherFindOptionsArgs(rule))
                    .skip(skip)
                    .limit(limit);
        // Return result as list of users document
        return datastore
                .find(User.class)
                .filter(eq("status", "active"))
                .iterator(findOptions)
                .toList();
    }

    /**
     * Method for get user list without rule
     * @param request Spark request object
     * @param datastore datastore (morphia connection)
     * @return list of users documents
     */
    public static List<User> getList(Request request, Datastore datastore) {
        return UserService.getList(request, datastore, null);
    }
}
