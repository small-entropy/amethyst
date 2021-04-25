package Utils.constants;

public enum UsersParams {
    ID("id"),
    PROPERTY_ID("property_id"),
    RIGHT_ID("right_id");

    private final String name;

    UsersParams(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
