package synthwave.services.core.users;

import synthwave.filters.UsersFilter;
import core.models.morphia.embeddeds.EmbeddedProperty;
import synthwave.models.morphia.extend.User;
import synthwave.repositories.morphia.UserProfileRepository;
import synthwave.repositories.morphia.UsersRepository;
import synthwave.services.abstracts.CRUDEmbeddedPropertyService;
import dev.morphia.Datastore;
import core.exceptions.DataException;
import core.utils.ParamsManager;
import spark.Request;

import java.util.Arrays;
import java.util.List;

import org.bson.types.ObjectId;


/**
 * Base class for work with profile property list
 * @author small-entropy
 */
public abstract class CoreUserProfileService 
     extends CRUDEmbeddedPropertyService<User, UsersFilter, UsersRepository, UserProfileRepository> {
	
	/**
	 * Default constructor for core user profile service. Create
	 * instance by datastore & blacklist
	 * @param datastore Morphia datastore object
	 * @param blacList blaclist
	 */
    public CoreUserProfileService(
    		Datastore datastore, 
    		List<String> blacList
    ) {
        super(datastore, new UserProfileRepository(datastore, blacList));
    }
    
    /**
	 * Method for get entity id from request
	 * @param request Spark request object
	 * @return founded id
	 */
    @Override
	protected ObjectId getEntityIdFromRequest(Request request) throws DataException {
		return ParamsManager.getUserId(request);
	}

    /**
     * Method for get default profile properties list
     * @return list of user properties for profile
     */
    public static List<EmbeddedProperty> getDefaultProfile() {
        Long currentDateTime = System.currentTimeMillis();
        EmbeddedProperty registered = new EmbeddedProperty(
                "registered", 
                currentDateTime
        );
        return Arrays.asList(registered);
    }
}
