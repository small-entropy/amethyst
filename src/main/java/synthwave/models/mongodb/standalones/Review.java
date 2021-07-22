/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package synthwave.models.mongodb.standalones;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

import org.bson.types.ObjectId;

/**
 *
 * @author igrav
 */
@Entity("reviews")
public class Review {
    @Id
    private ObjectId id;    
}
