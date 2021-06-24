package Models.Base;

import dev.morphia.annotations.Id;
import org.bson.types.ObjectId;

/**
 * Abstract class for standalone document
 * @author small-entropy
 */

public abstract class Standalone {
  
    /** Property for identificator document */
    @Id
    private ObjectId id;
    /** Property document status */
    private String status;
    
    /**
     * Main cinstructor for standalone entity
     */
    public Standalone() {
        this.status = "active";
    }

    /**
     * Constructor for create standalone antity if send id
     * @param id document id
     */
    public Standalone(ObjectId id) {
        this.id = id;
        this.status = "active";
    }

    /**
     * Getter for document id
     * @return document id
     */
    public ObjectId getId() {
        return id;
    }

    /**
     * Setter for document id
     * @param id new document id
     */
    public void setId(ObjectId id) {
        this.id = id;
    }

    /**
     * Getter for document status
     * @return current document status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Setter for document status
     * @param status new document status
     */
    public void setStatus(String status) {
        this.status = status;
    }
    
    /**
     * Method for get stringified id
     * @return stringified document id
     */
    public String getStringifiedId() {
        return getId().toString();
    }
    
    /**
     * Method for deactivate user
     */
    public void deactivate() {
        this.status = "inactive";
    }

    /**
     * Method for activate user
     */
    public void activate() {
        this.status = "active";
    }
}
