package Models.Standalones;

import Models.Embeddeds.EmbeddedOwner;
import Models.Embeddeds.EmbeddedProperty;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Version;
import java.util.List;
import org.bson.types.ObjectId;

/**
 * Model for tag collection
 * @author small-entropy
 */
@Entity("tags")
public class Tag {
    @Id
    private ObjectId id;
    private String name;
    private String value;
    private String description;
    private String status;
    private EmbeddedOwner owner;
    @Version
    private Long version;

    public Tag() {
        this.status = "active";
    }

    public Tag(
            String name, 
            String value, 
            String description,
            EmbeddedOwner owner
    ) {
        this.name = name;
        this.value = value;
        this.description = description;
        this.status = "active";
        this.owner = owner;
    }

    public EmbeddedOwner getOwner() {
        return owner;
    }

    public void setOwner(EmbeddedOwner owner) {
        this.owner = owner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
    
    /**
     * Method for deactivate user
     */
    public void deactivate() {
        this.status = "inactive";
    }

    /**
     * Method for activate user
     */
    public void activate() {
        this.status = "active";
    }
    
    public String getStringifiedId() {
        return id.toString();
    }
}
