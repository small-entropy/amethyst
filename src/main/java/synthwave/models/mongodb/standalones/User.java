package synthwave.models.mongodb.standalones;
import synthwave.models.mongodb.embeddeds.EmbeddedProperty;
import synthwave.models.mongodb.embeddeds.EmbeddedRight;
import platform.exceptions.AuthorizationException;
import at.favre.lib.crypto.bcrypt.BCrypt;
import org.bson.types.ObjectId;
import dev.morphia.annotations.*;
import platform.models.mongodb.Standalone;

import java.util.List;


@Entity("users")
@Indexes({
    @Index(
            fields = @Field("username"), 
            options = @IndexOptions(unique = true)
    ),
    @Index(fields = @Field("profile"))
})
public class User extends Standalone {

    private String username;
    private String password;
    private List<String> issuedToken;
    private List<EmbeddedProperty> properties;
    private List<EmbeddedProperty> profile;
    private List<EmbeddedRight> rights;
    @Version private Long version;

    public Long getVersion() {
        return version;
    }


    public User() {
        super();
    }

    /**
     * Constructor for user document
     * @param username user username
     * @param password user password
     */
    public User(String username, String password) {
        super();
        this.username = username;
        this.password = getHashedPassword(password);
        
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
            List<EmbeddedProperty> properties,
            List<EmbeddedProperty> profile,
            List<EmbeddedRight> rights
    ) {
        super(id);
        this.username = username;
        this.password = getHashedPassword(password);
        this.properties = properties;
        this.profile = profile;
        this.rights = rights;
    }

    /**
     * Setter for profile field
     * @param profile list of user properties for profile
     */
    public void setProfile(List<EmbeddedProperty> profile) {
        this.profile = profile;
    }

    /**
     * Getter for user right
     * @return list of user right
     */
    public List<EmbeddedRight> getRights() {
        return rights;
    }

    /**
     * Setter for user right property
     * @param rights new user list
     */
    public void setRights(List<EmbeddedRight> rights) {
        this.rights = rights;
    }

    /**
     * Getter for profile property
     * @return value of profile property
     */
    public List<EmbeddedProperty> getProfile() {
        return profile;
    }

    /**
     * Setter for profile property
     * @param properties new value for profile property
     */
    public void setProperties(List<EmbeddedProperty> properties) {
        this.properties = properties;
    }

    /**
     * Method for add one property to profile user properties list
     * @param property user property for profile
     */
    public void addProfileProperty(EmbeddedProperty property) {
        this.profile.add(property);
    }

    /**
     * Method for get user properties field
     * @return list of user properties
     */
    public List<EmbeddedProperty> getProperties() {
        return properties;
    }

    /**
     * Add user property to user properties list
     * @param property new user property
     */
    public void addProperty(EmbeddedProperty property) {
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
    
    public void reGeneratePassword(
            String oldPassword, 
            String newPassword
    ) throws AuthorizationException {
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
     * Getter for first token from issued tokens list
     * @return first token from list
     */
    public String getFirstToken() {
        final int index = 0;
        return this.issuedToken.get(index);
    }
}
