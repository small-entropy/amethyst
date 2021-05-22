package DataTransferObjects;

import Models.BreadcrumbCategory;
import Models.Catalog;
import Models.CatalogCategory;
import Models.Category;
import Models.Owner;
import Models.User;
import java.util.List;
import org.bson.types.ObjectId;

/**
 * Class data transfer object for category model data
 * @author small-entropy
 */
public class CategoryDTO {
    private ObjectId id;
    private String name;
    private String title;
    private String description;
    private Catalog catalog;
    private Category parent;
    private List<CatalogCategory> catalogs; 
    private List<BreadcrumbCategory> bradcrumbs;
    private List<BreadcrumbCategory> childs;
    private User owner;

    public CategoryDTO() {}

    public List<CatalogCategory> getCatalogs() {
        return catalogs;
    }

    public void setCatalogs(List<CatalogCategory> catalogs) {
        this.catalogs = catalogs;
    }

    public List<BreadcrumbCategory> getChilds() {
        return childs;
    }

    public void setChilds(List<BreadcrumbCategory> childs) {
        this.childs = childs;
    }

    public List<BreadcrumbCategory> getBradcrumbs() {
        return bradcrumbs;
    }

    public void setBradcrumbs(List<BreadcrumbCategory> bradcrumbs) {
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
