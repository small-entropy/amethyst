package Controllers.common;
import Exceptions.AccessException;
import Exceptions.DataException;
import Exceptions.TokenException;
import Responses.ErrorResponse;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.google.gson.Gson;
import com.mongodb.MongoWriteException;
import static spark.Spark.*;


/**
 * Enum with Http Codes
 */
enum HttpErrors {
    INTERNAL_SERVER_ERROR(500, "Internal server error"),
    NOT_FOUND(404, "Not found"),
    UNAUTHORIZED(401, "Unauthorized user"),
    CONFLICT(409, "Conflict with sent data"),
    NOT_ACCEPTABLE(406, "Not Acceptable");
    private int code;
    private String message;
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

/**
 * Class controller for errors handling
 */
public class ErrorsController {

    /** Content Type for response */
    final static String RESPONSE_TYPE = "application/json";

    /** Instance of GSON object */
    private static final Gson GSON = new Gson();

    /**
     * Methods for default errors handlers
     */
    public static void errors_InternalServerError() {
        // Error handler for internal server error
        internalServerError((req, res) -> {
            res.type(ErrorsController.RESPONSE_TYPE);
            String message = HttpErrors.INTERNAL_SERVER_ERROR.getMessage();
            ErrorResponse response = new ErrorResponse(message, null);
            return ErrorsController.GSON.toJson(response);
        });

        // Error handler for not found error
        notFound((req, res) -> {
            res.type(ErrorsController.RESPONSE_TYPE);
            String message = HttpErrors.NOT_FOUND.getMessage();
            ErrorResponse response = new ErrorResponse(message, null);
            return ErrorsController.GSON.toJson(response);
        });
    }

    /**
     * Method for custom errors handlers
     */
    public static void errors_Custom() {
        // Custom exception handler for MongoWriteException error
        exception(MongoWriteException.class, (error, req, res) -> {
            res.status(HttpErrors.INTERNAL_SERVER_ERROR.getCode());
            res.type(ErrorsController.RESPONSE_TYPE);
            ErrorResponse response = new ErrorResponse(error.getMessage(), null);
            res.body(ErrorsController.GSON.toJson(response));
        });

        // Custom exception handler for AlgorithmMismatchException error
        exception(AlgorithmMismatchException.class, (error, req, res) -> {
            res.status(HttpErrors.INTERNAL_SERVER_ERROR.getCode());
            res.type(ErrorsController.RESPONSE_TYPE);
            ErrorResponse response = new ErrorResponse(error.getMessage(), null);
            res.body(ErrorsController.GSON.toJson(response));
        });

        // Custom exception handler for TokenException error
        exception(TokenException.class, (error, req, res) -> {
            // Get status code by exception message
            int statusCode = switch (error.getMessage()) {
                case "NotSend" -> HttpErrors.UNAUTHORIZED.getCode();
                case "NotEquals" -> HttpErrors.CONFLICT.getCode();
                default -> HttpErrors.INTERNAL_SERVER_ERROR.getCode();
            };
            res.status(statusCode);
            // Get error message
            String message = error.getCause().getMessage();
            ErrorResponse response = new ErrorResponse(message, null);
            res.body(ErrorsController.GSON.toJson(response));
        });
        // Custom exception handler for DataException
        exception(DataException.class, (error, req, res) -> {
            int statusCode = switch (error.getMessage()) {
                case "NotFound" -> HttpErrors.NOT_FOUND.getCode();
                default -> HttpErrors.INTERNAL_SERVER_ERROR.getCode();
            };
            res.status(statusCode);
            String message = error.getCause().getMessage();
            ErrorResponse response = new ErrorResponse(message, null);
            res.body(ErrorsController.GSON.toJson(response));
        });
        // Custom exception handler for AccessException
        exception(AccessException.class, (error, req, res) -> {
            int statusCode = switch (error.getMessage()) {
                case "CanRead", "CanCreate" -> HttpErrors.NOT_ACCEPTABLE.getCode();
                default -> HttpErrors.INTERNAL_SERVER_ERROR.getCode();
            };
            res.status(statusCode);
            String message = error.getCause().getMessage();
            ErrorResponse response = new ErrorResponse(message, null);
            res.body(ErrorsController.GSON.toJson(response));
        });
    }
}
