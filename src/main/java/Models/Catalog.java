package Models;
import org.bson.types.ObjectId;
import dev.morphia.annotations.*;

@Entity("catalogs")
public class Catalog {
    @Id
    private ObjectId id;
    private String name;
    private String description;

    Catalog() {}

    public Catalog(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public ObjectId getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
