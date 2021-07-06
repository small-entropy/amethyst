package synthwave.models.mongodb.embeddeds;
import dev.morphia.annotations.Embedded;
import org.bson.types.ObjectId;

@Embedded
public class EmbeddedProperty {
    private ObjectId id;
    private String key;
    private Object value;

    EmbeddedProperty() {}

    public EmbeddedProperty(String key, Object value) {
        this.id = new ObjectId();
        this.key = key;
        this.value = value;
    }

    public String getId() {
        return id.toString();
    }
    
    public ObjectId getPureId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
