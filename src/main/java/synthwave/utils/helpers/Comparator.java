package synthwave.utils.helpers;

import synthwave.utils.jwt.JsonWebToken;
import platform.constants.RequestParams;
import com.auth0.jwt.interfaces.DecodedJWT;
import platform.utils.helpers.RequestUtils;
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
        String token = RequestUtils.getTokenByRequest(request);
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
