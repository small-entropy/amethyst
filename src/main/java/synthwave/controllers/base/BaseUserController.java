package synthwave.controllers.base;

import platform.constants.DefaultRights;
import platform.constants.ResponseMessages;

/**
 * Base class for user controllers
 * @author small-entropy
 */
public class BaseUserController extends BaseController {
    protected static final String RULE = DefaultRights.USERS.getName();
    
    protected static final String MSG_LIST = ResponseMessages.USERS_LIST.getMessage();
    protected static final String MSG_ENTITY =  ResponseMessages.USER.getMessage();
    protected static final String MSG_DELTED = ResponseMessages.USER_DEACTIVATED.getMessage();
}
