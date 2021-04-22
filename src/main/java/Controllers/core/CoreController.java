package Controllers.core;

public class CoreController {
    protected enum Statuses {
        SUCCESS("success"),
        FAILED("failed");
        private String status;
        Statuses(String status) {
            this.status = status;
        }

        public String getStatus() {
            return status;
        }
    }
}
