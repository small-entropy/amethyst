package Responses;

public class ErrorResponse {
    private String status;
    private String message;
    private StandardMeta meta;

    public ErrorResponse(String message, StandardMeta meta) {
        this.status = "error";
        this.message = message;
        this.meta = meta;
    }
}
