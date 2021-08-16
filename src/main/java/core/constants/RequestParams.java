package core.constants;

/**
 * Enum for request params
 * @author small-entropy
 */
public enum RequestParams {
    CATALOG_ID("catalog_id"),
    CATAGORY_ID("category_id"),
    USER_ID("user_id"),
    PROPERTY_ID("property_id"),
    RIGHT_ID("right_id"),
    COMPANY_ID("comapny_id"),
    TAGS_ID("tags_id");
    
    private final String name;

    RequestParams(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
