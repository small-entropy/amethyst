package Models;
import org.bson.types.ObjectId;
import dev.morphia.annotations.*;


@Entity("catalogs")
public class Catalog {
    @Id
    private ObjectId id;
    @Indexed(options = @IndexOptions(unique = true))
    private String name;
    private String title;
    private String description;
    @Reference
    private User owner;
    @Version private Long version;
            
    Catalog() {}
    
    public Catalog(String name, String title, User owner) {
        this.name = name;
        this.title = title;
        this.owner = owner;
    }
    
    public Catalog(String name, String title, String description, User owner) {
        this.name = name;
        this.title = title;
        this.description = description;
        this.owner = owner;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public String getId() {
        return id.toString();
    }

    public void setId(ObjectId id) {
        this.id = id;
    }
    
    
    public ObjectId getPureId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
    
    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
