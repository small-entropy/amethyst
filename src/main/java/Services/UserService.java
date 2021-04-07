package Services;
// Import models
import DTO.UserDTO;
import Exceptions.TokenException;
import Models.User;
// Import utils for request headers
import Utils.HeadersUtils;
// Import utils for work with JWT
import Utils.JsonWebToken;
// Import utils for work with query params
import Utils.QueryUtils;
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
     * @return updated user docukent
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
    public static User markToRemove(Request request, Datastore datastore) throws TokenException {
        // Get token from request
        String token = UserService.getTokenByRequest(request);
        // Check token on exist
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
            // If ids not equals - return null
            if (isEqualsIds) {
                ObjectId id = new ObjectId(paramId);
                User user = datastore
                        .find(User.class)
                        .filter(eq("id", id))
                        .first();
                if (user != null) {
                    user.deactivate();
                    datastore.save(user);
                    return user;
                } else {
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
     * @return finded user document
     */
    public static User getUserByUuid(Request request, Datastore datastore) {
        String token = UserService.getTokenByRequest(request);
        DecodedJWT decoded = (token != null) ? JsonWebToken.decode(token) : null;
        String decodedIdString = (decoded != null) ? decoded.getClaim("id").asString() : null;
        String idString = request.params("id");
        boolean isEqualsToken = decodedIdString != null && decodedIdString.equals(idString);
        ObjectId id = new ObjectId(idString);
        FindOptions findOptions = isEqualsToken
                ? new FindOptions()
                : new FindOptions()
                    .projection()
                    .exclude("issuedToken", "password");
        return datastore
                .find(User.class)
                .filter(and(
                        eq("id", id),
                        eq("active", true)
                ))
                .first(findOptions);
    }

    /**
     * Method for login user by token
     * @param request Spark request object
     * @param datastore datastore (Morphia connection)
     * @return user object
     */
    private static User getUserByToken(Request request, Datastore datastore) {
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
                        eq("active", true)
                    ))
                    .first();
        } else {
            return null;
        }
    }

    /**
     * Method for get token from headers or query params
     * @param request Spark request object
     * @return token from headers or query
     */
    private static String getTokenByRequest(Request request) {
        String header = HeadersUtils.getTokenFromHeaders(request);
        String queryParam = QueryUtils.getTokenFromQuery(request);
        if (header != null || queryParam != null) {
            return (header != null) ? header : queryParam;
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
        String token = UserService.getTokenByRequest(request);
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
                        eq("active", true)
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
        // Update document in datastore
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
        User user = UserService.getUserByToken(request, datastore);
        if (user != null) {
            String token = UserService.getTokenByRequest(request);
            int tokenIndex = user.getIssuedTokens().indexOf(token);
            user.getIssuedTokens().remove(tokenIndex);
            datastore.save(user);
            return user;
        } else {
            return null;
        }
    }

    /**
     * Method for get user list
     * @param request Spark request object
     * @param datastore datastore (morphia connection)
     * @return list of users documents
     */
    public static List<User> getList(Request request, Datastore datastore) {
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
        // Create find options for iterator
        FindOptions findOptions = new FindOptions()
                .projection()
                .exclude("issuedToken", "password")
                .skip(skip)
                .limit(limit);
        // Return result as list of users document
        return datastore
                .find(User.class)
                .filter(
                        eq("active", true)
                )
                .iterator(findOptions)
                .toList();
    }
}
