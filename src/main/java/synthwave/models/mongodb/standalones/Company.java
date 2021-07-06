package synthwave.models.mongodb.standalones;

import synthwave.models.mongodb.embeddeds.EmbeddedOwner;
import synthwave.models.mongodb.embeddeds.EmbeddedProperty;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Field;
import dev.morphia.annotations.Index;
import dev.morphia.annotations.IndexOptions;
import dev.morphia.annotations.Indexes;
import dev.morphia.annotations.Version;
import synthwave.models.mongodb.base.Document;

import java.util.List;
import org.bson.types.ObjectId;

/**
 * Class for work with sellers
 * @author small-entropy
 */
@Entity("companies")
@Indexes({
    @Index(
            fields = @Field("name"),
            options = @IndexOptions(unique = true)
    ),
    @Index(
            fields = @Field("title"),
            options = @IndexOptions(unique = true)
    )
})
public class Company extends Document {
    
    private List<EmbeddedProperty> profile;
    @Version private Long version;

    public Company() {
        super();
    }

    public Company(
            ObjectId id, 
            String name, 
            String title, 
            String description, 
            EmbeddedOwner owner,
            List<EmbeddedProperty> profile
    ) {
        super(id, name, title, description, owner);
        this.profile = profile;
    }
    
    public Company(
            String name, 
            String title, 
            String description, 
            EmbeddedOwner owner,
            List<EmbeddedProperty> profile
    ) {
        super(name, title, description, owner);
        this.profile = profile;
    }
    
    public List<EmbeddedProperty> getProfile() {
        return profile;
    }

    public void setProfile(List<EmbeddedProperty> profile) {
        this.profile = profile;
    }


    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
