package core.constants;

public enum HttpErrors {
    INTERNAL_SERVER_ERROR(500, "Internal server error"),
    BAD_REQUEST(400, "Bad reqeust"),
    NOT_FOUND(404, "Not found"),
    UNAUTHORIZED(401, "Unauthorized user"),
    CONFLICT(409, "Conflict with sent data"),
    NOT_ACCEPTABLE(406, "Not Acceptable"),
    NOT_IMPLEMENTED(501, "Not Implemented"); 
    private final int code;
    private final String message;
    HttpErrors(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}