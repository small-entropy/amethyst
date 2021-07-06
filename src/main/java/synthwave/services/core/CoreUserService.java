package synthwave.services.core;

import platform.utils.helpers.Comparator;
import platform.utils.access.jwt.JsonWebToken;
import platform.utils.helpers.HeadersUtils;
import platform.utils.helpers.ParamsManager;
import platform.utils.helpers.QueryManager;
import synthwave.dto.RuleDTO;
import synthwave.dto.UserDTO;
import platform.exceptions.DataException;
import platform.exceptions.TokenException;
import synthwave.filters.UsersFilter;
import synthwave.models.mongodb.standalones.User;
import synthwave.repositories.mongodb.v1.UsersRepository;
import synthwave.services.base.BaseService;
import platform.utils.access.v1.RightManager;
import com.google.gson.Gson;
import dev.morphia.Datastore;
import java.util.List;
import org.bson.types.ObjectId;
import spark.Request;

public abstract class CoreUserService 
        extends BaseService<UsersRepository> {
    
    public CoreUserService(
            Datastore datastore,
            String[] globalExcludes,
            String[] publicExcludes,
            String[] privateExcludes
    ) {
        super(
                new UsersRepository(datastore),
                globalExcludes,
                publicExcludes,
                privateExcludes
        );
    }
    
    protected RuleDTO getRule(
            Request request,
            String right,
            String action
    ) {
        return RightManager.getRuleByRequest_Token(
                request, 
                getRepository(), 
                right, 
                action
        );
    }

    /**
     * Method for create user data transfer object from request body
     * @param request Spark request object
     * @return user data transfer object
     */
    protected UserDTO getUserDtoFromBody(Request request) {
        return new Gson().fromJson(request.body(), UserDTO.class);
    }
    
    /**
     * Method for get user, if ID as String
     * @param userId user id
     * @return 
     */
    public User getUserById(ObjectId userId) {
        return getUserById(userId, getGlobalExcludes());
    }
    
    /**
     * Method for get user by id, if id as String
     * @param userId user id
     * @param excludes excludes fileds
ument
     */
    public User getUserById(ObjectId userId, String[] excludes) {
        UsersFilter filter = new UsersFilter(userId, excludes);
        return getUserById(filter);
    }
    
    /**
     * Method for get user by id with created filter
     * @param filter filter object
     * @return 
     */
    public User getUserById(UsersFilter filter) {
        return getRepository().findOneById(filter);
    }
    
    /**
     * Method for get user document, when send token in request 
     * headers or request params
     * @param request Spark request object
     * @return user document
     * @throws TokenException exception for errors with user token
     * @throws DataException  exception for errors with founded data
     */
    public User getUserWithTrust(Request request) 
            throws TokenException, DataException 
    {
        // Result of check on trust
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        // Check trust result.
        // If token and URL have a equals user ID - get full user document.
        // If token and URL haven't equals user ID - throw error.
        if (isTrusted) {
            // User id from URL params
            ObjectId userId = ParamsManager.getUserId(request);
            // Get user document from database
            User user = getUserById(userId);
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
     * @param filter filter object
     * @return user object
     */
    public User getUserByToken(
            Request request,
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
            return getUserById(filter);
        } else {
            return null;
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
            return usersRepository.findOneById(filter);
        } else {
            return null;
        }
    }
    
    /**
     * Method for find user by username from request
     * Method return document with all fields
     * @param request Spark request object
     * @return user document
     */
    public User getUserByUsername(Request request) {
        UserDTO userDTO = getUserDtoFromBody(request);
        return getUserByUsername(userDTO);
    }
    
    /**
     * Method for get user by username with user data transfer object & source
     * @param userDTO user data transfer obejct
     * @param usersRepository user data source
     * @return user document
     */
    public User getUserByUsername(UserDTO userDTO) {
        UsersFilter filter = new UsersFilter(
                userDTO.getUsername(), 
                getGlobalExcludes()
        );
        return getRepository().findOneByUsername(filter);
    }
    
    public static User getUserByUsername(
            Request request,
            UsersRepository usersRepository
    ) {
        UserDTO userDTO = new Gson().fromJson(request.body(), UserDTO.class);
        UsersFilter filter = new UsersFilter(
                userDTO.getUsername(), 
                new String[] {}
        );
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
    protected List<User> getList(
            int skip, 
            int limit, 
            String[] excludes
    ) {
        UsersFilter filter = new UsersFilter(skip, limit, excludes);
        return getRepository().findAll(filter);
    }
}
