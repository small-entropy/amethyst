/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Filters;

import org.bson.types.ObjectId;

/**
 *
 * @author entropy
 */
public abstract class Filter {
    private int skip;
    private int limit;
    private String status = "active";
    private ObjectId id;
    private String[] excludes;
    
    public Filter() {}
    
    public Filter(ObjectId id, String[] excludes) {
        this.id = id;
        this.excludes = excludes;
    }
    
    public Filter(int skip, int limit, String[] excludes) {
        this.skip = skip;
        this.limit = limit;
        this.excludes = excludes;
    }
    
    public int getSkip() {
        return skip;
    }
    
    public void setSkip(int skip) {
        this.skip = skip;
    }
    
    public int getLimit() {
        return limit;
    }
    
    public void setLimit(int limit) {
        this.limit = limit;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public ObjectId getId() {
        return id;
    }
    
    public void setId(ObjectId id) {
        this.id = id;
    }
    
    public String[] getExcludes() {
        return excludes;
    }
    
    public void setExcludes(String[] excludes) {
        this.excludes = excludes;
    }
}
