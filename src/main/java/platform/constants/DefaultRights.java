package platform.constants;

/**
 * Enum with default rights names
 * @author small-entropy
 */
public enum DefaultRights {
    USERS("users_right"),
    CATALOGS("catalogs_right"),
    CATEGORIES("categories_right"),
    PRODUCTS("products_right"),
    REVIEW("review_right"),
    ORDERS("orders_right"),
    CART("carts_right"),
    TAGS("tags_right"),
    COMPANIES("companies_right"),
    PAGES("pages_right");

    private final String name;

    DefaultRights(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}