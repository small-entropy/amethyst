package Controllers.base;

import Controllers.core.v1.AbstractController;
import Utils.constants.DefaultRights;
import Utils.constants.ResponseMessages;

/**
 *
 * @author small-entropy
 */
public class BaseCatalogsController extends AbstractController {
    protected static final String RIGHT = DefaultRights.CATALOGS.getName();
    
    protected static final String MSG_LIST = ResponseMessages.CATALOGS_LIST.getMessage();
    protected static final String MSG_ENTITY = ResponseMessages.CATALOG.getMessage();
    protected static final String MSG_CREATED = ResponseMessages.CATALOG_CREATED.getMessage();
    protected static final String MSG_UPDATED = ResponseMessages.CATALOG_UPDATED.getMessage();
    protected static final String MSG_DELETED = ResponseMessages.CATALOG_DELETED.getMessage();
}
