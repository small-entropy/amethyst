package Services.core;

import DataTransferObjects.v1.UserDTO;
import Exceptions.DataException;
import Exceptions.TokenException;
import Filters.UsersFilter;
import Models.Standalones.User;
import Repositories.v1.UsersRepository;
import Utils.common.*;
import com.google.gson.Gson;
import java.util.List;
import org.bson.types.ObjectId;
import spark.Request;

public abstract class CoreUserService extends CoreService {

    // Fields for only public field by rights for users collection
    public final static String[] PUBLIC_ALLOWED = new String[]{ 
        "issuedToken", 
        "password", 
        "properties", 
        "status", "rights", 
        "version" 
    };
    // Fields for public and private fields by rights for users collection
    public final static String[] PUBLIC_AND_PRIVATE_ALLOWED = new String[]{ 
        "status", 
        "password", 
        "version" 
    };
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
     * Method for get user, if ID as String
     * @param idString id user ID as staing
     * @param usersRepository source for work with users collection
     * @return 
     */
    public static User getUserById(
            String idString, 
            UsersRepository usersRepository
    ) {
        ObjectId id = new ObjectId(idString);
        return getUserById(id, usersRepository, ALL_ALLOWED);
    }
    
    /**
     * Method for get user by id, if id as String
     * @param idString id as string
     * @param excludes excludes fileds
     * @param usersRepository source for work with users collection
     * @return founded user document
     */
    public static User getUserById(
            String idString, 
            String[] excludes, 
            UsersRepository usersRepository
    ) {
        ObjectId id = new ObjectId(idString);
        return getUserById(id, usersRepository, excludes);
    }

    /**
     * Method for get user by id, if id as ObjectId
     * @param id user id
     * @param usersRepository source for work with users collection
     * @param excludes exludes fields
     * @return user document
     */
    public static User getUserById(
            ObjectId id, 
            UsersRepository usersRepository, 
            String[] excludes
    ) {
        UsersFilter filter = new UsersFilter(id, excludes);
        return getUserById(filter, usersRepository);
    }
    
    /**
     * Method for get user by id with created filter
     * @param filter filter object
     * @param usersRepository users source of data object
     * @return 
     */
    public static User getUserById(
            UsersFilter filter, 
            UsersRepository usersRepository
    ) {
        return usersRepository.findOneById(filter);
    }
    
    /**
     * Method for get user document, when send token in request headers or request params
     * @param request Spark request object
     * @param usersRepository users data source
     * @return user document
     * @throws TokenException exception for errors with user token
     * @throws DataException  exception for errors with founded data
     */
    public static User getUserWithTrust(
            Request request, 
            UsersRepository usersRepository
    ) throws TokenException, DataException {
        // Result of check on trust
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        // Check trust result.
        // If token and URL have a equals user ID - get full user document.
        // If token and URL haven't equals user ID - throw error.
        if (isTrusted) {
            // User id from URL params
            String idParam = ParamsManager.getUserId(request);
            // Get user document from database
            User user = CoreUserService.getUserById(idParam, usersRepository);
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
     * @param usersRepository users data source
     * @param filter filter object
     * @return user object
     */
    public static User getUserByToken(
            Request request,
            UsersRepository usersRepository,
            UsersFilter filter
    ) {
        // Get token from request headers
        String header = HeadersUtils.getTokenFromHeaders(request);
        // Get token from request query params
        String queryParam = QueryManager.getTokenFromQuery(request);
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
            filter.setId(id);
            return getUserById(filter, usersRepository);
        } else {
            return null;
        }
    }
    
    /**
     * Method for find user by username from request
     * Method return document with all fields
     * @param request Spark request object
     * @param usersRepository source for work with users collection
     * @return user document
     */
    public static User getUserByUsername(
            Request request, 
            UsersRepository usersRepository
    ) {
        UserDTO userDTO = getUserDtoFromBody(request);
        return getUserByUsername(userDTO, usersRepository);
    }
    
    /**
     * Method for get user by username with user data transfer object & source
     * @param userDTO user data transfer obejct
     * @param usersRepository user data source
     * @return user document
     */
    public static User getUserByUsername(
            UserDTO userDTO, 
            UsersRepository usersRepository
    ) {
        UsersFilter filter = new UsersFilter(userDTO.getUsername(), ALL_ALLOWED);
        return usersRepository.findOneByUsername(filter);
    }

    /**
     * Method for get list from datastore
     * @param skip skiped documents
     * @param limit limit of documents
     * @param excludes excludes fields
     * @param usersRepository source of data
     * @return list with user documents
     */
    protected static List<User> getList(
            int skip, 
            int limit, 
            String[] excludes, 
            UsersRepository usersRepository
    ) {
        UsersFilter filter = new UsersFilter(skip, limit, excludes);
        return usersRepository.findAll(filter);
    }
}
