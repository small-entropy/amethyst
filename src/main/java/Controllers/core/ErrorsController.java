package Controllers.core;
import Exceptions.AccessException;
import Exceptions.AuthorizationException;
import Exceptions.DataException;
import Exceptions.TokenException;

import static spark.Spark.*;


/**
 * Class controller for errors handling
 * @author small-entropy
 */
public class ErrorsController extends CoreErrorsController {

    /**
     * Method for custom errors handlers
     */
    public static void errors_Custom() {
        
        // Custom exception handler for TokenException error
        exception(TokenException.class, (error, req, res) -> {
            // Get status code by exception message
            int statusCode = switch (error.getMessage()) {
                case "NotSend" -> HttpErrors.UNAUTHORIZED.getCode();
                case "NotEquals" -> HttpErrors.CONFLICT.getCode();
                default -> HttpErrors.INTERNAL_SERVER_ERROR.getCode();
            };
            ErrorsController.sendError(res, statusCode, error);
        });
        
        // Custom exception handler for DataException
        exception(DataException.class, (error, req, res) -> {
            int statusCode = switch (error.getMessage()) {
                case "NotFound" -> HttpErrors.NOT_FOUND.getCode();
                case "CanNotCreate" -> HttpErrors.CONFLICT.getCode();
                case "CanNotUpdate" -> HttpErrors.BAD_REQUEST.getCode();
                case "NotSendParams" -> HttpErrors.BAD_REQUEST.getCode();
                default -> HttpErrors.INTERNAL_SERVER_ERROR.getCode();
            };
            ErrorsController.sendError(res, statusCode, error);
        });
        
        // Custom exception handler for AccessException
        exception(AccessException.class, (error, req, res) -> {
            int statusCode = switch (error.getMessage()) {
                case "CanNotRead",
                    "CanNotCreate",
                    "CanNotUpdate",
                    "CanNotDelete" -> HttpErrors.NOT_ACCEPTABLE.getCode();
                default -> HttpErrors.INTERNAL_SERVER_ERROR.getCode();
            };
            ErrorsController.sendError(res, statusCode, error);
        });
        
        // Custom exception handler for AuthorizationExceptions
        exception(AuthorizationException.class, (error, req, res) -> {
            int statusCode = switch (error.getMessage()) {
                case "UserNotFound" -> HttpErrors.NOT_FOUND.getCode();
                case "WrongPassword" -> HttpErrors.CONFLICT.getCode();
                default -> HttpErrors.INTERNAL_SERVER_ERROR.getCode();
            };
            ErrorsController.sendError(res, statusCode, error);
        });
    }
}
