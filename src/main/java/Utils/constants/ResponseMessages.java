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
    // Rights message
    RIGHTS("Successfully get user rights"),
    RIGHT("Successfullly get user rights"),
    RIGHT_CREATED("Successfully create user right"),
    RIGHT_UPDATED("User right successfully updated"),
    RIGHT_DELETED("Successfully removed user right");
    
    // Property for text message
    private final String message;

    ResponseMessages(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}