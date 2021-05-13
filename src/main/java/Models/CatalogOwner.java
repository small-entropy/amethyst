/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import dev.morphia.annotations.Embedded;
import java.util.Date;
import org.bson.types.ObjectId;

/**
 * Model for embded document of catalog document for saving owner data
 * @author igrav
 */
@Embedded
public class CatalogOwner {
    private ObjectId id;
    private String username;
    private Long time;
    
    CatalogOwner() {}
    
    public CatalogOwner(ObjectId id, String username) {
        this.id = id;
        this.username = username;
        this.time = new Date().getTime();
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}
