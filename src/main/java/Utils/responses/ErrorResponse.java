package Utils.responses;

/**
 * Class for error response
 */
public class ErrorResponse extends AbstractResponse {
    /**
     * Constructor for error response by message & meta
     * @param message value of message
     * @param meta meta object
     */
    public ErrorResponse(String message, StandardMeta meta) {
        super("error", message, meta);
    }

    /**
     * Constructor for error response by only message
     * @param message value of message
     */
    public ErrorResponse(String message) {
        super("error", message);
    }
}
