package Utils.responses;

/**
 * Class for response meta information object
 */
public class StandardMeta {
    // Count property
    private Integer count;
    // Token property
    private String token;

    /**
     * Default object constructor
     */
    StandardMeta() {}

    /**
     * Setter for count property
     * @param count new value of count property
     */
    public void setCount(Integer count) {
        this.count = count;
    }

    /**
     * Setter for token property
     * @param token new value of token property
     */
    public void setToken(String token) { this.token = token ; }
}
