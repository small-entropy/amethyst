package Models;
import org.bson.types.ObjectId;
import dev.morphia.annotations.Embedded;

@Embedded
public class UserRight {
    private ObjectId id;
    private String collection;
    private boolean create;
    private boolean read;
    private boolean update;
    private boolean delete;

    UserRight() {}

    public UserRight(String collection) {
        this.id = new ObjectId();
        this.collection = collection;
        this.create = false;
        this.read = true;
        this.update = false;
        this.delete = false;
    }

    public UserRight(String collection, boolean create, boolean read, boolean update, boolean delete) {
        this.id = new ObjectId();
        this.collection = collection;
        this.create = create;
        this.read = read;
        this.update = update;
        this.delete = delete;
    }

    /**
     * Getter for id property
     * @return current value of id property
     */
    public ObjectId getId() {
        return id;
    }

    /**
     * Getter collection name
     * @return current value of collection name
     */
    public String getCollection() {
        return collection;
    }

    /**
     * Getter for create rule
     * @return current value of create rule
     */
    public boolean getCreate() {
        return create;
    }

    /**
     * Getter for read rule
     * @return current value of read rule
     */
    public boolean getRead() {
        return read;
    }

    /**
     * Getter for update rule
     * @return current value of update rule
     */
    public boolean getUpdate() {
        return update;
    }

    /**
     * Getter for delete rule
     * @return current value of delete rule
     */
    public boolean getDelete() {
        return delete;
    }
}
