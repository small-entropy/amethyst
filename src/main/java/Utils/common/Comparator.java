package Utils.common;

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
}
