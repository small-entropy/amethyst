/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package synthwave.filters;

import core.filters.Filter;
import org.bson.types.ObjectId;

/**
 *
 * @author entropy
 */
public class UsersFilter extends Filter {
    private String username;
    
    public UsersFilter() {}
    
    public UsersFilter(ObjectId id, String[] excludes) {
        super(id, excludes);
    }
    
    public UsersFilter(int skip, int limit, String[] excludes) {
        super(skip, limit, excludes);
    }
    
    public UsersFilter(String username, String[] excludes) {
        super();
        this.username = username;
        this.setExcludes(excludes);
    }
        
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
}
