package platform.utils.responses;

/**
 * Class for response meta information object
 */
public class StandardMeta {
    // Count property
    private int count;
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
    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    /**
     * Setter for token property
     * @param token new value of token property
     */
    public void setToken(String token) { 
        this.token = token ; 
    }

    public String getToken() {
        return token;
    }
    
}
