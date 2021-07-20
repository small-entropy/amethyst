package synthwave.services.core.users;

import synthwave.dto.PropertyDTO;
import platform.exceptions.DataException;
import synthwave.models.mongodb.embeddeds.EmbeddedProperty;
import synthwave.repositories.mongodb.v1.UserPropertiesRepository;
import synthwave.services.base.BaseDocumentService;
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
        extends BaseDocumentService<UserPropertiesRepository> {
    
    public CoreUserPropertyService(Datastore datastore, List<String> blaclist) {
        super(
                datastore,
                new UserPropertiesRepository(datastore, blaclist),
                new String[] {},
                new String[] {},
                new String[] {}
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
     * Method for create user property
     * @param request Spark request object
     * @param propertiesRepository user property datasource
     * @return user property
     * @throws DataException throw if con not be found user or property document
     */
    protected EmbeddedProperty createUserProperty(
            Request request
    ) throws DataException {
        ObjectId userId = ParamsManager.getUserId(request);
        PropertyDTO propertyDTO =  PropertyDTO.build(
                request, 
                PropertyDTO.class
        );
        return getRepository().createProperty(userId, propertyDTO);
    }

    /**
     * Get user properties by request
     * @param request Spark request object
     * @return user properties list
     * @throws DataException throw if con not be found user or property document
     */
    protected List<EmbeddedProperty> getUserProperties(Request request) 
            throws DataException {
        ObjectId userId = ParamsManager.getUserId(request);
        return getRepository().getPropertiesList(userId);
    }

    /**
     * Method for get user property by id
     * @param request Spark request object
     * @return founded user property
     * @throws DataException throw if con not be found user or property document
     */
    protected EmbeddedProperty getUserPropertyById(
            Request request
    ) throws DataException {
        ObjectId userId = ParamsManager.getUserId(request);
        ObjectId propertyId = ParamsManager.getPropertyId(request);
        return getRepository().getDocumentPropertyByEntityIdAndId(propertyId, userId);

    }

    /**
     * Method for update user property in user document
     * @param request Spark request object
     * @return updated user property
     * @throws DataException throw if con not be found user or property document
     */
    protected EmbeddedProperty updateUserProperty(Request request) 
            throws DataException {
        PropertyDTO propertyDTO = PropertyDTO.build(
                request, 
                PropertyDTO.class
        );
        ObjectId propertyId = ParamsManager.getPropertyId(request);
        ObjectId userId = ParamsManager.getUserId(request);
        return getRepository().updateProperty(
                propertyId, 
                userId, 
                propertyDTO
        );
    }
    
    /**
     * Method for remove user property by request params
     * @param request Spark request object
     * @return actual list of user properties
     * @throws DataException throw if con not be found user or property document
     */
    protected List<EmbeddedProperty> deleteUserProperty(Request request) 
            throws DataException {
        ObjectId userId = ParamsManager.getUserId(request);
        ObjectId propertyId = ParamsManager.getPropertyId(request);
        return getRepository().removeProperty(propertyId, userId);
    }
}
