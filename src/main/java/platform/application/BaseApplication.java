package platform.application;

public abstract class BaseApplication<D, T> {
    private final D datastore;
    private final T transformer;

    public BaseApplication(D datastore, T transformer) {
        this.datastore = datastore;
        this.transformer = transformer;
    }

     /**
     * Getter for application datastore
     * @return application datastore
     */
    public D getDatastore() {
        return datastore;
    }

    /***
     * Getter for application transformer
     * @return application transformer
     */
    public T getTransformer() {
        return transformer;
    }

    /**
     * Method for run application
     */
    public void run() {}
}
