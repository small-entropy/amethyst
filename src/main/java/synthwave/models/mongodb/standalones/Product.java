package synthwave.models.mongodb.standalones;

import synthwave.models.mongodb.embeddeds.EmbeddedCatalog;
import synthwave.models.mongodb.embeddeds.EmbeddedCategory;
import synthwave.models.mongodb.embeddeds.EmbeddedCompany;
import synthwave.models.mongodb.embeddeds.EmbeddedOwner;
import synthwave.models.mongodb.embeddeds.EmbeddedPrice;
import synthwave.models.mongodb.embeddeds.EmbeddedProperty;
import synthwave.models.mongodb.embeddeds.EmbeddedTag;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Version;
import platform.models.mongodb.morphia.Document;

import java.util.List;

/**
 * Model for work with products collection
 * @author small-entropy
 */
@Entity("products")
public class Product extends Document {
    
    private String brief;
    private EmbeddedCatalog catalog;
    private List<EmbeddedPrice> prices;
    private List<EmbeddedCategory> categories;
    private List<EmbeddedProperty> properties;
    private List<EmbeddedProperty> requirements;
    private List<EmbeddedProperty> scores;
    private List<EmbeddedTag> tags;
    private EmbeddedCompany seller;
    private EmbeddedCompany manufacturer;
    @Version private Long version;

    public Product() {
        super();
    }

    public Product(
            String name, 
            String title, 
            String brief, 
            String description, 
            EmbeddedCatalog catalog, 
            List<EmbeddedPrice> prices, 
            List<EmbeddedCategory> categories, 
            List<EmbeddedProperty> properties, 
            List<EmbeddedProperty> requirements, 
            List<EmbeddedProperty> scores, 
            List<EmbeddedTag> tags, 
            EmbeddedCompany seller, 
            EmbeddedCompany manufacturer, 
            EmbeddedOwner owner, 
            String status, 
            Long version
    ) {
        super(name, title, description, owner);
        this.brief = brief;
        this.catalog = catalog;
        this.prices = prices;
        this.categories = categories;
        this.properties = properties;
        this.requirements = requirements;
        this.scores = scores;
        this.tags = tags;
        this.seller = seller;
        this.manufacturer = manufacturer;
    }
    
    /**
     * Getter for scores list
     * @return current value of scores list
     */
    public List<EmbeddedProperty> getScores() {
        return scores;
    }

    /**
     * Setter for scores list
     * @param scores new value for scores list
     */
    public void setScores(List<EmbeddedProperty> scores) {
        this.scores = scores;
    }

    /**
     * Getter for brief
     * @return value of brief field
     */
    public String getBrief() {
        return brief;
    }

    /**
     * Setter for brief field
     * @param brief new value for brief field
     */
    public void setBrief(String brief) {
        this.brief = brief;
    }

    /**
     * Getter for tags list
     * @return current value of tags list
     */
    public List<EmbeddedTag> getTags() {
        return tags;
    }

    /**
     * Setter for tags field
     * @param tags new value for tags field
     */
    public void setTags(List<EmbeddedTag> tags) {
        this.tags = tags;
    }

    /**
     * Getter for prices list
     * @return current value for prices list
     */
    public List<EmbeddedPrice> getPrices() {
        return prices;
    }

    /**
     * Setter for prices field
     * @param prices new value for prices field
     */
    public void setPrices(List<EmbeddedPrice> prices) {
        this.prices = prices;
    }

    /**
     * Getter for seller field
     * @return seller document
     */
    public EmbeddedCompany getSeller() {
        return seller;
    }

    /**
     * Setter for seller field
     * @param seller new value for seller field
     */
    public void setSeller(EmbeddedCompany seller) {
        this.seller = seller;
    }

    /**
     * Getter for manufacturer field
     * @return manufacturer document
     */
    public EmbeddedCompany getManufacturer() {
        return manufacturer;
    }

    /**
     * Setter for manufacturer field
     * @param manufacturer new value for manufacturer document
     */
    public void setManufacturer(EmbeddedCompany manufacturer) {
        this.manufacturer = manufacturer;
    }

    /**
     * Getter for catalog field
     * @return current value of catalog field
     */
    public EmbeddedCatalog getCatalog() {
        return catalog;
    }

    /**
     * Setter for catalog field
     * @param catalog new value for catalog field
     */
    public void setCatalog(EmbeddedCatalog catalog) {
        this.catalog = catalog;
    }

    /**
     * Getter for categories list
     * @return current value for categiries field
     */
    public List<EmbeddedCategory> getCategories() {
        return categories;
    }

    /**
     * Setter for categoriese list
     * @param categories new value for categories list
     */
    public void setCategories(List<EmbeddedCategory> categories) {
        this.categories = categories;
    }

    /**
     * Getter for properties list
     * @return current value for properties list
     */
    public List<EmbeddedProperty> getProperties() {
        return properties;
    }

    /**
     * Setter properties list
     * @param properties new value for peorpties list
     */
    public void setProperties(List<EmbeddedProperty> properties) {
        this.properties = properties;
    }

    /**
     * Getter for requirements list
     * @return current value of requirements list
     */
    public List<EmbeddedProperty> getRequirements() {
        return requirements;
    }

    /**
     * Setter for requirements list
     * @param requirements new value for requirements list
     */
    public void setRequirements(List<EmbeddedProperty> requirements) {
        this.requirements = requirements;
    }

    /**
     * Getter for version field
     * @return current value version field
     */
    public Long getVersion() {
        return version;
    }

    /**
     * Setter for version field
     * @param version new value for version field
     */
    public void setVersion(Long version) {
        this.version = version;
    }
}
