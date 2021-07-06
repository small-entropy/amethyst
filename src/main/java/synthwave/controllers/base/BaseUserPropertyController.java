package synthwave.controllers.base;

import platform.controllers.BaseController;
import platform.constants.DefaultRights;
import platform.constants.ResponseMessages;

/**
 * Base class for user property controller
 * @author small-entropy
 */
public class BaseUserPropertyController extends BaseController {
    
    protected static final String RIGHT = DefaultRights.USERS.getName();
    
    protected static final String MSG_LIST = ResponseMessages.USER_PROPERTIES.getMessage();
    protected static final String MSG_CREATED = ResponseMessages.USER_PROPERTY_CREATED.getMessage();
    protected static final String MSG_ENTITY = ResponseMessages.USER_PROPERTY.getMessage();
    protected static final String MSG_UPDATED = ResponseMessages.USER_PROPERTY_UPDATED.getMessage();
    protected static final String MSG_DELETED = ResponseMessages.USER_PROPETY_DELETED.getMessage();
    
}
