/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Reference;
import dev.morphia.annotations.Version;
import org.bson.types.ObjectId;

/**
 *
 * @author igrav
 */
@Entity("reviews")
public class Review {
    @Id
    private ObjectId id;
    @Version private Long version;
    private String text;
    @Reference
    private Product product;
    @Reference
    private User author;
}
