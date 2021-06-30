package Utils.responses;

/**
 * Class for create success response object (always have a "success" status)
 * @param <D> type of data property
 */
public class SuccessResponse<D> extends StandardResponse<D> {
    /**
     * Constructor for response object with message, data & meta property
     * @param message response message
     * @param data data for response
     * @param meta meta for response
     */
    public SuccessResponse(String message, D data, StandardMeta meta) {
        super("success", message, data, meta);
    }

    /**
     * Constructor for response object with message & data
     * @param message response message
     * @param data response data
     */
    public SuccessResponse(String message, D data) {
        super("success", message, data);
    }
}
