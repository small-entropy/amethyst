package Utils;

public class ErrorResponse<T> {
    private String status = "error";
    private String message;
    private T error;
    private StandardMeta meta;

    public ErrorResponse() {}

    public ErrorResponse(String message) {
        this.message = message;
    }

    public ErrorResponse(String message, T error) {
        this.message = message;
        this.error = error;
    }

    public ErrorResponse(String message, T error, StandardMeta meta) {
        this.message = message;
        this.error = error;
        this.meta = meta;
    }
}
