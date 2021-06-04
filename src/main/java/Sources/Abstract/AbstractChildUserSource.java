/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sources.Abstract;

import Exceptions.DataException;
import Filters.UsersFilter;
import Models.Standalones.User;
import Sources.UsersSource;
import dev.morphia.Datastore;
import org.bson.types.ObjectId;

/**
 * Abstract class with additional method for user datasource
 * @author small-entropy
 */
public abstract class AbstractChildUserSource extends  UsersSource {
    
    /**
     * Constructor for datasouce
     * @param datastore Morphia datastore
     */
    public AbstractChildUserSource(Datastore datastore) {
        super(datastore);
    }
    
    /**
     * Method for get full user document
     * @param idString user id from request params
     * @return user document
     * @throws DataException throw if can not find list of user properties
     */
    public User getUserDocument(String idString) throws DataException {
        ObjectId id = new ObjectId(idString);
        String[] excludes = new String[]{};
        UsersFilter filter = new UsersFilter(id, excludes);
        User user = findOneById(filter);
        if (user != null) {
            return user;
        } else {
            Error error = new Error("Can not find user by id from request");
            throw new DataException("NotFound", error);
        }
    }
}
