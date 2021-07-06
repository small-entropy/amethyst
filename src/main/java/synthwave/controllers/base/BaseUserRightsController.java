package synthwave.controllers.base;

import platform.controllers.BaseController;
import platform.constants.DefaultRights;
import platform.constants.ResponseMessages;

/**
 * Base class for user rights controller
 * @author small-entropy
 */
public class BaseUserRightsController extends BaseController {
    protected final static String RULE = DefaultRights.USERS.getName();
    
    protected final static String MSG_LIST = ResponseMessages.RIGHTS.getMessage();
    protected final static String MSG_CREATED = ResponseMessages.RIGHT_CREATED.getMessage();
    protected final static String MSG_ENTITY = ResponseMessages.RIGHT.getMessage();
    protected final static String MSG_UPDATED = ResponseMessages.RIGHT_UPDATED.getMessage();
    protected final static String MSG_DELETED = ResponseMessages.RIGHT_DELETED.getMessage();
}
