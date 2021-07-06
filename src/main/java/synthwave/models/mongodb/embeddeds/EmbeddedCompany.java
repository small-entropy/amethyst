package synthwave.models.mongodb.embeddeds;

import dev.morphia.annotations.Embedded;
import org.bson.types.ObjectId;

/**
 * Model for embedded company document
 * @author small-enropy
 */
@Embedded
public class EmbeddedCompany {
    private ObjectId id;
    private String title;
    private String description;
    private EmbeddedOwner owner;

    public EmbeddedCompany() {}

    public EmbeddedCompany(
            ObjectId id, 
            String title, 
            String description,
            EmbeddedOwner owner
    ) {
        this.id = id;
        this.title = title;
        this.description = description;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EmbeddedOwner getOwner() {
        return owner;
    }

    public void setOwner(EmbeddedOwner owner) {
        this.owner = owner;
    }
}
