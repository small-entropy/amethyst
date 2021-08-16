package synthwave.services.v1.users;

import dev.morphia.Datastore;
import java.util.List;
import org.bson.types.ObjectId;
import spark.Request;

import engine.dto.RuleDTO;
import core.exceptions.DataException;
import core.exceptions.TokenException;
import core.utils.ParamsManager;
import core.utils.QueryManager;

import synthwave.models.morphia.extend.User;
import synthwave.services.core.users.CoreUserService;
import core.utils.Comparator;

/**
 * Class service for work with users collection
 * @author small-entropy
 * @version 1
 */
public class UserService extends CoreUserService {
    
    public UserService(Datastore datastore) {
        super(
                datastore, 
                new String[] {}, 
                new String[] {
                    "issuedToken", 
                    "password", 
                    "properties", 
                    "rights", 
                    "version",
                    "status"
                },
                new String[] { "password", "properties", "status" }
        );
    }

    /**
     * Method for deactivate user account
     * @param request Spark request object
     * @param usersRepository
     * @return user object after saving
     * @throws TokenException
     * @throws DataException
     */
    public User deleteEntity(Request request) throws DataException {
        ObjectId userId = ParamsManager.getUserId(request);
        User user = getUserById(userId);
        user.deactivate();
        getRepository().save(user);
        return user;
    }

    /**
     * Method for get user information by UUID.If user send token 
     * - get full document.If user not send token - get short document
     * @param request Spark request object
     * @param usersRepository
     * @return founded user document
     * @throws DataException
     */
    public User getEntityById(Request request) throws  DataException {
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        ObjectId userId = ParamsManager.getUserId(request);
        String[] excludes = isTrusted
                ? getPrivateExcludes()
                : getGlobalExcludes();
        User user = getUserById(userId, excludes);
        if (user != null) {
            return user;
        } else {
            Error error = new Error("Can not find user by id");
            throw new DataException("NotFound", error);
        }
    }
    
    @Override
    public List<User> getEntitiesList(
            Request request, 
            String right,
            String action
    ) throws DataException {
        int skip = QueryManager.getSkip(request);
        int limit = QueryManager.getLimit(request);
        RuleDTO rule = getRule(request, right, action);
        String[] excludes = getExcludes(rule, true);
        return getList(skip, limit, excludes);
    }
}
