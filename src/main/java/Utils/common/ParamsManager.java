/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils.common;

import Utils.constants.RequestParams;

/**
 *
 * @author igrav
 */
public class ParamsManager {
    
    public static final String getUserId() {
        return RequestParams.USER_ID.getName();
    }
    
    public static final String getCategoryId() {
        return RequestParams.CATAGORY_ID.getName();
    }
}
