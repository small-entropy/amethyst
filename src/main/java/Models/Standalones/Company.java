package Models.Standalones;

import Models.Embeddeds.EmbeddedOwner;
import Models.Embeddeds.EmbeddedProperty;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Version;
import java.util.List;
import org.bson.types.ObjectId;

/**
 * Class for work with sellers
 * @author small-entropy
 */
@Entity("sellers")
public class Company {
    @Id
    private ObjectId id;
    private String name;
    private String title;
    private String description;
    private EmbeddedOwner owner;
    private String status;
    private List<EmbeddedProperty> profile;
    private List<EmbeddedProperty> properties;
    @Version
    private Long version;

    public Company() {
        this.status = "active";
    }

    public Company(
            ObjectId id, 
            String name, 
            String title, 
            String description, 
            EmbeddedOwner owner,
            List<EmbeddedProperty> profile
    ) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.description = description;
        this.owner = owner;
        this.profile = profile;
        this.status = "active";
    }

    public List<EmbeddedProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<EmbeddedProperty> properties) {
        this.properties = properties;
    }

    public List<EmbeddedProperty> getProfile() {
        return profile;
    }

    public void setProfile(List<EmbeddedProperty> profile) {
        this.profile = profile;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    public void deactivate() {
        this.status = "inactive";
    }

    /**
     * Getter for owner field
     * @return 
     */
    public EmbeddedOwner getOwner() {
        return owner;
    }

    /**
     * Setter for owner field
     * @param owner owner document
     */
    public void setOwner(EmbeddedOwner owner) {
        this.owner = owner;
    }
    
    public String getStringifiedId() {
        return id.toString();
    }
}
