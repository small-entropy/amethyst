package Models.Standalones;
import Models.Embeddeds.Owner;
import org.bson.types.ObjectId;
import dev.morphia.annotations.*;

/**
 * Model class for work with catalog focument
 * @author small-entropy
 */
@Entity("catalogs")
public class Catalog {
    @Id
    private ObjectId id;
    @Indexed(options = @IndexOptions(unique = true))
    private String name;
    private String title;
    private String description;
    private Owner owner;
    private String status;
    @Version private Long version;
            
    Catalog() {}
    
    public Catalog(String name, String title, Owner owner) {
        this.name = name;
        this.title = title;
        this.owner = owner;
        this.status = "active";
    }
    
    public Catalog(ObjectId id, String name, String title, Owner owner) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.owner = owner;
        this.status = "active";
    }
    
    
    public Catalog(String name, String title, String description, Owner owner) {
        this.name = name;
        this.title = title;
        this.description = description;
        this.owner = owner;
        this.status = "active";
    }
    
    public Catalog(ObjectId id, String name, String title, String description, Owner owner) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.description = description;
        this.owner = owner;
        this.status = "active";
    }

    public String getStatus()  {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }
    
    
    public String getStringifiedId() {
        return id.toString();
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
    
    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }
    
    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public void deactivate() {
        this.status = "inactive";
    }
}
