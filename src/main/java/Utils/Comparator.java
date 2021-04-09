package Utils;

import Models.User;
import Models.UserProperty;
import com.auth0.jwt.interfaces.DecodedJWT;
import spark.Request;

import java.util.List;

/**
 * Class with some compare methods
 */
public class Comparator {

    /**
     * Method for compare user ID from url and from token
     * @param request Spark request object
     * @return result of compare
     */
    public static boolean id_fromParam_fromToken(Request request) {
        boolean isTrusted;
        String token = RequestUtils.getTokenByRequest(request);
        String idParam = request.params("id");
        if (token != null) {
            DecodedJWT decodedJWT = JsonWebToken.decode(token);
            String decodedId = decodedJWT.getClaim("id").asString();
            isTrusted = idParam.equals(decodedId);
        } else {
            isTrusted = false;
        }
        return isTrusted;
    }

    /**
     * Method for compare key and keys from properties
     * @param key name of new property
     * @param user user document
     * @return result of compare key and properties keys
     */
    public static boolean keyProperty_fromUser(String key, User user) {
        boolean hasProperty = false;
        List<UserProperty> properties = user.getProperties();
        for (UserProperty property : properties) {
            if (property.getKey().equals(key)) {
                hasProperty = true;
                break;
            }
        }
        return hasProperty;
    }
}
