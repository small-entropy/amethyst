package Models;
import dev.morphia.annotations.Embedded;
import org.bson.types.ObjectId;

@Embedded
public class UserProperty {
    private ObjectId id;
    private String key;
    private Object value;

    UserProperty() {}

    public UserProperty(String key, Object value) {
        this.id = new ObjectId();
        this.key = key;
        this.value = value;
    }

    public Object getId() {
        return id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
