package Utils.constants;

/**
 *
 * @author small-entropey
 */
public enum ResponseMessages {
    // Users roures messages
    USERS_LIST("User list successfully founded"),
    USER("Successfully found user"),
    USER_DEACTIVATED("Successfully marked to remove"),
    // Authorization messages
    REGISTERED("User successfully registered"),
    AUTOLOGIN("Successfully login by token"),
    LOGIN("Login is success"),
    LOGOUT("User successfully logout"),
    PASSWORD_CHANGED("Password successfully changes"),
    // Catalogs messages
    CATALOGS_LIST("Successfully get list of catalogs"),
    CATALOG("Successfully get catalog"),
    CATALOG_CREATED("Successfully created catalog"),
    CATALOG_UPDATED("Successfully catapog updated"),
    CATALOG_DELETED("Successfully deleted catalog"),
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
    CATEGORIES("Successfully get categories"),
    CATEGORY("Successfully get category"),
    CATEGORY_CREATED("Successfully created category"),
    CATEGORY_UPDATED("Successfully updated category"),
    CATEGORY_DELETED("Successfulle deleted category"),
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
    TAG_DELETED("Successfully deleted tag"),
    // Comapnites
    COMPANIES("Successfull get companies"),
    COMPANY("Successfull get tag"),
    COMPANY_CREATED("Successfull created company"),
    COMPANY_UPDATED("Successfull updated company"),
    COMPANY_DELETED("Successfull deleted company");
    // Property for text message
    private final String message;

    ResponseMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
