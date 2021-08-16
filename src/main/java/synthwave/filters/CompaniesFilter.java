package synthwave.filters;

import core.filters.Filter;
import org.bson.types.ObjectId;

/**
 * Class for company filter
 * @author small-entropy
 */
public class CompaniesFilter extends Filter {
    private String title;

    public CompaniesFilter() {
        super();
    }
    
    public CompaniesFilter(String title) {
        super();
        this.title = title;
    }
    
    public CompaniesFilter(ObjectId id, String[] excludes) {
        super(id, excludes);
    }
    
    public CompaniesFilter(int skip, int limit, String[] excludes) {
        super(skip, limit, excludes);
    }

    public CompaniesFilter(String[] excludes) {
        super(excludes);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
