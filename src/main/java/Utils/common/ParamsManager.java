/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils.common;

import Utils.constants.RequestParams;
import spark.Request;

/**
 *
 * @author igrav
 */
public class ParamsManager {
    
    public static final String getUserId(Request request) {
        return request.params(RequestParams.USER_ID.getName());
    }
    
    public static final String getCategoryId(Request request) {
        return request.params(RequestParams.CATAGORY_ID.getName());
    }
    
    public static final String getCatalogId(Request request) {
        return request.params(RequestParams.CATALOG_ID.getName());
    }
    
    public static final String getRightId(Request request) {
        return request.params(RequestParams.RIGHT_ID.getName());
    }
    
    public static final String getPropertyId(Request request) {
        return request.params(RequestParams.PROPERTY_ID.getName());
    }
}
