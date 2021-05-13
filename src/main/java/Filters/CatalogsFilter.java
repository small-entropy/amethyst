/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Filters;

import org.bson.types.ObjectId;

/**
 * Class for catalogs collection filter object
 * @author small-entropy
 */
public class CatalogsFilter extends Filter {
    private String name;
    private String tile;
    private ObjectId owner;
    
    public CatalogsFilter() {}
    
    public CatalogsFilter(ObjectId id, String[] excludes) {
        super(id, excludes);
    }
    
    public CatalogsFilter(int skip, int limit, String[] excludes) {
        super(skip, limit, excludes);
    }
    
    public CatalogsFilter(String name, String[] excludes) {
        super(excludes);
        this.name = name;
    }
    
    public CatalogsFilter(String[] excludes) {
        super(excludes);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTile() {
        return tile;
    }

    public void setTile(String tile) {
        this.tile = tile;
    }

    public ObjectId getOwner() {
        return owner;
    }

    public void setOwner(ObjectId owner) {
        this.owner = owner;
    }
}
