package synthwave.services.core.users;

import synthwave.filters.UsersFilter;
import platform.exceptions.DataException;
import synthwave.models.mongodb.embeddeds.EmbeddedProperty;
import synthwave.models.mongodb.standalones.User;
import synthwave.repositories.mongodb.v1.UserPropertiesRepository;
import synthwave.repositories.mongodb.v1.UsersRepository;
import synthwave.services.core.base.BaseEmbeddedPropertiesService;
import platform.utils.helpers.ParamsManager;
import dev.morphia.Datastore;
import java.util.Arrays;
import java.util.List;
import org.bson.types.ObjectId;
import spark.Request;

/**
 * Base user properties service
 */
public abstract class CoreUserPropertyService 
        extends BaseEmbeddedPropertiesService<User, UsersFilter, UsersRepository, UserPropertiesRepository> {
    
    public CoreUserPropertyService(Datastore datastore, List<String> blaclist) {
        super(
                datastore,
                new UserPropertiesRepository(datastore, blaclist)
        );
    }

    /**
     * Method for get default properties for create user
     * @return list of default user properties
     */
    protected static List<EmbeddedProperty> getDefaultUserProperty() {
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
