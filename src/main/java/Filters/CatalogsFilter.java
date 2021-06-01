package Filters;

import org.bson.types.ObjectId;

/**
 * Class for catalogs collection filter object
 * @author small-entropy
 */
public class CatalogsFilter extends Filter {
    private String tile;
    
    public CatalogsFilter() {
        super();
    }
    
    public CatalogsFilter(ObjectId id, String[] excludes) {
        super(id, excludes);
    }
    
    public CatalogsFilter(int skip, int limit, String[] excludes) {
        super(skip, limit, excludes);
    }
    
    public CatalogsFilter(String name, String[] excludes) {
        super(excludes);
        setName(name);
    }
    
    public CatalogsFilter(String[] excludes) {
        super(excludes);
    }

    public String getTile() {
        return tile;
    }

    public void setTile(String tile) {
        this.tile = tile;
    }
}
