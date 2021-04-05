package Responses;

public class StandardResponse<D> extends AbstractResponse {
    private D data;

    public StandardResponse(String status) {
        super(status);
    }

    public StandardResponse(String status, String message) {
        super(status, message);
    }

    public StandardResponse(String status, String message, D data) {
        super(status, message);
        this.data = data;
    }

    public StandardResponse(String status, String message, D data, StandardMeta meta) {
        super(status, message, meta);
        this.data = data;
    }

    public D getData() {
        return data;
    }
}
