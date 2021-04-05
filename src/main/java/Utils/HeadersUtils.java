package Utils;
import spark.Request;

enum HeadersFields {
    AUTHORIZATION("Authorization");

    private String value;

    HeadersFields(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

public class HeadersUtils {
    /**
     * Method get token from
     * @param request Spark request object
     * @return token from header
     */
    public static String getTokenFromHeaders(Request request){
        final String separator = " ";
        final short headerAuthIndex = 0;
        final String field = HeadersFields.AUTHORIZATION.getValue();
        return (request.headers(field) != null)
                ? request.headers(field).split(separator)[headerAuthIndex]
                : null;
    }
}
