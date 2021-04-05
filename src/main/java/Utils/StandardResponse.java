package Utils;

public class StandardResponse<D> {
    private String status;
    private String message;
    private D data;
    private Object error;
    private StandardMeta meta;

    public StandardResponse(String status) {
        this.status = status;
    }

    public StandardResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public StandardResponse(String status, String message, D data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public StandardResponse(String status, String message, D data, StandardMeta meta) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.meta = meta;
    }

    public StandardResponse(String status, String message, D data, Object error, StandardMeta meta) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.error = error;
        this.meta = meta;
    }

}
