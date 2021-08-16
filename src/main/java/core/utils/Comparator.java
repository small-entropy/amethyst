package core.utils;

import core.constants.RequestParams;
import com.auth0.jwt.interfaces.DecodedJWT;
import spark.Request;


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
        String token = RequestManager.getTokenByRequest(request);
        String idParam = request.params(RequestParams.USER_ID.getName());
        if (token != null && idParam != null) {
            DecodedJWT decodedJWT = JsonWebToken.decode(token);
            String decodedId = decodedJWT.getClaim("id").asString();
            isTrusted = idParam.equals(decodedId);
        } else {
            isTrusted = false;
        }
        return isTrusted;
    }
}
