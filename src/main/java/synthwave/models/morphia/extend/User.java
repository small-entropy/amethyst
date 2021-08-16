package synthwave.models.morphia.extend;
import engine.models.morphia.extend.StandaloneExtended;
import core.models.morphia.embeddeds.EmbeddedProperty;
import core.models.morphia.embeddeds.EmbeddedRight;
import core.exceptions.AuthorizationException;
import at.favre.lib.crypto.bcrypt.BCrypt;
import org.bson.types.ObjectId;
import dev.morphia.annotations.*;

import java.util.List;


@Entity("users")
@Indexes({
    @Index(
            fields = @Field("username"), 
            options = @IndexOptions(unique = true)
    ),
    @Index(fields = @Field("profile"))
})
public class User extends StandaloneExtended {

    private String username;
    private String password;
    private List<String> issuedToken;
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
        super(id, profile, properties);
        this.username = username;
        this.password = getHashedPassword(password);
        this.rights = rights;
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
