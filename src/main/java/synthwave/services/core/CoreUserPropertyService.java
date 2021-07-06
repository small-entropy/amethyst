package synthwave.services.core;

import synthwave.dto.v1.UserPropertyDTO;
import platform.exceptions.DataException;
import synthwave.models.mongodb.embeddeds.EmbeddedProperty;
import synthwave.repositories.mongodb.v1.PropertiesRepository;
import platform.services.BaseDocumentService;
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
        extends BaseDocumentService<PropertiesRepository> {
    
    public CoreUserPropertyService(Datastore datastore, List<String> blaclist) {
        super(
                datastore,
                new PropertiesRepository(datastore, blaclist),
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
        UserPropertyDTO userPropertyDTO =  UserPropertyDTO.build(
                request, 
                UserPropertyDTO.class
        );
        return getRepository().createUserProperty(userId, userPropertyDTO);
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
        return getRepository().getList(userId);
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
        return getRepository().getUserPropertyById(propertyId, userId);

    }

    /**
     * Method for update user property in user document
     * @param request Spark request object
     * @return updated user property
     * @throws DataException throw if con not be found user or property document
     */
    protected EmbeddedProperty updateUserProperty(Request request) 
            throws DataException {
        UserPropertyDTO userPropertyDTO = UserPropertyDTO.build(
                request, 
                UserPropertyDTO.class
        );
        ObjectId propertyId = ParamsManager.getPropertyId(request);
        ObjectId userId = ParamsManager.getUserId(request);
        return getRepository().updateUserProperty(
                propertyId, 
                userId, 
                userPropertyDTO
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
