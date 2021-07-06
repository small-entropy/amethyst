package synthwave.controllers.base;

import platform.constants.DefaultRights;
import platform.constants.ResponseMessages;

/**
 * Base class for companies controllers
 * @author small-entropy
 */
public class BaseCompaniesController extends BaseController {
    protected static String RIGHT = DefaultRights.COMPANIES.getName();
    
    protected static final String MSG_LIST = ResponseMessages.COMPANIES.getMessage();
    protected static final String MSG_ENTITY = ResponseMessages.COMPANY.getMessage();
    protected static final String MSG_CREATED = ResponseMessages.COMPANY_CREATED.getMessage();
    protected static final String MSG_UPDATED = ResponseMessages.COMPANY_UPDATED.getMessage();
    protected static final String MSG_DELETED = ResponseMessages.COMPANY_DELETED.getMessage();
}
