package Controllers.base;

import Controllers.core.v1.AbstractController;
import Utils.constants.DefaultRights;
import Utils.constants.ResponseMessages;

/**
 *
 * @author small-entropy
 */
public class BaseUserController extends AbstractController {
    protected static final String RULE = DefaultRights.USERS.getName();
    
    protected static final String MSG_LIST = ResponseMessages.USERS_LIST.getMessage();
    protected static final String MSG_ENTITY =  ResponseMessages.USER.getMessage();
    protected static final String MSG_DELTED = ResponseMessages.USER_DEACTIVATED.getMessage();
}
