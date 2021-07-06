package synthwave.controllers.base;

import platform.controllers.BaseController;
import synthwave.dto.v1.RuleDTO;
import synthwave.repositories.mongodb.v1.UsersRepository;
import platform.constants.DefaultRights;
import platform.constants.ResponseMessages;
import platform.utils.access.v1.RightManager;
import spark.Request;

/**
 * Base class for create authorization controllers
 * @author small-entropy
 */
public class BaseAuthorizationController extends BaseController {
    
    protected static final String MSG_REGISTERED = ResponseMessages.REGISTERED.getMessage(); 
    protected static final String MSG_LOGIN = ResponseMessages.LOGIN.getMessage();
    protected static final String MSG_PASSWORD_CHANGED = ResponseMessages.PASSWORD_CHANGED.getMessage();
    protected static final String MSG_AUTOLOGIN = ResponseMessages.AUTOLOGIN.getMessage();
    protected static final String MSG_LOGOUT = ResponseMessages.LOGOUT.getMessage();
    
    protected static final String RULE = DefaultRights.USERS.getName();
    
    /**
     * Method for get rule by username from request body
     * @param request Spark request object
     * @param usersRepository datastore source for users collection
     * @param right right string
     * @param action action name
     * @return 
     */
    protected static RuleDTO getRule_byUsername(
            Request request,
            UsersRepository usersRepository,
            String right,
            String action
    ) {
        return RightManager.getRuleByRequest_Username(
                request, 
                usersRepository, 
                right, 
                action
        );
    }
}
