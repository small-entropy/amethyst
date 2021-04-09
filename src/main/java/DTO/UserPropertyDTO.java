package DTO;

public class UserPropertyDTO {
    private String key;
    private Object value;
    private boolean notPublic;

    UserPropertyDTO() {}

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
