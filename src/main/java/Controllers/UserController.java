package Controllers;
// Import models
import Models.User;
// Import JWT classes
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
// Import GSON class
import com.google.gson.Gson;
// Import Morphia classes
import dev.morphia.Datastore;
import dev.morphia.query.FindOptions;
// Import Spark classes
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
    private static String getToken() {
        Algorithm algorithm = Algorithm.HMAC256(UserController.salt);
        return JWT.create().withIssuer("auth0").sign(algorithm);
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
                    ? UserController.getToken()
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
     * @return user documentreturn
     */
    public static User registerUser(Request request, Response response, Datastore datastore) {
        // Create algorithm object
        Algorithm algorithm = Algorithm.HMAC256(UserController.salt);
        // Generate JWT token
        String token = UserController.getToken();
        // Get tokens list
        List<String> tokens = Arrays.asList(token);
        // Crete user object from JSON
        User user = new Gson().fromJson(request.body(), User.class);
        // Regenerate password hash
        user.reGeneratePassword();
        // Set issued tokens list
        user.setIssuedTokens(tokens);
        // Save document in datastore
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
     * @return
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
