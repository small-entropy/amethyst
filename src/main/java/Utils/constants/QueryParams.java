/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils.constants;

/**
 *
 * @author igrav
 */
public enum QueryParams {
    SKIP("skip"),
    LIMIT("limit");
    
    private final String key;

    QueryParams(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
