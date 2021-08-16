package synthwave.models.morphia.extend;

import engine.models.morphia.extend.DocumentExtended;
import core.models.morphia.embeddeds.EmbeddedOwner;
import core.models.morphia.embeddeds.EmbeddedProperty;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Field;
import dev.morphia.annotations.Index;
import dev.morphia.annotations.IndexOptions;
import dev.morphia.annotations.Indexes;
import dev.morphia.annotations.Version;
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
public class Company extends DocumentExtended {
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
            List<EmbeddedProperty> profile,
            List<EmbeddedProperty> properties
    ) {
        super(id, name, title, description, owner, profile, properties);
    }
    
    public Company(
            String name, 
            String title, 
            String description, 
            EmbeddedOwner owner,
            List<EmbeddedProperty> profile,
            List<EmbeddedProperty> properties
    ) {
        super(name, title, description, owner, profile, properties);
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
