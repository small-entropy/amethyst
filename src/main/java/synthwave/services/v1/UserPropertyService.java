package synthwave.services.v1;
// Import user model (class)
import platform.exceptions.AccessException;
import platform.exceptions.DataException;
import platform.exceptions.TokenException;
import synthwave.models.mongodb.embeddeds.EmbeddedProperty;
import synthwave.services.core.CoreUserPropertyService;
import dev.morphia.Datastore;
import java.util.Arrays;
import java.util.List;
import spark.Request;

/**
 * Class controller for work with user properties
 */
public class UserPropertyService extends CoreUserPropertyService {
    
    public UserPropertyService(Datastore datastore) {
        super(
                datastore, 
                Arrays.asList("banned")
        );
    }

    /**
     * Method for create user property
     * @param request Spark request object
     * @param propertiesRepository users data source
     * @param rule rule data transfer object
     * @return user property
     * @throws AccessException user access exception
     * @throws DataException throw if some data can not found or 
     *                       can not create user property
     */
    public EmbeddedProperty createUserProperty(
            Request request, 
            String right,
            String action
    ) throws AccessException, DataException {
    	boolean hasAccess = checkHasAccess(request, right, action);
    	if (hasAccess) {
            return createUserProperty(request);
        } else {
            Error error = new Error("Can create user property for user");
            throw new AccessException("CanNotCreate", error);
        }
    }

    /**
     * Method for get user properties
     * @param request Spark request object
     * @return list of user properties
     * @throws TokenException throw if token not valid or not send
     * @throws DataException throw if some data can not found
     * @throws AccessException throw if user han't access to field
     */
    public List<EmbeddedProperty> getUserProperties(
            Request request, 
            String right,
            String action
    ) throws TokenException, DataException, AccessException {
    	boolean hasAccess = checkHasAccess(request, right, action);
        if (hasAccess) {
            return getUserProperties(request);
        } else {
            Error error = new Error("Can not rights for read private fields");
            throw new AccessException("CanNotRead", error);
        }
    }

    /**
     * Method for get user property by id
     * @param request Spark request object
     * @return founded user property
     * @throws AccessException throw if user han't access to field
     * @throws DataException throw if user or property can not found
     */
    public EmbeddedProperty getUserPropertyById(
            Request request, 
            String right,
            String action
    ) throws AccessException, DataException {
    	boolean hasAccess = checkHasAccess(request, right, action);
        if (hasAccess) {
            return getUserPropertyById(request);
        } else {
            Error error = new Error("Can not rights for read private fields");
            throw new AccessException("CanNotRead", error);
        }
    }

    /**
     * Method for update user property
     * @param request Spark request object
     * @return updated user property document
     * @throws AccessException throw if user han't access to field
     * @throws DataException throw if user or property can not found
     */
    public EmbeddedProperty updateProperty(
            Request request, 
            String right,
            String action
    ) throws AccessException, DataException {
    	boolean hasAccess = checkHasAccess(request, right, action);
        if (hasAccess) {
            return updateUserProperty(request);
        } else {
            Error error = new Error("Has no access to update user properties");
            throw new AccessException("CanNotUpdate", error);
        }
    }
    
    /**
     * Method for remove user property fro list by request params with check rule
     * @param request Spark request data
     * @return actual value of user properties
     * @throws AccessException throw if user hasn't access to remove document
     * @throws DataException throw if user or property can not found
     */
    public List<EmbeddedProperty> deleteUserProperty(
            Request request, 
            String right,
            String action
    ) throws AccessException, DataException {
        boolean hasAccess = checkHasAccess(request, right, action);
        if (hasAccess) {
            return deleteUserProperty(request);
        } else {
            Error error = new Error("Has no access to delete user property");
            throw new AccessException("CanNotDelete", error);
        }
    }
}
