package Utils.constants;

public enum DefaultRights {
    USERS("users_right"),
    CATALOGS("catalogs_right");

    private final String name;

    DefaultRights(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}