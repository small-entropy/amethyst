package Models;
import org.bson.types.ObjectId;
import dev.morphia.annotations.Embedded;

@Embedded
public class UserRight {
    private ObjectId id;
    private String collection;
    private String create;
    private String read;
    private String update;
    private String delete;

    UserRight() {}

    public UserRight(String collection) {
        this.id = new ObjectId();
        this.collection = collection;
        this.create = "001000";
        this.read = "011001";
        this.update = "001000";
        this.delete = "001000";
    }

    public UserRight(String collection, String create, String read, String update, String delete) {
        this.id = new ObjectId();
        this.collection = collection;
        this.create = create;
        this.read = read;
        this.update = update;
        this.delete = delete;
    }

    public ObjectId getId() {
        return id;
    }

    public String getCollection() {
        return collection;
    }

    public String getCreate() {
        return create;
    }

    public String getRead() {
        return read;
    }

    public String getUpdate() {
        return update;
    }

    public String getDelete() {
        return delete;
    }

    public void setCreate(String create) {
        this.create = create;
    }

    public void setRead(String read) {
        this.read = read;
    }

    public void setUpdate(String update) {
        this.update = update;
    }

    public void setDelete(String delete) {
        this.delete = delete;
    }
}
