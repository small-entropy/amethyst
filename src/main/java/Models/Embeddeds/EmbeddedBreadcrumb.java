package Models.Embeddeds;

import dev.morphia.annotations.Embedded;
import org.bson.types.ObjectId;

/**
 *
 * @author igrav
 */
@Embedded
public class EmbeddedBreadcrumb {
    private ObjectId id;
    private String title;
    
    EmbeddedBreadcrumb() {}

    public EmbeddedBreadcrumb(ObjectId id, String title) {
        this.id = id;
        this.title = title;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
