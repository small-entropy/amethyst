package synthwave.controllers.base;

import platform.controllers.BaseController;
import platform.constants.DefaultRights;
import platform.constants.ResponseMessages;

/**
 * Base class for build controller for work with tags collection
 * @author small-entropy
 */
public class BaseTagsController extends BaseController {
    protected static final String RIGHT = DefaultRights.TAGS.getName();
    
    protected static final String MSG_LIST = ResponseMessages.TAGS.getMessage();
    protected static final String MSG_ENTITY = ResponseMessages.TAG.getMessage();
    protected static final String MSG_CREATED = ResponseMessages.TAG_CREATED.getMessage();
    protected static final String MSG_UPDATED = ResponseMessages.TAG_UPDATED.getMessage();
    protected static final String MSG_DELETED = ResponseMessages.TAG_DELETED.getMessage();
}
