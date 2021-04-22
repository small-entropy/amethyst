package Responses;

public class SuccessResponse<D> extends StandardResponse<D> {
    public SuccessResponse(String message, D data, StandardMeta meta) {
        super("success", message, data, meta);
    }

    public SuccessResponse(String message, D data) {
        super("success", message, data);
    }
}
