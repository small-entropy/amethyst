/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils.common;

import Exceptions.DataException;
import Utils.constants.RequestParams;
import org.bson.types.ObjectId;
import spark.Request;

/**
 * Class for work with request params
 * @author small-entropy
 */
public class ParamsManager {
    
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
    
    public static final ObjectId getUserId(Request request) 
            throws DataException 
    {
        String key = RequestParams.USER_ID.getName();
        String errorMessage = "Incorrect user id in request";
        return getObjectIdFromRequest(request, key, errorMessage);
    }
    
    public static final ObjectId getCategoryId(Request request) 
            throws DataException 
    {
        String key = RequestParams.CATAGORY_ID.getName();
        String errorMessage = "Incorrect category id in request";
        return getObjectIdFromRequest(request, key, errorMessage);
    }
    
    public static final ObjectId getCatalogId(Request request) 
            throws DataException 
    {
        String key = RequestParams.CATALOG_ID.getName();
        String errorMessage = "Incorrect catalog id in request";
        return getObjectIdFromRequest(request, key, errorMessage);
    }
    
    public static final ObjectId getRightId(Request request) 
            throws DataException
    {
        String key = RequestParams.RIGHT_ID.getName();
        String errorMessage = "Incorrect right id in request";
        return getObjectIdFromRequest(request, key, errorMessage);
    }
    
    public static final ObjectId getPropertyId(Request request) 
            throws DataException
    {
        String key = RequestParams.PROPERTY_ID.getName();
        String errorMessage = "Incorrect property id in request";
        return getObjectIdFromRequest(request, key, errorMessage);
    }
}
