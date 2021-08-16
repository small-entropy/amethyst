package core.response.answers;

/**
 * Common class for create response objects
 */
public abstract class BaseAnswer {
    // Response status
    private final String status;
    // Response message
    private String message;
    // Response meta object
    private Meta meta;

    /**
     * Constructor for create response with only status
     * @param status response status
     */
    public BaseAnswer(String status) {
        this.status = status;
    }

    /**
     * Constructor for create response by status & message
     * @param status response status
     * @param message response message
     */
    public BaseAnswer(String status, String message) {
        this.status = status;
        this.message = message;
    }

    /**
     * Constructor for create response by all data
     * @param status response status
     * @param message response message
     * @param meta response meta
     */
    public BaseAnswer(String status, String message, Meta meta) {
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
    public Meta getMeta() {
        return meta;
    }
}
