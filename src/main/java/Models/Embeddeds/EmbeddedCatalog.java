/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models.Embeddeds;

import dev.morphia.annotations.Embedded;
import org.bson.types.ObjectId;

/**
 * 
 * @author small-entropy
 */
@Embedded
public class EmbeddedCatalog {
    private ObjectId id;
    private String title;
    private EmbeddedOwner owner;
    
    EmbeddedCatalog() {}
    
    public EmbeddedCatalog(ObjectId id, String title, EmbeddedOwner owner) {
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

    public EmbeddedOwner getOwner() {
        return owner;
    }

    public void setOwner(EmbeddedOwner owner) {
        this.owner = owner;
    }
}