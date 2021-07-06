package synthwave.controllers.base;

import platform.controllers.BaseController;
import platform.constants.DefaultRights;
import platform.constants.ResponseMessages;

/**
 * Base class for products controllers
 * @author small-entropey
 */
public class BaseProductsController extends BaseController {
    protected static final String RULE = DefaultRights.PRODUCTS.getName();

    protected static final String MSG_LIST = ResponseMessages.PRODUCTS.getMessage();
    protected static final String MSG_ENTITY = ResponseMessages.PRODUCT.getMessage();
    protected static final String MSG_CREATED = ResponseMessages.PRODUCT_CREATED.getMessage();
    protected static final String MSG_UPDATED = ResponseMessages.PRODUCT_UPDATED.getMessage();
    protected static final String MSG_DELETED = ResponseMessages.PRODUCT_DELETED.getMessage();

}
