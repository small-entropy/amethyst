package Services.core;

import DTO.RuleDTO;
import DTO.UserDTO;
import Exceptions.DataException;
import Exceptions.TokenException;
import Models.User;
import Utils.common.*;
import Utils.constants.UsersParams;
import com.google.gson.Gson;
import dev.morphia.Datastore;
import dev.morphia.query.FindOptions;
import org.bson.types.ObjectId;
import spark.Request;

import java.util.Arrays;
import java.util.List;

import static dev.morphia.query.experimental.filters.Filters.and;
import static dev.morphia.query.experimental.filters.Filters.eq;

public abstract class CoreUserService extends CoreService {

    // Fields for only public field by rights for users collection
    public final static String[] PUBLIC_ALLOWED = new String[]{ "issuedToken", "password", "properties", "status", "rights", "version" };
    // Fields for public and private fields by rights for users collection
    public final static String[] PUBLIC_AND_PRIVATE_ALLOWED = new String[]{ "status", "password", "version" };
    // Fields for all fields by rights for users collection
    public final static String[] ALL_ALLOWED = new String[]{};

    /**
     * Method for create user data transfer object from request body
     * @param request Spark request object
     * @return user data transfer object
     */
    protected static UserDTO getUserDtoFromBody(Request request) {
        return new Gson().fromJson(request.body(), UserDTO.class);
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
        return CoreUserService.getUserById(id, datastore, findOptions);
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
                .exclude(CoreUserService.ALL_ALLOWED);
        return CoreUserService.getUserById(paramId, datastore, findOptions);
    }

    public static User getUserById(ObjectId id, Datastore datastore) {
        FindOptions findOptions = new FindOptions()
                .projection()
                .exclude(CoreUserService.ALL_ALLOWED);
        return CoreUserService.getUserById(id, datastore, findOptions);
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
            String idParams = request.params(UsersParams.ID.getName());
            // Generate Object ID from string
            ObjectId id = new ObjectId(idParams);
            // Get find options wiwth all fields
            FindOptions findOptions = new FindOptions()
                    .projection()
                    .exclude(CoreUserService.ALL_ALLOWED);
            // Get user document from database
            User user = CoreUserService.getUserById(id, datastore, findOptions);
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
            return CoreUserService.getUserById(id, datastore, findOptions);
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
                .exclude(CoreUserService.ALL_ALLOWED);
        // Return user document
        return CoreUserService.getUserByToken(request, datastore, findOptions);
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
                .exclude(CoreUserService.ALL_ALLOWED);
        return CoreUserService.getUserByUsername(userDTO, datastore, findOptions);
    }

    /**
     * Method for find user by username from request
     * Method return document with all fields
     * @param request Spark request object
     * @param datastore Morphia datastore object
     * @return user document
     */
    public static User getUserByUsername(Request request, Datastore datastore) {
        UserDTO userDTO = CoreUserService.getUserDtoFromBody(request);
        return CoreUserService.getUserByUsername(userDTO, datastore);
    }

    /**
     * Method fo find user by username from user data transfer object.
     * In this method user document can exclude fields by find options
     * @param userDTO user data transfer object
     * @param datastore Morphia datastore object
     * @param findOptions find options object
     * @return user document
     */
    protected static User getUserByUsername(UserDTO userDTO, Datastore datastore, FindOptions findOptions) {
        return datastore.find(User.class)
                .filter(and(
                        eq("username", userDTO.getUsername()),
                        eq("status", "active")
                ))
                .first(findOptions);
    }

    /**
     * Method for update user document
     * @return updated user document
     */
    protected static String updateUser() {
        return "Update user data";
    }

    protected static List<User> getList(int skip, int limit, String[] excludes, Datastore datastore) {
        FindOptions findOptions = new FindOptions()
                .projection()
                .exclude(excludes)
                .skip(skip)
                .limit(limit);
        return datastore
                .find(User.class)
                .filter(eq("status", "active"))
                .iterator(findOptions)
                .toList();
    }
}
