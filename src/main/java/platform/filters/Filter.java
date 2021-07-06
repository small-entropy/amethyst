package platform.filters;
import org.bson.types.ObjectId;

/**
 * Class for abstract collection filter
 * @author small-entropy
 */
public abstract class Filter {
	/** Property for skip field */
	private int skip;
    /** Property for limit field */
    private int limit;
    /** Property for name field */
    private String name;
    /** Property for status string */
    private String status = "active";
    /** Property for object */
    private ObjectId id;
    /** Property for owner field */
    private ObjectId owner;
    /** Property for excludes fields */
    private String[] excludes =  new String[] {};
    
    /**
     * Default constructor for filter object. Create instance without any data
     */
    public Filter() {}
    
    /**
     * Constructor for filter object. Create instance by entity id
     * @param id value of entity id
     */
    public Filter(ObjectId id) {
        this.id = id;
    }
    
    /**
     * Constructor for filter object. Create instance by entity id & owner id
     * @param id value of entity id
     * @param owner value of owner id
     * @param excludes value of exclude fields
     */
    public Filter(ObjectId id, ObjectId owner, String[] excludes) {
        this.id = id;
        this.owner = owner;
        this.excludes = excludes;
    }
    
    /**
     * Constructor for filter object. Create instance by entity id & owner id
     * @param id value of entity id
     * @param owner value of owner id
     */
    public Filter(ObjectId id, ObjectId owner) {
        this.id = id;
        this.owner = owner;
    }
    
    /**
     * Constructor for filter object. Create instance by id & exclude fields
     * @param id value of entity id
     * @param excludes value of exclude fields
     */
    public Filter(ObjectId id, String[] excludes) {
        this.id = id;
        this.excludes = excludes;
    }
    
    /**
     * Constructor for filter object. Create instance by skip, limit & exclude fields
     * @param skip value of skip query
     * @param limit value of limit query
     * @param excludes value of exclude fields
     */
    public Filter(int skip, int limit, String[] excludes) {
        this.skip = skip;
        this.limit = limit;
        this.excludes = excludes;
    }
    
    /**
     * Constructor for filter object. Create instance by exclude fields 
     * @param excludes exclude fields
     */
    public Filter(String[] excludes) {
        this.excludes = excludes;
    }

    /**
     * Getter for name field
     * @return current value for name field
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for name field
     * @param name new value for name field
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Getter for owner field
     * @return current value for owner field
     */
    public ObjectId getOwner() {
        return owner;
    }

    /**
     * Setter for owner field
     * @param owner new value for owner field
     */
    public void setOwner(ObjectId owner) {
        this.owner = owner;
    }
    
    /**
     * Getter for skip field
     * @return new value for getter field
     */
    public int getSkip() {
        return skip;
    }
    
    /**
     * Setter value for skip field
     * @param skip new value for skip field
     */
    public void setSkip(int skip) {
        this.skip = skip;
    }
    
    /**
     * Getter for limit field
     * @return current value for limit field
     */
    public int getLimit() {
        return limit;
    }
    
    /**
     * Setter for limit field
     * @param limit new value for limit field
     */
    public void setLimit(int limit) {
        this.limit = limit;
    }
    
    /**
     * Getter for status field
     * @return current value for status field
     */
    public String getStatus() {
        return status;
    }
    
    /**
     * Setter for status field
     * @param status new value for status field
     */
    public void setStatus(String status) {
        this.status = status;
    }
    
    /**
     * Getter for id field
     * @return value of id field
     */
    public ObjectId getId() {
        return id;
    }
    
    /**
     * Setter for id field
     * @param id new value for id field
     */
    public void setId(ObjectId id) {
        this.id = id;
    }
    
    /**
     * Getter for exclude fields
     * @return value of exclude fields
     */
    public String[] getExcludes() {
        return excludes;
    }
    
    /**
     * Setter for exclude fields
     * @param excludes exclude fields
     */
    public void setExcludes(String[] excludes) {
        this.excludes = excludes;
    }
}
