package synthwave.models.mongodb.standalones;

import synthwave.models.mongodb.base.DocumentExtended;
import synthwave.models.mongodb.embeddeds.EmbeddedBreadcrumb;
import synthwave.models.mongodb.embeddeds.EmbeddedCatalog;
import synthwave.models.mongodb.embeddeds.EmbeddedOwner;
import synthwave.models.mongodb.embeddeds.EmbeddedProperty;
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
public class Category extends DocumentExtended {
    
    private EmbeddedCatalog catalog;
    private List<EmbeddedBreadcrumb> breadcrumbs;
    private List<EmbeddedBreadcrumb> childs;
    @Version private Long version;

    public Category() {
        super();
    }
    
    public Category(
            ObjectId id, 
            String name, 
            String title, 
            String description,
            EmbeddedCatalog catalog,
            List<EmbeddedBreadcrumb> breadcrumbs,
            EmbeddedOwner owner
    ) {
        super(id, name, title, description, owner, null, null);
        this.catalog = catalog;
        this.breadcrumbs = breadcrumbs;
    }
    
    public Category(
            String name, 
            String title, 
            String description,
            EmbeddedCatalog catalog,
            List<EmbeddedBreadcrumb> breadcrumbs,
            EmbeddedOwner owner
    ) {
        super(name, title, description, owner, null, null);
        this.catalog = catalog;
        this.breadcrumbs = breadcrumbs;
    }
    
    public Category(
            ObjectId id, 
            String name, 
            String title, 
            String description,
            EmbeddedCatalog catalog,
            List<EmbeddedBreadcrumb> breadcrumbs,
            EmbeddedOwner owner,
            List<EmbeddedProperty> profile,
            List<EmbeddedProperty> properties
    ) {
        super(id, name, title, description, owner, profile, properties);
        this.catalog = catalog;
        this.breadcrumbs = breadcrumbs;
    }
    
    public Category(
            String name, 
            String title, 
            String description,
            EmbeddedCatalog catalog,
            List<EmbeddedBreadcrumb> breadcrumbs,
            EmbeddedOwner owner,
            List<EmbeddedProperty> profile,
            List<EmbeddedProperty> properties
    ) {
        super(name, title, description, owner, profile, properties);
        this.catalog = catalog;
        this.breadcrumbs = breadcrumbs;
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
}
