package synthwave.services.core.users;

import synthwave.filters.UsersFilter;
import core.exceptions.DataException;
import core.models.morphia.embeddeds.EmbeddedProperty;
import synthwave.models.morphia.extend.User;
import synthwave.repositories.morphia.UserPropertiesRepository;
import synthwave.repositories.morphia.UsersRepository;
import synthwave.services.abstracts.CRUDEmbeddedPropertyService;
import core.utils.ParamsManager;
import dev.morphia.Datastore;
import java.util.Arrays;
import java.util.List;
import org.bson.types.ObjectId;
import spark.Request;

/**
 * Base user properties service
 */
public abstract class CoreUserPropertyService 
        extends CRUDEmbeddedPropertyService<User, UsersFilter, UsersRepository, UserPropertiesRepository> {
    
    public CoreUserPropertyService(Datastore datastore, List<String> blaclist) {
        super(datastore, new UserPropertiesRepository(datastore, blaclist));
    }

    /**
     * Method for get default properties for create user
     * @return list of default user properties
     */
    public static List<EmbeddedProperty> getDefaultUserProperty() {
        EmbeddedProperty banned = new EmbeddedProperty("banned", false);
        return Arrays.asList(banned);
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
}
