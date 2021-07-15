/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package synthwave.repositories.mongodb.v1;

import synthwave.filters.UsersFilter;
import synthwave.models.mongodb.standalones.User;
import synthwave.repositories.mongodb.base.BasePropertyRepository;
import dev.morphia.Datastore;
import java.util.List;

import org.bson.types.ObjectId;

/**
 * Class of source for user properties documents
 * @author small-entropy
 */
public class UserPropertiesRepository 
	extends BasePropertyRepository<User, UsersFilter, UsersRepository> {
    
    /**
     * Constructor for source
     * @param datastore Morphia datastore object
     * @param blaclList blaclist for profile documents
     */
    public UserPropertiesRepository(Datastore datastore, List<String> blaclList) {
        super("properties", blaclList, new UsersRepository(datastore));
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