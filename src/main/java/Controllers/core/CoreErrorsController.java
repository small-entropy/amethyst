package Controllers.core;

import Responses.ErrorResponse;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.google.gson.Gson;
import com.mongodb.MongoWriteException;

import static spark.Spark.*;
import static spark.Spark.exception;

public class CoreErrorsController {
    protected enum HttpErrors {
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

    /** Content Type for response */
    protected static String RESPONSE_TYPE = "application/json";

    /** Instance of GSON object */
    protected static final Gson GSON = new Gson();

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

    public static void errors_ExternalPackagesErrors() {
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
    }
}
