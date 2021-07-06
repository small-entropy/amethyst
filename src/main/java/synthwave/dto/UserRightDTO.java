/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package synthwave.dto;

import platform.dto.BaseDTO;

/**
 * Class of data transfer object for 
 * @author small-entropy
 */
public class UserRightDTO extends BaseDTO {
    private String name;
    private String create;
    private String read;
    private String update;
    private String delete;

    /**
     * Default object constructor
     */
    UserRightDTO() {}

    /**
     * Getter for name proeprty
     * @return value of name property
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for name property
     * @param name nea value for name proeprty
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for create property
     * @return value of create property
     */
    public String getCreate() {
        return create;
    }

    /**
     * Setter for create property
     * @param create new value for create property
     */
    public void setCreate(String create) {
        this.create = create;
    }

    /**
     * Getter for read property
     * @return value of read property
     */
    public String getRead() {
        return read;
    }

    /**
     * Setter for read property
     * @param read new value of read proeprty
     */
    public void setRead(String read) {
        this.read = read;
    }

    /**
     * Getter for update property
     * @return value of update property
     */
    public String getUpdate() {
        return update;
    }

    /**
     * Setter for update property
     * @param update new value of update property
     */
    public void setUpdate(String update) {
        this.update = update;
    }

    /**
     * Getter for delete property
     * @return value of delete property
     */
    public String getDelete() {
        return delete;
    }

    /**
     * Setter for delete property
     * @param delete new value for delete property
     */
    public void setDelete(String delete) {
        this.delete = delete;
    }
    
}
