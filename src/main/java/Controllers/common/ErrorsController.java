package Controllers.common;
import Responses.ErrorResponse;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.google.gson.Gson;
import com.mongodb.MongoWriteException;
import static spark.Spark.*;

/**
 * Enum with Http Messages
 */
enum HttpErrorsMessage {
    INTERNAL_SERVER_ERROR("Custom 500 handling"),
    NOT_FOUND("Route not found");
    private String value;
    HttpErrorsMessage(String value) {
        this.value = value;
    }
    public String getValue() {
        return value;
    }
};

/**
 * Enum with Http Codes
 */
enum HttpErrorsCodes {
    INTERNAL_SERVER_ERROR(500);
    private int value;
    HttpErrorsCodes(int value) {
        this.value = value;
    }
    public int getValue() { return value; }
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
            String message = HttpErrorsMessage.INTERNAL_SERVER_ERROR.getValue();
            ErrorResponse response = new ErrorResponse(message, null);
            return ErrorsController.GSON.toJson(response);
        });

        // Error handler for not found error
        notFound((req, res) -> {
            res.type(ErrorsController.RESPONSE_TYPE);
            String message = HttpErrorsMessage.NOT_FOUND.getValue();
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
            res.status(HttpErrorsCodes.INTERNAL_SERVER_ERROR.getValue());
            res.type(ErrorsController.RESPONSE_TYPE);
            ErrorResponse response = new ErrorResponse(error.getMessage(), null);
            res.body(ErrorsController.GSON.toJson(response));
        });

        // Custom exception handler for AlgorithmMismatchException error
        exception(AlgorithmMismatchException.class, (error, req, res) -> {
            res.status(HttpErrorsCodes.INTERNAL_SERVER_ERROR.getValue());
            res.type(ErrorsController.RESPONSE_TYPE);
            ErrorResponse response = new ErrorResponse(error.getMessage(), null);
            res.body(ErrorsController.GSON.toJson(response));
        });
    }
}
