package synthwave.repositories.morphia;

import synthwave.filters.UsersFilter;
import synthwave.models.morphia.extend.User;
import engine.repositories.morphia.BasePropertyRepository;
import dev.morphia.Datastore;
import java.util.List;

import org.bson.types.ObjectId;

/**
 * Class of repository for profile documents
 * @author small-entropy
 */
public class UserProfileRepository 
	extends BasePropertyRepository<User, UsersFilter, UsersRepository> {
    
    /**
     * Constructor for source
     * @param datastore Morphia datastore object
     * @param blaclList blaclist for profile documents
     */
    public UserProfileRepository(
    		Datastore datastore, 
    		List<String> blacklist
    ) {
    	super("profile", blacklist, new UsersRepository(datastore));
    	
    }
    
    @Override
    public User findOneById(ObjectId entityId, String[] excludes) {
    	UsersFilter filter = new UsersFilter(entityId, excludes);
    	return this.getRepository().findOneById(filter);
    }
    
    @Override
    public void save(User entity) {
    	this.getRepository().save(entity);
    }
}
