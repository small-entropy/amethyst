package synthwave.models.morphia.embeddeds;

import org.bson.types.ObjectId;

/**
 *
 * @author small-entropy
 */
public class EmbeddedCategory {
    private ObjectId id;
    private String title;
    private String owner;

    EmbeddedCategory() {}
    
    public EmbeddedCategory(
            ObjectId id,
            String title,
            String owner
    ) {
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }
}
