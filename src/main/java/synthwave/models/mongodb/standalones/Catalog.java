package synthwave.models.mongodb.standalones;

import synthwave.models.mongodb.base.DocumentExtended;
import synthwave.models.mongodb.embeddeds.EmbeddedOwner;
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
public class Catalog extends DocumentExtended {
    @Version private Long version;
            
    public Catalog() {
        super();
    }
    
    public Catalog(
            String name, 
            String title, 
            EmbeddedOwner owner
    ) {
        super(name, title, owner, null, null);
    }
    
    public Catalog(
            ObjectId id, 
            String name, 
            String title, 
            EmbeddedOwner owner
    ) {
        super(id, name, title, owner);
    }
    
    
    public Catalog(
            String name, 
            String title, 
            String description, 
            EmbeddedOwner owner
    ) {
        super(name, title, description, owner);
    }
    
    public Catalog(
            ObjectId id, 
            String name, 
            String title, 
            String description, 
            EmbeddedOwner owner
    ) {
        super(id, name, title, description, owner);
    }
    
    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
