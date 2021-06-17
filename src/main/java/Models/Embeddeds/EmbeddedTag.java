package Models.Embeddeds;

import dev.morphia.annotations.Embedded;
import org.bson.types.ObjectId;

/**
 * Model for embedded tag document
 * @author small-entropy
 */
@Embedded
public class EmbeddedTag {
    private ObjectId id;
    private String name;
    private String value;

    public EmbeddedTag() {}

    public EmbeddedTag(ObjectId id, String value, String name) {
        this.id = id;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }
    
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
