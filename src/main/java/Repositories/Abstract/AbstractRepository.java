package Repositories.Abstract;

import Filters.Filter;
import java.util.List;
import Interfaces.RepositoryInterface;

/**
 * Class for work with some data source
 * @author entropy
 * @param <S> type of store
 * @param <M> type of model
 * @param <I> type of primary key
 * @param <F> type of filters
 * @param <D> type of data transfer objects
 */
public abstract class AbstractRepository<S, M, I, F extends Filter, D> 
        implements RepositoryInterface<S, M, I, F, D> 
{
    // Store property
    private final S store;
    // Model class property
    private final Class<M> modelClass;
    
    /**
     * Constructor for data source
     * @param store store object
     * @param model model 
     */
    public AbstractRepository(S store, Class<M> model) {
        this.store = store;
        this.modelClass = model;
    }
    
    /**
     * Getter for store property
     * @return store object
     */
    @Override
    public final S getStore() {
        return this.store;
    }
    
    /**
     * Getter for model class
     * @return model class
     */
    @Override
    public final Class<M> getModelClass() {
        return this.modelClass;
    }
    
    /**
     * Template method for get entities list by filter
     * @param filter filter object
     * @return list of entities
     */
    @Override
    public List<M> findAll(F filter) {
        return null;
    }
    
    /**
     * Method for find entity by id
     * @param filter filter object
     * @return founded entity
     */
    @Override
    public M findOneById(F filter) {
        return null;
    }
    
    /**
     * Template method for creation data in store
     * @param dto data transfer object
     * @return created object
     */
    @Override
    public M create(D dto) {
        return null;
    };
    
    /**
     * Template for method saving data in store
     * @param data 
     */
    @Override
    public void save(M data) {}
}
