package engine.models.morphia.simple;

import core.models.morphia.standalones.Standalone;
import core.models.morphia.embeddeds.EmbeddedOwner;
import org.bson.types.ObjectId;

/**
 * Abstract class for Amethyst abstract document
 * @author small-entropy
 */
public abstract class Document extends Standalone {
   
    /** Property for name of document */
    private String name;
    /** Property for document title */
    private String title;
    /** Property for document description */
    private String description;
    /** Property for document owner */
    private EmbeddedOwner owner;
    /**
     * Main constructor for document (if no data send)
     */
    public Document() {
        super();
    }
    
    /**
     * Constructor by id for document
     * @param id document id
     */
    public Document(ObjectId id) {
        super(id);
    }

    /**
     * Constructor for document by all data (without id)
     * @param name document name
     * @param title document title
     * @param description document description
     * @param owner document owner
     */
    public Document(
            String name, 
            String title, 
            String description, 
            EmbeddedOwner owner
    ) {
        super();
        this.name = name;
        this.title = title;
        this.description = description;
        this.owner = owner;
        this.setStatus("active");
    }
    
    /**
     * Constructor for document by all data
     * @param id document id
     * @param name document name
     * @param title document title
     * @param description document description
     * @param owner document owner
     */
    public Document(
            ObjectId id,
            String name, 
            String title, 
            String description, 
            EmbeddedOwner owner
    ) {
        super(id);
        this.name = name;
        this.title = title;
        this.description = description;
        this.owner = owner;;
    }
    
    /**
     * Document constructor for document by name, title & owner
     * @param name document name
     * @param title document title
     * @param owner document owner
     */
    public Document(String name, String title, EmbeddedOwner owner) {
        super();
        this.name = name;
        this.title = title;
        this.owner = owner;
        this.setStatus("active");
    }
    
    /**
     * Document constructor for document by id, name, title & owner
     * @param id document id
     * @param name document name
     * @param title document title
     * @param owner document owner
     */
     public Document(
             ObjectId id, 
             String name, 
             String title, 
             EmbeddedOwner owner
     ) {
        super(id);
        this.name = name;
        this.title = title;
        this.owner = owner;
    }

    /**
     * Getter for name property
     * @return current value of document name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for document name
     * @param name new value for document name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for document description
     * @return current value fo document description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter for document description
     * @param description new value for document description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter for title document
     * @return current value of document title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter for document titile
     * @param title new value for document title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Getter for document owner
     * @return current value of document owner
     */
    public EmbeddedOwner getOwner() {
        return owner;
    }

    /**
     * Setter for document owner
     * @param owner new value for document owner
     */
    public void setOwner(EmbeddedOwner owner) {
        this.owner = owner;
    }
}
