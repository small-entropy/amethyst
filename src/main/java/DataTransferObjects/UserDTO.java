package DataTransferObjects;

import Models.UserProperty;
import Models.UserRight;
import java.util.List;

public class UserDTO {
    private String username;
    private String password;
    private String oldPassword;
    private String newPassword;
    private List<UserRight> rights;
    private List<UserProperty> properties;
    private List<UserProperty> profile;
    
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
    
    public List<UserRight> getRights() {
        return rights;
    }
    
    public void setRights(List<UserRight> rights) {
        this.rights = rights;
    }
    
    public List<UserProperty> getProperties() {
        return properties;
    }
    
    public void setProperties(List<UserProperty> properties) {
        this.properties = properties;
    }
    
    public List<UserProperty> getProfile() {
        return profile;
    }
    
    public void setProfile(List<UserProperty> profile) {
        this.profile = profile;
    }
}
