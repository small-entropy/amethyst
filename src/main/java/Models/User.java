package Models;
import at.favre.lib.crypto.bcrypt.BCrypt;
import com.google.gson.annotations.Expose;
import org.bson.types.ObjectId;
import dev.morphia.annotations.*;
import java.lang.String;
import java.util.List;


@Entity("users")
public class User {
    @Id
    private ObjectId id;
    @Indexed(options = @IndexOptions(unique = true))
    private String username;
    private String password;
    private List<String> issuedToken;
    private String status;
    private List<UserProperty> properties;
    private List<UserProperty> profile;
    private List<UserRight> rights;

    User() {}

    /**
     * Constructor for user document
     * @param username user username
     * @param password user password
     */
    public User(String username, String password) {
        this.username = username;
        this.password = getHashedPassword(password);
        this.status = "active";
    }

    public void setProfile(List<UserProperty> profile) {
        this.profile = profile;
    }

    /**
     * Getter for user right
     * @return list of user right
     */
    public List<UserRight> getRights() {
        return rights;
    }

    /**
     * Setter for user right property
     * @param rights new user list
     */
    public void setRights(List<UserRight> rights) {
        this.rights = rights;
    }

    /**
     *
     * @return
     */
    public List<UserProperty> getProfile() {
        return profile;
    }

    public void setProperties(List<UserProperty> properties) {
        this.properties = properties;
    }

    /**
     *
     * @param property
     */
    public void addProfileProperty(UserProperty property) {
        this.profile.add(property);
    }

    /**
     *
     * @return
     */
    public List<UserProperty> getProperties() {
        return properties;
    }

    /**
     *
     * @param property
     */
    public void addProperty(UserProperty property) {
        this.properties.add(property);
    }

    /**
     * Getter for username
     * @return value of username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Getter for user id
     * @return user id (as ObjectId)
     */
    public String getId() {
        return id.toString();
    }

    /**
     * Setter for user issued tokens
     * @param tokens list of tokens
     */
    public void setIssuedTokens(List<String> tokens) {
        this.issuedToken = tokens;
    }

    /**
     * Getter for issued tokens list
     * @return issued tokens list
     */
    public List<String> getIssuedTokens() {
        return this.issuedToken;
    }

    /**
     * Setter for password
     * @param notEncryptPassword not encrypt user password
     */
    public void setPassword(String notEncryptPassword) {
        this.password = getHashedPassword(notEncryptPassword);
    }

    /**
     * Method for regenerate user hash password by current password
     * (call if user password not enctypt)
     */
    public void reGeneratePassword() {
        password = getHashedPassword(password);
    }

    /**
     * Method for verify user password
     * @param passwordToCheck password to check
     * @return result verify password
     */
    public BCrypt.Result verifyPassword(String passwordToCheck) {
        return BCrypt.verifyer().verify(passwordToCheck.toCharArray(), password);
    }

    /**
     * Method for ecnrypt password
     * @param password not hashed password
     * @return password hash
     */
    private static String getHashedPassword(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

    /**
     * Getter for active state user
     * @return current value of active field
     */
    public String getStatus() {
        return this.status;
    }

    /**
     * Method for deactivate user
     */
    public void deactivate() {
        this.status = "inactive";
    }

    /**
     * Method for activate user
     */
    public void activate() {
        this.status = "active";
    }
}
