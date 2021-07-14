package synthwave.models.mongodb.standalones;

import synthwave.models.mongodb.embeddeds.EmbeddedOwner;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Field;
import dev.morphia.annotations.Index;
import dev.morphia.annotations.IndexOptions;
import dev.morphia.annotations.Indexes;
import dev.morphia.annotations.Version;
import platform.models.mongodb.morphia.Document;

/**
 * Model for tag collection
 * @author small-entropy
 */
@Entity("tags")
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
public class Tag extends Document {

    @Version private Long version;

    public Tag() {
        super();
    }

    public Tag(
            String name, 
            String title, 
            String description,
            EmbeddedOwner owner
    ) {
        super(name, title, description, owner);
    }


    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
