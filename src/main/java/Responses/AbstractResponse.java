package Responses;

/**
 * Common class for create response objects
 */
public abstract class AbstractResponse {
    // Response status
    private final String status;
    // Response message
    private String message;
    // Response meta object
    private StandardMeta meta;

    /**
     * Constructor for create response with only status
     * @param status response status
     */
    public AbstractResponse(String status) {
        this.status = status;
    }

    /**
     * Constructor for create response by status & message
     * @param status response status
     * @param message response message
     */
    public AbstractResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    /**
     * Constructor for create response by all data
     * @param status response status
     * @param message response message
     * @param meta response meta
     */
    public AbstractResponse(String status, String message, StandardMeta meta) {
        this.status = status;
        this.message = message;
        this.meta = meta;
    }

    /**
     * Getter for status property
     * @return value of status
     */
    public String getStatus() {
        return status;
    }

    /**
     * Getter for message property
     * @return value of message
     */
    public String getMessage() {
        return message;
    }

    /**
     * Getter for meta property
     * @return object meta
     */
    public StandardMeta getMeta() {
        return meta;
    }
}
