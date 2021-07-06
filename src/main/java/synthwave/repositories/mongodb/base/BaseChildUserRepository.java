package synthwave.repositories.mongodb.base;

import platform.exceptions.DataException;
import synthwave.filters.UsersFilter;
import synthwave.models.mongodb.standalones.User;
import synthwave.repositories.mongodb.v1.UsersRepository;
import dev.morphia.Datastore;
import org.bson.types.ObjectId;

/**
 * Abstract class with additional method for user datasource
 * @author small-entropy
 */
public abstract class BaseChildUserRepository extends  UsersRepository {
    
    /**
     * Constructor for datasouce
     * @param datastore Morphia datastore
     */
    public BaseChildUserRepository(Datastore datastore) {
        super(datastore);
    }
    
    /**
     * Method for get full user document
     * @param userId user id from request params
     * @return user document
     * @throws DataException throw if can not find list of user properties
     */
    public User getUserDocument(ObjectId userId) throws DataException {
        String[] excludes = new String[]{};
        UsersFilter filter = new UsersFilter(userId, excludes);
        User user = findOneById(filter);
        if (user != null) {
            return user;
        } else {
            Error error = new Error("Can not find user by id from request");
            throw new DataException("NotFound", error);
        }
    }
}
