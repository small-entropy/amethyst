package DataTransferObjects.v1;

import DataTransferObjects.base.BaseDTO;
import Models.Embeddeds.EmbeddedBreadcrumb;
import Models.Standalones.Catalog;
import Models.Standalones.Category;
import Models.Standalones.User;
import java.util.List;
import org.bson.types.ObjectId;

/**
 * Class data transfer object for category model data
 * @author small-entropy
 */
public class CategoryDTO extends BaseDTO {
    private ObjectId id;
    private String name;
    private String title;
    private String description;
    private Catalog catalog;
    private Category parent;
    private List<EmbeddedBreadcrumb> bradcrumbs;
    private List<EmbeddedBreadcrumb> childs;
    private User owner;

    public CategoryDTO() {}

    public List<EmbeddedBreadcrumb> getChilds() {
        return childs;
    }

    public void setChilds(List<EmbeddedBreadcrumb> childs) {
        this.childs = childs;
    }

    public List<EmbeddedBreadcrumb> getBradcrumbs() {
        return bradcrumbs;
    }

    public void setBradcrumbs(List<EmbeddedBreadcrumb> bradcrumbs) {
        this.bradcrumbs = bradcrumbs;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Catalog getCatalog() {
        return catalog;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }

    public Category getParent() {
        return parent;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }
}
