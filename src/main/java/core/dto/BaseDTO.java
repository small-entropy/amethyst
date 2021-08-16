/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package core.dto;

import com.google.gson.Gson;
import spark.Request;

/**
 *
 * @author small-entropy
 */
public class BaseDTO {
    
    public static <T> T build(Request request, Class<T> dto) {
        return new Gson().fromJson(request.body(), dto);
    }
}
