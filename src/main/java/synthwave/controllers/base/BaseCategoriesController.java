/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package synthwave.controllers.base;

import platform.controllers.BaseController;
import platform.constants.DefaultRights;
import platform.constants.ResponseMessages;

/**
 * Base class for categories controllers
 * @author small-entropy
 */
public class BaseCategoriesController extends BaseController {
    /** Property with name of right */
    protected static final String RIGHT = DefaultRights.CATEGORIES.getName();
    
    /** Property with message for success get categories */
    protected static final String MSG_LIST = ResponseMessages.CATEGORIES.getMessage();
    /** Property with message for success create category document */
    protected static final String MSG_CREATED = ResponseMessages.CATEGORY_CREATED.getMessage();
    /** Property with message for success get category document */
    protected static final String MSG_ENTITY = ResponseMessages.CATEGORY.getMessage();
    /** Property with emssage for scucess update category document */
    protected static final String MSG_UPDATED = ResponseMessages.CATEGORY_UPDATED.getMessage();
    protected static final String MSG_DELETED = ResponseMessages.CATEGORY_DELETED.getMessage();
}