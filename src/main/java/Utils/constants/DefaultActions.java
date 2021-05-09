package Utils.constants;

public enum DefaultActions {
    CREATE("create"),
    READ("read"),
    UPDATE("update"),
    DELETE("delete");

    private final String name;

    DefaultActions(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
