package Utils;

// JSend answer standard
public class Answer<D, M> {
    private String status;
    private D data;
    private M meta;

    public Answer() {}

    public Answer(D data) {
        this.data = data;
        if (data == null) {
            this.status = "fail";
        } else {
            this.status = "success";
        }
    }

    public void setData(D data) {
        this.data = data;
    }

    public void setMeta(M meta) {
        this.meta = meta;
    }
}