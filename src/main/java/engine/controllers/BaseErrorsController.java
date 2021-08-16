package engine.controllers;

import engine.response.answer.Failed;
import com.auth0.jwt.exceptions.AlgorithmMismatchException;
import com.google.gson.Gson;
import com.mongodb.MongoWriteException;
import core.constants.HttpErrors;
import spark.Response;

import static spark.Spark.*;
import static spark.Spark.exception;

/**
 * Class for work with errors
 * @author small-entropy
 */
public class BaseErrorsController {

    /** Content Type for response */
    public static String RESPONSE_TYPE = "application/json";

    /** Instance of GSON object */
    public static final Gson GSON = new Gson();

    /**
     * Method for send default error response by status code & exception
     * @param response Spark response object
     * @param statusCode status code for response
     * @param error exception object
     */
    public static void sendError(
            Response response, 
            int statusCode, 
            Exception error
    ) {
        String message = error.getCause().getMessage();
        sendError(response, statusCode, message);
    }

    /**
     * Method for send default error response by status code & error message
     * @param response Spark response object
     * @param statusCode response status code
     * @param message error message
     */
    public static void sendError(
            Response response,
            int statusCode,
            String message
    ) {
        response.status(statusCode);
        response.type(RESPONSE_TYPE);
        Failed errorResponse = new Failed(message, null);
        response.body(GSON.toJson(errorResponse));
    }

    /**
     * Methods for default errors handlers
     */
    public static void errors_InternalServerError() {
        // Error handler for internal server error
        internalServerError((req, res) -> {
            res.type(RESPONSE_TYPE);
            String message = HttpErrors.INTERNAL_SERVER_ERROR.getMessage();
            Failed response = new Failed(message, null);
            return GSON.toJson(response);
        });

        // Error handler for not found error
        notFound((req, res) -> {
            res.type(RESPONSE_TYPE);
            String message = HttpErrors.NOT_FOUND.getMessage();
            Failed response = new Failed(message, null);
            return GSON.toJson(response);
        });
    }

    public static void errors_ExternalPackagesErrors() {
        // Custom exception handler for MongoWriteException error
        exception(MongoWriteException.class, (error, req, res) -> {
            sendError(
                    res,
                    HttpErrors.INTERNAL_SERVER_ERROR.getCode(),
                    error.getMessage()
            );
        });

        // Custom exception handler for AlgorithmMismatchException error
        exception(AlgorithmMismatchException.class, (error, req, res) -> {
            sendError(
                    res, 
                    HttpErrors.INTERNAL_SERVER_ERROR.getCode(),
                    error.getMessage()
            );
        });
        // Custom exception handler for IllegalArgumentException
        exception(IllegalArgumentException.class, (error, req, res) -> {
            sendError(
                    res,
                    HttpErrors.CONFLICT.getCode(),
                    error.getMessage()
            );
        });
    }
}
