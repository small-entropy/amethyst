package Models.Standalones;
import Models.Embeddeds.UserProperty;
import Models.Embeddeds.UserRight;
import Exceptions.AuthorizationException;
import at.favre.lib.crypto.bcrypt.BCrypt;
import org.bson.types.ObjectId;
import dev.morphia.annotations.*;
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
    @Version private Long version;

    public Long getVersion() {
        return version;
    }


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

    /**
     * Constructor for user document
     * @param id user id (ObjectId)
     * @param username user username
     * @param password user password
     * @param properties user properties
     * @param profile user profile
     * @param rights user rights
     */
    public User(
            ObjectId id,
            String username,
            String password,
            List<UserProperty> properties,
            List<UserProperty> profile,
            List<UserRight> rights
    ) {
        this.id = id;
        this.username = username;
        this.password = getHashedPassword(password);
        this.status = "active";
        this.properties = properties;
        this.profile = profile;
        this.rights = rights;
    }

    /**
     * Setter for profile field
     * @param profile list of user properties for profile
     */
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
     * Getter for profile property
     * @return value of profile property
     */
    public List<UserProperty> getProfile() {
        return profile;
    }

    /**
     * Setter for profile property
     * @param properties new value for profile property
     */
    public void setProperties(List<UserProperty> properties) {
        this.properties = properties;
    }

    /**
     * Method for add one property to profile user properties list
     * @param property user property for profile
     */
    public void addProfileProperty(UserProperty property) {
        this.profile.add(property);
    }

    /**
     * Method for get user properties field
     * @return list of user properties
     */
    public List<UserProperty> getProperties() {
        return properties;
    }

    /**
     * Add user property to user properties list
     * @param property new user property
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
    public ObjectId getId() {
        return id;
    }

    public String getStringifiedId() {
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
     * (call if user password not encrypt)
     */
    public void reGeneratePassword() {
        password = getHashedPassword(password);
    }
    
    public void reGeneratePassword(String oldPassword, String newPassword) throws AuthorizationException {
        boolean verify = verifyPassword(oldPassword).verified;
        if (verify) {
            password = getHashedPassword(newPassword);
        } else {
            Error error = new Error("Incorrect password");
            throw new AuthorizationException("WrongPassword", error);
        }
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
     * Method for encrypt password
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
