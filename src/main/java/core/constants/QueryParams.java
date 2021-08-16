package core.constants;

/**
 *
 * @author igrav
 */
public enum QueryParams {
    SKIP("skip"),
    LIMIT("limit");
    
    private final String key;

    QueryParams(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }
}
