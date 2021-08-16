package core.constants;

/**
 * Enum with list constants for findAll method
 * @author small-entropy
 */
public enum ListConstants {
    SKIP(0),
    LIMIT(10);

    private final int value;
    
    ListConstants(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
