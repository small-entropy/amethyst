/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models.Embeddeds;

import Models.Embeddeds.Owner;
import dev.morphia.annotations.Embedded;
import org.bson.types.ObjectId;

/**
 * 
 * @author small-entropy
 */
@Embedded
public class CatalogCategory {
    private ObjectId id;
    private String title;
    private Owner owner;
    
    CatalogCategory() {}
    
    public CatalogCategory(ObjectId id, String title, Owner owner) {
        this.id = id;
        this.title = title;
        this.owner = owner;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }
    
    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }
}