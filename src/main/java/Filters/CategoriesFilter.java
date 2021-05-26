package Filters;

import org.bson.types.ObjectId;

/**
 * Class for categories filter
 * @author small-entropy
 */
public class CategoriesFilter extends Filter {
    private ObjectId catalog;
    
    public CategoriesFilter() {
        super();
    }
    
    public CategoriesFilter(ObjectId id, String[] excludes) {
        super(id, excludes);
    }
    
    public CategoriesFilter(int skip, int limit, String[] excludes) {
        super(skip, limit, excludes);
    }

    public ObjectId getCatalog() {
        return catalog;
    }

    public void setCatalog(ObjectId catalog) {
        this.catalog = catalog;
    }
}
