/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataTransferObjects;

import Models.User;

/**
 * Class data transfer object for catalog model data
 * @author small-entropy
 */
public class CatalogDTO {
    private String name;
    private String title;
    private String description;
    private User owner;
    
    /**
     * Default constructor for data transfer object
     */
    public CatalogDTO() {}

    /**
     * Getter for name property
     * @return value of name property
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for name propery
     * @param name new value for name property
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for title property
     * @return value of title property
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for title property
     * @param title value of title property
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter for description proeprty
     * @return value of description property
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for description property
     * @param description new value for description property
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter for owner property
     * @return value of owner property
     */
    public User getOwner() {
        return owner;
    }

    /**
     * Setter for owner propety
     * @param owner new value for owner property
     */
    public void setOwner(User owner) {
        this.owner = owner;
    }
}
