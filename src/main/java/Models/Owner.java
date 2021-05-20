package Models;

import dev.morphia.annotations.Embedded;
import java.util.Date;
import org.bson.types.ObjectId;

/**
 * Model for embded document of catalog document for saving owner data
 * @author small-entropy
 */
@Embedded
public class Owner {
    // Owner id as ObjectId
    private ObjectId id;
    // Owner username
    private String username;
    // Time creation
    private Long time;
    
    /**
     * Default catalog owner constructor
     */
    Owner() {}
    
    /**
     * Constructor for create catalog owner by user id and username
     * @param id owner id as ObjectId
     * @param username owner username
     */
    public Owner(ObjectId id, String username) {
        this.id = id;
        this.username = username;
        this.time = new Date().getTime();
    }

    /**
     * Getter owner id
     * @return value of owner id
     */
    public ObjectId getId() {
        return id;
    }

    /**
     * Setter owner id
     * @param id new value for owner id
     */
    public void setId(ObjectId id) {
        this.id = id;
    }

    /**
     * Getter owner username
     * @return value of username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Setter for owner username
     * @param username new value for username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * Getter time creation
     * @return value of time creation
     */
    public Long getTime() {
        return time;
    }

    /**
     * Setter time creation
     * @param time new value of time creation
     */
    public void setTime(Long time) {
        this.time = time;
    }
}
