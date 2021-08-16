package core.dto;

public class PropertyDTO extends BaseDTO {
    private String key;
    private Object value;
    private boolean notPublic;

    PropertyDTO() {}

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }

    public boolean getNotPublic() {
        return notPublic;
    }
}
