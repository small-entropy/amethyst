/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Datasources;

import DataTransferObjects.UserDTO;
import Models.User;
import dev.morphia.Datastore;
import dev.morphia.query.FindOptions;
import static dev.morphia.query.experimental.filters.Filters.eq;
import static dev.morphia.query.experimental.filters.Filters.and;
import dev.morphia.query.internal.MorphiaCursor;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author igrav
 */
public class UserDatasource extends AbstractDatasource<Datastore, User, ObjectId> {

    public UserDatasource(Datastore datastore) {
        super(datastore);
    }
    
    @Override
    public List<User> findAll(int skip, int limit, String[] excludes) {
        FindOptions findOptions = new FindOptions()
                .projection()
                .exclude(excludes)
                .skip(skip)
                .limit(limit);
        return getDatastore()
                .find(User.class)
                .filter(eq("status", "active"))
                .iterator(findOptions)
                .toList();
    }
    
    @Override
    public User findOne(ObjectId id, String[] excludes) {
        FindOptions findOptions = new FindOptions()
                .projection()
                .exclude(excludes);
        return getDatastore()
                .find(User.class)
                .filter(and(
                        eq("id", id),
                        eq("status", "active")
                ))
                .first(findOptions);
    }
}
