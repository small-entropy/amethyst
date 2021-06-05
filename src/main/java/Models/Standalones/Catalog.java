package Models.Standalones;

import Models.Embeddeds.EmbeddedOwner;
import org.bson.types.ObjectId;
import dev.morphia.annotations.*;

/**
 * Model class for work with catalog focument
 * @author small-entropy
 */
@Entity("catalogs")
@Indexes({
    @Index(
            fields = @Field("name"),
            options = @IndexOptions(unique = true)
    ),
    @Index(fields = @Field("title")),
    @Index(fields = @Field("owner"))
})
public class Catalog {
    @Id
    private ObjectId id;
    private String name;
    private String title;
    private String description;
    private EmbeddedOwner owner;
    private String status;
    @Version private Long version;
            
    Catalog() {}
    
    public Catalog(String name, String title, EmbeddedOwner owner) {
        this.name = name;
        this.title = title;
        this.owner = owner;
        this.status = "active";
    }
    
    public Catalog(ObjectId id, String name, String title, EmbeddedOwner owner) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.owner = owner;
        this.status = "active";
    }
    
    
    public Catalog(String name, String title, String description, EmbeddedOwner owner) {
        this.name = name;
        this.title = title;
        this.description = description;
        this.owner = owner;
        this.status = "active";
    }
    
    public Catalog(ObjectId id, String name, String title, String description, EmbeddedOwner owner) {
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
    
    public EmbeddedOwner getOwner() {
        return owner;
    }

    public void setOwner(EmbeddedOwner owner) {
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
