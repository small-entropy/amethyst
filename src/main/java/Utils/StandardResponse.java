package Utils;

public class StandardResponse<T> {
    private String status;
    private String message;
    private T data;
    private StandardMeta meta;

    public StandardResponse(String status) {
        this.status = status;
    }

    public StandardResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public StandardResponse(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public StandardResponse(String status, String message, T data, StandardMeta meta) {
        this.status = status;
        this.message = message;
        this.data = data;
        this.meta = meta;
    }

}
