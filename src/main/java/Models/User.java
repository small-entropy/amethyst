package Models;
import at.favre.lib.crypto.bcrypt.BCrypt;
import org.bson.types.ObjectId;
import dev.morphia.annotations.*;
import java.lang.String;
import java.util.List;

@Entity("users")
public class User {
    @Id
    private ObjectId _id;
    @Indexed(options = @IndexOptions(unique = true))
    private String username;
    private String password;
    private List<String> issuedToken;

    User() {}

    public User(String username, String password) {
        this.username = username;
        this.password = getHashedPassword(password);
    }

    public String getUsername() {
        return username;
    }

    public String get_id() {
        return _id.toString();
    }

    public void setIssuedTokens(List<String> tokens) {
        this.issuedToken = tokens;
    }

    public List<String> getIssuedTokens() {
        return this.issuedToken;
    }

    public void setPassword(String notEncryptPassword) {
        this.password = getHashedPassword(notEncryptPassword);
    }

    public void reGeneratePassword() {
        password = getHashedPassword(password);
    }

    public BCrypt.Result verifyPassword(String passwordToCheck) {
        return BCrypt.verifyer().verify(passwordToCheck.toCharArray(), password);
    }

    private static String getHashedPassword(String password) {
        return BCrypt.withDefaults().hashToString(12, password.toCharArray());
    }

}
