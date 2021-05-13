package Models;
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
    private CatalogOwner owner;
    private String status;
    @Version private Long version;
            
    Catalog() {}
    
    public Catalog(String name, String title, CatalogOwner owner) {
        this.name = name;
        this.title = title;
        this.owner = owner;
        this.status = "active";
    }
    
    public Catalog(ObjectId id, String name, String title, CatalogOwner owner) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.owner = owner;
        this.status = "active";
    }
    
    
    public Catalog(String name, String title, String description, CatalogOwner owner) {
        this.name = name;
        this.title = title;
        this.description = description;
        this.owner = owner;
        this.status = "active";
    }
    
    public Catalog(ObjectId id, String name, String title, String description, CatalogOwner owner) {
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
    
    public CatalogOwner getOwner() {
        return owner;
    }

    public void setOwner(CatalogOwner owner) {
        this.owner = owner;
    }
    
    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
