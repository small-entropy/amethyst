package Models.Standalones;

import Models.Embeddeds.EmbeddedCatalog;
import Models.Embeddeds.EmbeddedCategory;
import Models.Embeddeds.EmbeddedOwner;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.Version;
import java.util.List;
import org.bson.types.ObjectId;

/**
 * 
 * @author small-entropy
 */
@Entity("products")
public class Product {
    @Id
    private ObjectId id;
    private String name;
    private String title;
    private String description;
    private EmbeddedCatalog catalog;
    private List<EmbeddedCategory> categories;
    private EmbeddedOwner owner;
    @Version private Long version;

    Product() {}

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EmbeddedCatalog getCatalog() {
        return catalog;
    }

    public void setCatalog(EmbeddedCatalog catalog) {
        this.catalog = catalog;
    }

    public List<EmbeddedCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<EmbeddedCategory> categories) {
        this.categories = categories;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
