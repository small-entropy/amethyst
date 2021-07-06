package synthwave.dto;

import platform.dto.BaseDTO;

public class UserPropertyDTO extends BaseDTO {
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
