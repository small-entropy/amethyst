package synthwave.controllers.base;

import platform.constants.DefaultRights;
import platform.constants.ResponseMessages;

/**
 * Base class for user profile controller
 * @author small-entropy
 */
public class BaseUserProfileController extends BaseController {
    protected static final String RIGHT = DefaultRights.USERS.getName();
    
    protected static final String MSG_LIST = ResponseMessages.PROFILE.getMessage();
    protected static final String MSG_CREATED = ResponseMessages.PROFILE_CREATED.getMessage();
    protected static final String MSG_ENTITY = ResponseMessages.PROFILE_PROPERTY.getMessage();
    protected static final String MSG_UPDATED = ResponseMessages.PROFILE_UPDATED.getMessage();
    protected static final String MSG_DELETED = ResponseMessages.PROFILE_DELETED.getMessage();
}
