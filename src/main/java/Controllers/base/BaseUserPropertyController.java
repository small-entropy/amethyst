package Controllers.base;

import Controllers.core.v1.AbstractController;
import Utils.constants.DefaultRights;
import Utils.constants.ResponseMessages;

/**
 * Base class for user property controller
 * @author small-entropy
 */
public class BaseUserPropertyController extends AbstractController {
    
    protected static final String RULE = DefaultRights.USERS.getName();
    
    protected static final String MSG_LIST = ResponseMessages.USER_PROPERTIES.getMessage();
    protected static final String MSG_CREATED = ResponseMessages.USER_PROPERTY_CREATED.getMessage();
    protected static final String MSG_ENTITY = ResponseMessages.USER_PROPERTY.getMessage();
    protected static final String MSG_UPDATED = ResponseMessages.USER_PROPERTY_UPDATED.getMessage();
    protected static final String MSG_DELETED = ResponseMessages.USER_PROPETY_DELETED.getMessage();
    
}
