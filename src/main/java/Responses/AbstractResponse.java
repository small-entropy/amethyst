package Responses;

public abstract class AbstractResponse {
    private String status;
    private String message;
    private StandardMeta meta;

    public AbstractResponse(String status) {
        this.status = status;
    }

    public AbstractResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public AbstractResponse(String status, String message, StandardMeta meta) {
        this.status = status;
        this.message = message;
        this.meta = meta;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public StandardMeta getMeta() {
        return meta;
    }
}
