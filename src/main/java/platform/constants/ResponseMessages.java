package platform.constants;

/**
 *
 * @author small-entropey
 */
public enum ResponseMessages {
    // Users roures messages
    USERS_LIST("User list successfully founded"),
    USER("Successfully found user"),
    USER_DEACTIVATED("Successfully marked to remove"),
    // Catalogs messages
    
    // Profile messages
    PROFILE("Successfully get user profile"),
    PROFILE_PROPERTY("Successfully get user profile property"),
    PROFILE_CREATED("Successfully created profile property"),
    PROFILE_UPDATED("Profile property successfully updated"),
    PROFILE_DELETED("Profile property removed"),
    // User properties messages
    USER_PROPERTIES("Successfully get user properties"),
    USER_PROPERTY("Successfully get user property"),
    USER_PROPERTY_CREATED("Successfully created user property"),
    USER_PROPERTY_UPDATED("User property successfully updated"),
    USER_PROPETY_DELETED("Successfully removed user property"),
    // Rights messages
    RIGHTS("Successfully get user rights"),
    RIGHT("Successfullly get user rights"),
    RIGHT_CREATED("Successfully create user right"),
    RIGHT_UPDATED("User right successfully updated"),
    RIGHT_DELETED("Successfully removed user right"),
    // Category messages
    
    // Products messages
    PRODUCTS("Successfully get products"),
    PRODUCT("Successfully get product"),
    PRODUCT_CREATED("Successfully created product"),
    PRODUCT_UPDATED("Successfully updated product"),
    PRODUCT_DELETED("Successfully deleted product"),
    // Tags
    TAGS("Successfully get tags"),
    TAG("Successfully get tag "),
    TAG_CREATED("Successfully created tag"),
    TAG_UPDATED("Successfully updated tag"),
    TAG_DELETED("Successfully deleted tag");

    // Property for text message
    private final String message;

    ResponseMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
