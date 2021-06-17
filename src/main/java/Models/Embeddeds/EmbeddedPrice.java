/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models.Embeddeds;

import org.bson.types.ObjectId;

/**
 * Model for embedded document price
 * @author small-entropy
 */
public class EmbeddedPrice {
    private ObjectId id;
    private String name;
    private String value;
    private String currency;
    private String description;

    public EmbeddedPrice() {}

    public EmbeddedPrice(
            ObjectId id, 
            String name, 
            String value, 
            String currency, 
            String description
    ) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.currency = currency;
        this.description = description;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
