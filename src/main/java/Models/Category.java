/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Models;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;
import dev.morphia.annotations.IndexOptions;
import dev.morphia.annotations.Indexed;
import dev.morphia.annotations.Version;
import java.util.List;
import org.bson.types.ObjectId;

/**
 * Model for categories colletion
 * @author small-entropy
 */
@Entity("categories")
public class Category {
    @Id
    private ObjectId id;
    @Indexed(options = @IndexOptions(unique = true))
    private String name;
    private String title;
    private String description;
    private CatalogCategory catalog;
    private List<BreadcrumbCategory> breadcrumbs;
    private List<BreadcrumbCategory> childs;
    private Owner owner;
    @Version private Long version;

    Category() {}

    public String getId() {
        return id.toString();
    }
    
    public ObjectId getPureId() {
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

    public CatalogCategory getCatalog() {
        return catalog;
    }

    public void setCatalog(CatalogCategory catalog) {
        this.catalog = catalog;
    }

    public List<BreadcrumbCategory> getBreadcrumbs() {
        return breadcrumbs;
    }

    public void setBreadcrumbs(List<BreadcrumbCategory> breadcrumbs) {
        this.breadcrumbs = breadcrumbs;
    }

    public List<BreadcrumbCategory> getChilds() {
        return childs;
    }

    public void setChilds(List<BreadcrumbCategory> childs) {
        this.childs = childs;
    }

    public Long getVersion() {
        return version;
    }

    public Owner getOwner() {
        return owner;
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
    }
    
    
}
