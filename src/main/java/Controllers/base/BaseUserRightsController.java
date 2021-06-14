package Controllers.base;

import Controllers.core.v1.AbstractController;
import Utils.constants.DefaultRights;
import Utils.constants.ResponseMessages;

/**
 * Base class for user rights controller
 * @author small-entropy
 */
public class BaseUserRightsController extends AbstractController {
    protected final static String RULE = DefaultRights.USERS.getName();
    
    protected final static String MSG_LIST = ResponseMessages.RIGHTS.getMessage();
    protected final static String MSG_CREATED = ResponseMessages.RIGHT_CREATED.getMessage();
    protected final static String MSG_ENTITY = ResponseMessages.RIGHT.getMessage();
    protected final static String MSG_UPDATED = ResponseMessages.RIGHT_UPDATED.getMessage();
    protected final static String MSG_DELETED = ResponseMessages.RIGHT_DELETED.getMessage();
}
