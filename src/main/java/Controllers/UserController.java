package Controllers;
// Import models
import Models.User;
// Import JWT classes
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
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

import static dev.morphia.query.experimental.filters.Filters.eq;

public class UserController {

    /** Salt for generate password hash */
    private static final String salt = "super#@!$ecretSa|t";

    /**
     * Method for generate user token
     * */
    private static String getToken(User user) {
        Algorithm algorithm = Algorithm.HMAC256(UserController.salt);
        return JWT.create()
                .withClaim("id", user.get_id())
                .withClaim("username", user.getUsername())
                .withIssuer("auth0")
                .sign(algorithm);
    }

    private static DecodedJWT decodeToken(String token) {
        return JWT.decode(token);
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
     * Method to autologin by token in header ot query params
     * @param request Spark request object
     * @param response Spark response object
     * @param datastore datastore to work with data (Morphia connection)
     * @return result auth user
     */
    public static User autoLoginUser(Request request, Response response, Datastore datastore) {
        // Field to headers
        final String HEADER_FIELD = "Authorization";
        // Field to query
        final String QUERY_FIELD = "token";
        // Separator to query string
        final String QUERY_SEPARATOR = " ";
        // Query array index
        final short QUERY_INDEX = 1;
        // Get value from header
        String header = (request.headers(HEADER_FIELD) != null)
                    ? request.headers(HEADER_FIELD).split(QUERY_SEPARATOR)[QUERY_INDEX]
                    : null;
        // Get value from query
        String queryParam = request.queryMap().get(QUERY_FIELD).value();
        // Check on exist token in header or query params
        if (header != null || queryParam != null) {
            // Chose value to decode
            String toDecode = (header != null) ? header : queryParam;
            // Try decode token
            DecodedJWT decoded = UserController.decodeToken(toDecode);
            // Get id as String from claim
            String idString = decoded.getClaim("id").asString();
            // Get ObjectId from id in claim
            ObjectId id = (idString != null) ? new ObjectId(idString) : null;
            // Find in datastore & return result
            return datastore.find(User.class).filter(eq("_id", id)).first();
        } else {
            return null;
        }
    }

    /**
     * Method for auth user
     * @param request Spark request object
     * @param response Spark response object
     * @param datastore datastore (morphia connection)
     * @return user document
     */
    public static User loginUser(Request request, Response response, Datastore datastore) {
        // Transform JSON object from body to Map
        var map = new Gson().fromJson(request.body(), Map.class);
        // Get field "password" from map
        String password = (String) map.get("password");
        // Get field "username" from map
        String username = (String) map.get("username");
        // Find user by username from request body
        User user = datastore.find(User.class).filter(eq("username", username)).first();
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
            String token = (tokens.size() == 0)
                    ? UserController.getToken(user)
                    : tokens.get(0);
            // Set token in header
            response.header("Authorization", UserController.getAuthHeaderValue(token));
            // Return user object
            return user;
        } else {
            // Return null
            return null;
        }
    }

    /**
     * Method for register user
     * @param request Spark request object
     * @param response Spark response object
     * @param datastore datastore (morphia connection)
     * @return user document
     */
    public static User registerUser(Request request, Response response, Datastore datastore) {
        // Crete user object from JSON
        User user = new Gson().fromJson(request.body(), User.class);
        // Regenerate password hash
        user.reGeneratePassword();
        // Save user
        datastore.save(user);
        // Generate JWT token
        String token = UserController.getToken(user);
        // Get tokens list
        List<String> tokens = Arrays.asList(token);
        // Set issued tokens list
        user.setIssuedTokens(tokens);
        // Update document in datastore
        datastore.save(user);
        // Set token in headers
        response.header("Authorization", UserController.getAuthHeaderValue(token));
        // Return saved document
        return user;
    }

    /**
     * Method for get user list
     * @param request Spark request object
     * @param response Spark response object
     * @param datastore datastore (morphia connection)
     * @return list of users documents
     */
    public static List<User> getList(Request request, Response response, Datastore datastore) {
        // Set default values for some params
        final int DEFAULT_SKIP = 0;
        final int DEFAULT_LIMIT = 10;
        final String SKIP_FIELD = "skip";
        final String LIMIT_FIELD = "limit";

        // Set skip value from request query
        String qSkip = request.queryMap().get(SKIP_FIELD).value();
        int skip = (qSkip == null) ? DEFAULT_SKIP : Integer.parseInt(qSkip);
        // Set limit value from request query
        String qLimit = request.queryMap().get(LIMIT_FIELD).value();
        int limit = (qLimit == null) ? DEFAULT_LIMIT : Integer.parseInt(qLimit);
        // Create find options for iterator
        FindOptions findOptions = new FindOptions().skip(skip).limit(limit);
        // Return result as list of users document
        return datastore.find(User.class).iterator(findOptions).toList();
    }
}
