// Import UserController class
import synthwave.applications.RESTSynthwave;


/** Class for fun applications */
public class Runner {

    public static boolean STORE_NULLS = true;
    public static boolean STORE_EMPTIES = true;
    public static String MODELS_PATH = "synthwave.models.mongodb.standalones";
    public static String DB_NAME = "Amethyst";
    public static String ORIGINS = "*";
    public static String METHODS = "GET, POST, PUT, DELETE, OPTIONS";
    public static String HEADERS = "Content-Type, api_key, Authorization";
    public static int PORT = 4567;
    
    /**
     * Main function (run by start project)
     * @param args method arguments
     */
    public static void main(String[] args) {
        new RESTSynthwave(
                STORE_NULLS, 
                STORE_EMPTIES, 
                MODELS_PATH, 
                DB_NAME, 
                ORIGINS, 
                HEADERS, 
                METHODS,
                PORT
        ).run();
    }
}
