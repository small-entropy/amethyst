package Models;
import at.favre.lib.crypto.bcrypt.BCrypt;
import org.bson.types.ObjectId;
import dev.morphia.annotations.*;
import java.lang.String;
import java.util.List;

@Entity("users_property")
class Property<T> {
    @Id
    private ObjectId id;
    private String key;
    private T value;

    Property() {}

    public Property(String key, T value) {
        this.key = key;
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public ObjectId getId() {
        return id;
    }
}

@Entity("users")
public class User {
    @Id
    private ObjectId id;
    @Indexed(options = @IndexOptions(unique = true))
    private String username;
    private String password;
    private List<String> issuedToken;
    private boolean active = true;

    User() {}

    /**
     * Constructor for user document
     * @param username user username
     * @param password user password
     */
    public User(String username, String password) {
        this.username = username;
        this.password = getHashedPassword(password);
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
    public boolean getActive() {
        return this.active;
    }

    /**
     * Method for deactivate user
     */
    public void deactivate() {
        this.active = false;
    }

    /**
     * Method for activate user
     */
    public void activate() {
        this.active = true;
    }
}
