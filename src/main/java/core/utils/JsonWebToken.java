package core.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.bson.types.ObjectId;

/**
 * Class for work with JWT
 */
public class JsonWebToken {

    /** Salt for encode/decode JWT */
    private static final String SALT = "super#@!$ecretSa|t";
    /** Field for user UUID in claims */
    private static final String CLAIMS_FIELDS_ID = "id";
    /** Field for user username in claims */
    private static final String CLAIMS_FIELDS_USERNAME = "username";
    /** Name of auth type */
    private static final String AUTH_ISSUE = "auth0";

    /**
     * Method for get algorithm for decode/encode token
     * @return algorithm for decode & encode token
     */
    private static Algorithm getAlgorithm() {
        return Algorithm.HMAC256(SALT);
    }

    /**
     * Method for encode token
     * @return JWT as string
     */
    public static String encode(String username, String id) {
        return JWT.create()
                .withClaim(CLAIMS_FIELDS_ID, id)
                .withClaim(CLAIMS_FIELDS_USERNAME, username)
                .withIssuer(AUTH_ISSUE)
                .sign(getAlgorithm());
    }

    /**
     * Method for get decode JWT
     * @param token token string
     * @return verified & decoded token
     */
    public static DecodedJWT decode(String token) {
        // Create JWTVerifier object
        JWTVerifier verifier = JWT.require(getAlgorithm())
                .withIssuer(JsonWebToken.AUTH_ISSUE)
                .build();
        // Return result verifier
        return verifier.verify(token);
    }

    /**
     * Method for return user id (as ObjectId) from token
     * @param token user token
     * @return user id
     */
    public static ObjectId getIdFromToken(String token) {
        DecodedJWT decoded = JsonWebToken.decode(token);
        String claimId = decoded.getClaim("id").asString();
        return new ObjectId(claimId);
    }
}
