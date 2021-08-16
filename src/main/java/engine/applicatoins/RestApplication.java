package engine.applicatoins;

import core.applications.BaseApplication;
import engine.middlewares.CORS;
import engine.middlewares.ResponseTypeJSON;

import static spark.Spark.*;

/**
 * Class for create application for REST API
 * @author small-entropy
 */
public abstract class RestApplication <D, T> extends BaseApplication<D, T> {
    private final String origins;
    private final String headers;
    private final String methods;
    private final int port;

    /**
     * Default constructor for create REST application
     * @param datastore application datastore
     * @param transformer application transformer
     * @param origings application origins
     * @param headers application default headers
     * @param methods application methods
     */
    public RestApplication(
        D datastore,
        T transformer,
        String origings,
        String headers,
        String methods,
        int port
    ) {
        super(datastore, transformer);
        this.origins = origings;
        this.headers = headers;
        this.methods = methods;
        this.port = port;
    }

    /**
     * Getter for applications allowed headers
     * @return application allowed headers
     */
    public String getHeaders() {
        return headers;
    }

    /**
     * Getter for application allowed methods
     * @return application allowed methods
     */
    public String getMethods() {
        return methods;
    }

    /**
     * Applications allowed domens
     * @return application allowed domens
     */
    public String getOrigins() {
        return origins;
    }

    /**
     * Method for prepare datastore for use in routes
     */
    protected void datastorePrepare() {}

    /**
     * Method for initialize errors handlers
     */
    protected void errorsHandlersInit() {}

    /**
     * Method for initialize CORS
     */
    protected void corsInit() {
        CORS.enable(origins, methods, headers);
    }

    /**
     * Method for initialize application routes
     */
    protected void routesInit() {}

    /**
     * Method for set response type
     */
    protected void responseTypeInit() {
        // Callback after call all routes with /api/* pattern
        ResponseTypeJSON.afterCall();
    }

    @Override
    public final void run() {
        // Set maximum threads
        int maxThreads = Runtime.getRuntime().availableProcessors();
        // Set muinumum
        int minThreads = 2;
        // Set thread timeout
        int timeOutMillis = 30000;
        // Set server port
        port(port);
        // Set options to thread pool
        threadPool(maxThreads, minThreads, timeOutMillis);
        datastorePrepare();
        corsInit();
        path("/api", () -> routesInit());
        responseTypeInit();
        errorsHandlersInit();
        // Await init all threads
        awaitInitialization();
    }
}
