/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package platform.request;

import platform.exceptions.DataException;
import platform.constants.RequestParams;
import org.bson.types.ObjectId;
import spark.Request;

/**
 * Class for work with request params
 * @author small-entropy
 */
public class ParamsManager {
    
    /**
     * Method for get ObjectId from request string
     * @param request Spark request object
     * @param key name of request param
     * @param errorMessage error message
     * @return founded param
     * @throws DataException throw if param not found in request
     */
    private static ObjectId getObjectIdFromRequest(
            Request request,
            String key,
            String errorMessage
    ) throws DataException {
        String value = request.params(key);
        if (value != null) {
            return new ObjectId(value);
        } else {
            Error error = new Error(errorMessage);
            throw new DataException("NotSendParams", error);
        }
    }
    
    /**
     * Method for get user id from request
     * @param request Spark request object
     * @return founded user id
     * @throws DataException throw if param not found in request
     */
    public static final ObjectId getUserId(Request request) 
            throws DataException 
    {
        String key = RequestParams.USER_ID.getName();
        String errorMessage = "Incorrect user id in request";
        return getObjectIdFromRequest(request, key, errorMessage);
    }
    
    /**
     * Method for get category id from request params
     * @param request Spark request object
     * @return founded param
     * @throws DataException throw if param not found in request 
     */
    public static final ObjectId getCategoryId(Request request) 
            throws DataException 
    {
        String key = RequestParams.CATAGORY_ID.getName();
        String errorMessage = "Incorrect category id in request";
        return getObjectIdFromRequest(request, key, errorMessage);
    }
    
    /**
     * Method for get catalog id from request params
     * @param request Spark request object
     * @return founded param
     * @throws DataException throw if param not found in request 
     */
    public static final ObjectId getCatalogId(Request request) 
            throws DataException 
    {
        String key = RequestParams.CATALOG_ID.getName();
        String errorMessage = "Incorrect catalog id in request";
        return getObjectIdFromRequest(request, key, errorMessage);
    }
    
    /**
     * Method for get right id from requst params
     * @param request Spark request object
     * @return founded param
     * @throws DataException throw if param not found in request
     */
    public static final ObjectId getRightId(Request request) 
            throws DataException
    {
        String key = RequestParams.RIGHT_ID.getName();
        String errorMessage = "Incorrect right id in request";
        return getObjectIdFromRequest(request, key, errorMessage);
    }
    
    /**
     * Method for get property id from request params
     * @param request Spark request object
     * @return founded param
     * @throws DataException throw if param not found in request 
     */
    public static final ObjectId getPropertyId(Request request) 
            throws DataException
    {
        String key = RequestParams.PROPERTY_ID.getName();
        String errorMessage = "Incorrect property id in request";
        return getObjectIdFromRequest(request, key, errorMessage);
    }
    
    /**
     * Method fot get tag id from requst params
     * @param request Spark request obejct
     * @return founded param
     * @throws DataException throw if param not found in request
     */
    public static final ObjectId getTagId(Request request) 
            throws DataException
    {
        String key = RequestParams.TAGS_ID.getName();
        String errorMessage = "Incorrect tag id in request";
        return getObjectIdFromRequest(request, key, errorMessage);
    }
    
    /**
     * Method for get company id from request params
     * @param request Spark request object
     * @return company id
     * @throws DataException throw if param not found in request
     */
    public static final ObjectId getCompanyId(Request request) 
            throws DataException {
        String key = RequestParams.COMPANY_ID.getName();
        String errorMessage = "Incorrect company id in request";
        return getObjectIdFromRequest(request, key, errorMessage);
    }
}
