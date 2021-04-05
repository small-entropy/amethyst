package Responses;

/**
 * Class for error response
 */
public class ErrorResponse extends AbstractResponse {
    public ErrorResponse(String message, StandardMeta meta) {
        super("error", message, meta);
    }
}
