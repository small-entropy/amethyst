package Services;
// Import models
import Models.User;
// Import JWT classes
import Utils.JsonWebToken;
import Responses.StandardResponse;
// Import GSON class
import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.gson.Gson;
// Import Morphia classes
import dev.morphia.Datastore;
import dev.morphia.query.FindOptions;
// Import Spark classes
import org.bson.types.ObjectId;
import spark.Request;
import spark.Response;
// Import Java standard classes
import java.util.Arrays;
import java.util.List;
import java.util.Map;
// Import Morphia filter criteria methods
import static dev.morphia.query.experimental.filters.Filters.eq;
import static dev.morphia.query.experimental.filters.Filters.and;

/**
 * Class service for work with users collection
 */
public class UserService {

    /** Default header Authorization field */
    private static final String headerAuthField = "Authorization";

    /** Default query Authorization key */
    private static final String queryAuthKey = "token";

    /** Default header auth separator */
    private static final String headerSeparator = " ";

    /** Default header index */
    private static final short headerAuthIndex = 1;

    /**
     * Method get token from
     * @param request Spark request object
     * @return token from header
     */
    private static String getTokenFromHeaders(Request request){
        return (request.headers(UserService.headerAuthField) != null)
                ? request.headers(UserService.headerAuthField).split(UserService.headerSeparator)[UserService.headerAuthIndex]
                : null;
    }

    /**
     * Method for get token from query
     * @param request Spark request object
     * @return token from query string
     */
    private static String getTokenFromQuery(Request request) {
        return request.queryMap().get(UserService.queryAuthKey).value();
    }

    /**
     * Method for login user by token
     * @param request Spark request object
     * @param datastore datastore (Morphia connection)
     * @return user object
     */
    private static User getUserByToken(Request request, Datastore datastore) {
        // Get token from request headers
        String header = UserService.getTokenFromHeaders(request);
        // Get token from request query params
        String queryParam = UserService.getTokenFromQuery(request);
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
                        eq("_id", id),
                        eq("active", true)
                    ))
                    .first();
        } else {
            return null;
        }
    }

    /**
     * Method for get auth string for header
     * @param token user token
     * @return string for save in response headers
     */
    private static String getAuthHeaderValue(String token) {
        return "Bearer ".concat(token);
    }

    /**
     * Method for get token from headers or query params
     * @param request Spark request object
     * @return token from headers or query
     */
    private static String getTokenByRequest(Request request) {
        String header = UserService.getTokenFromHeaders(request);
        String queryParam = UserService.getTokenFromQuery(request);
        if (header != null || queryParam != null) {
            return (header != null) ? header : queryParam;
        } else {
            return null;
        }
    }

    /**
     * Method to autologin by token in header ot query params
     * @param request Spark request object
     * @param response Spark response object
     * @param datastore datastore to work with data (Morphia connection)
     * @return result auth user
     */
    public static StandardResponse<User> autoLoginUser(Request request, Response response, Datastore datastore) {
        User user = UserService.getUserByToken(request, datastore);
        String status;
        String message;
        if (user != null) {
            String token = UserService.getTokenByRequest(request);
            if (user.getIssuedTokens() != null
                    && user.getIssuedTokens().contains(token)) {
                status = "success";
                message = "Successfully auth by token";
                return new StandardResponse<User>(status, message, user);
            } else {
                status = "fail";
                message = "This token not active";
                return new StandardResponse<User>(status, message, null);
            }
        } else {
            status = "fail";
            message = "Can not login by token";
            return new StandardResponse<User>(status, message, null);
        }
    }

    /**
     * Method for auth user
     * @param request Spark request object
     * @param response Spark response object
     * @param datastore datastore (morphia connection)
     * @return user document
     */
    public static StandardResponse<User> loginUser(Request request, Response response, Datastore datastore) {
        // Transform JSON object from body to Map
        var map = new Gson().fromJson(request.body(), Map.class);
        // Get field "password" from map
        String password = (String) map.get("password");
        // Get field "username" from map
        String username = (String) map.get("username");
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
            } else {
                token = tokens.get(0);
            }
            // Set token in header
            response.header("Authorization", UserService.getAuthHeaderValue(token));
            // Set value for message & status
            String status = "success";
            String message = "Successfull user auth by token";
            // Return user object
            return new StandardResponse<User>(status, message, user);
        } else {
            String status = "fail";
            String message = "Can not auth by current username & password";
            // Return null
            return new StandardResponse<User>(status, message, null);
        }
    }

    /**
     * Method for register user
     * @param request Spark request object
     * @param response Spark response object
     * @param datastore datastore (morphia connection)
     * @return user document
     */
    public static StandardResponse<User> registerUser(Request request, Response response, Datastore datastore) {
        // Crete user object from JSON
        User user = new Gson().fromJson(request.body(), User.class);
        // Regenerate password hash
        user.reGeneratePassword();
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
        response.header("Authorization", UserService.getAuthHeaderValue(token));
        // Set status, message
        String status = "success";
        String message = "Register user successed";
        // Create & return answer object for method
        return new StandardResponse<User>(status, message, user);
    }

    /**
     * Method for remove user token
     * @param request Spark request object
     * @param response Spark response object
     * @param datastore datastore (Morphia connection)
     * @return user document
     */
    public static StandardResponse<User> logoutUser(Request request, Response response, Datastore datastore) {
        User user = UserService.getUserByToken(request, datastore);
        String message;
        String status;
        if (user != null) {
            String token = UserService.getTokenByRequest(request);
            int tokenIndex = user.getIssuedTokens().indexOf(token);
            user.getIssuedTokens().remove(tokenIndex);
            datastore.save(user);
            status = "success";
            message = "Logout is success";
        } else {
            status = "fail";
            message = "Can not find user by token";
        }

        return new StandardResponse<User>(status, message, user);
    }

    /**
     * Method for get user list
     * @param request Spark request object
     * @param response Spark response object
     * @param datastore datastore (morphia connection)
     * @return list of users documents
     */
    public static StandardResponse<List<User>> getList(Request request, Response response, Datastore datastore) {
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
        List<User> users = datastore
                .find(User.class)
                .filter(
                        eq("active", true)
                )
                .iterator(findOptions)
                .toList();
        // Check users list size.
        // If size equal - fail method status & message
        // If size not equal - success method status & message
        boolean isEmptyUsers = users.size() != 0;
        // Set status, message
        String status = isEmptyUsers ? "success" : "fail";
        String message = isEmptyUsers ? "Success finding users" : "Can not finding users";
        // Create & return answer object for method
        return new StandardResponse<List<User>>(status, message, users);
    }
}
