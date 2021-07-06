package synthwave.filters;

import platform.filters.Filter;
import org.bson.types.ObjectId;

/**
 * Class for catalogs collection filter object
 * @author small-entropy
 */
public class CatalogsFilter extends Filter {
	
	/** Property of tile property */
    private String title;
    
    /**
     * Default constructor for catalogs filter
     */
    public CatalogsFilter() {
        super();
    }
    
    /**
     * Constructor for catalogs filter object. Create 
     * instance by entity id
     * @param id entity id
     */
    public CatalogsFilter(ObjectId id) {
        super(id);
    }
    
    /**
     * Constructor for catalogs filter object. Create instance by
     * entity id and owner id
     * @param id entity id
     * @param owner owner id
     */
    public CatalogsFilter(ObjectId id, ObjectId owner) {
        super(id, owner);
    }
    
    /**
     * Constructor for catalogs filter object. Create instance by
     * entity id, owner id & excludes fields
     * @param id entity id
     * @param owner owner id
     * @param excludes exclude fields
     */
    public CatalogsFilter(ObjectId id, ObjectId owner, String[] excludes) {
        super(id, owner, excludes);
    }
    
    /**
     * Constructor for catalogs filter object. Create instance by
     * entity id & exclude fields
     * @param id entity id
     * @param excludes exclude fields
     */
    public CatalogsFilter(ObjectId id, String[] excludes) {
        super(id, excludes);
    }
    
    /**
     * Constructor for catalogs filter object. Create instance by
     * skip query, limit query & exclude fields
     * @param skip value of skip query
     * @param limit value of limit query
     * @param excludes exclude fields
     */
    public CatalogsFilter(int skip, int limit, String[] excludes) {
        super(skip, limit, excludes);
    }
    
    /**
     * Constructor for catalogs filter object. Create instance by
     * entity name field & exclude fields
     * @param name value of name field
     * @param excludes exclude fields
     */
    public CatalogsFilter(String name, String[] excludes) {
        super(excludes);
        setName(name);
    }
    
    /**
     * Constructor for catalogs filter object. Create instance by
     * exclude fields
     * @param excludes exclude fields
     */
    public CatalogsFilter(String[] excludes) {
        super(excludes);
    }

    /**
     * Getter for title fields
     * @return value of title fields
     */
    public String getTitle() {
        return title;
    }

    /**
     * setter for title fields
     * @param title new value for title fields
     */
    public void setTitle(String title) {
        this.title = title;
    }
}
