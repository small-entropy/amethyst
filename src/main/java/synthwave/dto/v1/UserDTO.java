package synthwave.dto.v1;

import platform.dto.BaseDTO;
import synthwave.models.mongodb.embeddeds.EmbeddedProperty;
import synthwave.models.mongodb.embeddeds.EmbeddedRight;
import java.util.List;

public class UserDTO extends BaseDTO {
    private String username;
    private String password;
    private String oldPassword;
    private String newPassword;
    private List<EmbeddedRight> rights;
    private List<EmbeddedProperty> properties;
    private List<EmbeddedProperty> profile;
    
    UserDTO() {}
    
    public String getOldPassword() {
        return oldPassword;
    }
    
    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }
    
    public String getNewPassword() {
        return newPassword;
    }
    
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
    
    public List<EmbeddedRight> getRights() {
        return rights;
    }
    
    public void setRights(List<EmbeddedRight> rights) {
        this.rights = rights;
    }
    
    public List<EmbeddedProperty> getProperties() {
        return properties;
    }
    
    public void setProperties(List<EmbeddedProperty> properties) {
        this.properties = properties;
    }
    
    public List<EmbeddedProperty> getProfile() {
        return profile;
    }
    
    public void setProfile(List<EmbeddedProperty> profile) {
        this.profile = profile;
    }
}
