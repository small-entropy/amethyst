/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils.constants;

/**
 * Enum for request params
 * @author small-entropy
 */
public enum RequestParams {
    CATALOG_ID("catalog_id"),
    CATAGORY_ID("category_id"),
    USER_ID("user_id"),
    PROPERTY_ID("property_id"),
    RIGHT_ID("right_id");
    
    private final String name;

    RequestParams(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
