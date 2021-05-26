/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sources.Core;

import Sources.Abstract.AbstractSource;
import Filters.Filter;
import dev.morphia.Datastore;
import dev.morphia.query.FindOptions;
import static dev.morphia.query.experimental.filters.Filters.and;
import static dev.morphia.query.experimental.filters.Filters.eq;
import java.util.List;
import org.bson.types.ObjectId;

/**
 * Class for work with Morphia as data source
 * @author entropy
 * @param <M> model type
 * @param <F> filter type
 * @param <D> data transfer object ytpe
 */
public class MorphiaSource<M, F extends Filter, D> extends AbstractSource<Datastore, M, ObjectId, F, D>{
    
    /**
     * Constructor for Morphia datastore source object
     * @param datastore Morphia datastore object
     * @param modelClass class of model collection
     */
    public MorphiaSource(Datastore datastore, Class<M> modelClass) {
        super(datastore, modelClass);
    }
    
    /**
     * Method for get all dcouments by owner id
     * @param filter filter object
     * @return list of founded documents
     */
    public List<M> findAllByOwnerId(F filter) {
        FindOptions findOptions = new FindOptions()
                .projection()
                .exclude(filter.getExcludes())
                .skip(filter.getSkip())
                .limit(filter.getLimit());
        return getStore()
                .find(getModelClass())
                .filter(and(
                        eq("status", filter.getStatus()),
                        eq("owner.id", filter.getOwner())
                ))
                .iterator(findOptions)
                .toList();
                
    }
    
    /**
     * Method for get document by owner id and document id
     * @param filter filter object
     * @return fodunded document
     */
    public M findOneByOwnerAndId(F filter) {
        FindOptions findOptions = new FindOptions()
                .projection()
                .exclude(filter.getExcludes());
        return getStore()
                .find(getModelClass())
                .filter(and(
                        eq("status", filter.getStatus()),
                        eq("id", filter.getId()),
                        eq("owner.id", filter.getOwner())
                ))
                .first(findOptions);
                
    }
    
    /**
     * Method for find one document by name field
     * @param filter filter object
     * @return founded document
     */
    public M findOneByName(F filter) {
        FindOptions findOptions = new FindOptions()
                .projection()
                .exclude(filter.getExcludes());
        return getStore()
                .find(getModelClass())
                .filter(and(
                        eq("status", filter.getStatus()),
                        eq("name", filter.getName())
                ))
                .first(findOptions);
    }
    
    /**
     * Method for get list of entities by filters
     * @param filter filter object
     * @return founded entities
     */
    @Override
    public List<M> findAll(F filter) {
        // Craete find options by filter data
        FindOptions findOptions = new FindOptions()
                .projection()
                .exclude(filter.getExcludes())
                .skip(filter.getSkip())
                .limit(filter.getLimit());
        // Find in datastore by filter options & return list
        return getStore()
                .find(getModelClass())
                .filter(eq("status", filter.getStatus()))
                .iterator(findOptions)
                .toList();
    }
    
    /**
     * Method for find document by ID and status
     * @param filter filter object
     * @return 
     */
    @Override
    public M findOneById(F filter) {
        FindOptions findOptions = new FindOptions()
                .projection()
                .exclude(filter.getExcludes());
        return getStore()
                .find(getModelClass())
                .filter(and(
                        eq("status", filter.getStatus()),
                        eq("id", filter.getId())
                ))
                .first(findOptions);
    }
    
    /**
     * Template for method create entity in collection
     * @param dto entity data transfer object
     * @return created document
     */
    @Override
    public M create(D dto) {
        return null;
    }
    
    /**
     * Method for save document in datastore
     * @param data data for saving
     */
    @Override
    public void save(M data) {
        getStore().save(data);
    }
}
