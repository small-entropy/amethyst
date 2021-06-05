package Models.Standalones;

import Models.Embeddeds.EmbeddedBreadcrumb;
import Models.Embeddeds.EmbeddedCatalog;
import Models.Embeddeds.EmbeddedOwner;
import dev.morphia.annotations.*;
import java.util.List;
import org.bson.types.ObjectId;

/**
 * Model for categories colletion
 * @author small-entropy
 */
@Entity("categories")
@Indexes({
    @Index(
            fields = @Field("name"),
            options = @IndexOptions(unique = true)
    ),
    @Index(fields = @Field("title")),
    @Index(fields = @Field("catalog")),
    @Index(fields = @Field("owner"))
})
public class Category {
    @Id
    private ObjectId id;
    private String name;
    private String title;
    private String description;
    private EmbeddedCatalog catalog;
    private String status;
    private List<EmbeddedBreadcrumb> breadcrumbs;
    private List<EmbeddedBreadcrumb> childs;
    private EmbeddedOwner owner;
    @Version private Long version;

    Category() {}
    
    public Category(
            ObjectId id, 
            String name, 
            String title, 
            String description,
            EmbeddedCatalog catalog,
            List<EmbeddedBreadcrumb> breadcrumbs,
            EmbeddedOwner owner
    ) {
        this.id = id;
        this.name = name;
        this.title = title;
        this.description = description;
        this.catalog = catalog;
        this.breadcrumbs = breadcrumbs;
        this.owner = owner;
        this.status = "active";
    }
    
    public Category(
            String name, 
            String title, 
            String description,
            EmbeddedCatalog catalog,
            List<EmbeddedBreadcrumb> breadcrumbs,
            EmbeddedOwner owner
    ) {
        this.name = name;
        this.title = title;
        this.description = description;
        this.catalog = catalog;
        this.breadcrumbs = breadcrumbs;
        this.owner = owner;
        this.status = "active";
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ObjectId getId() {
        return id;
    }
    
    public String getStringifiedId() {
        return id.toString();
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

    public List<EmbeddedBreadcrumb> getBreadcrumbs() {
        return breadcrumbs;
    }

    public void setBreadcrumbs(List<EmbeddedBreadcrumb> breadcrumbs) {
        this.breadcrumbs = breadcrumbs;
    }

    public List<EmbeddedBreadcrumb> getChilds() {
        return childs;
    }

    public void setChilds(List<EmbeddedBreadcrumb> childs) {
        this.childs = childs;
    }

    public Long getVersion() {
        return version;
    }

    public EmbeddedOwner getOwner() {
        return owner;
    }

    public void setOwner(EmbeddedOwner owner) {
        this.owner = owner;
    }
    
    public void deactivate() {
        this.status = "inactive";
    }
}
