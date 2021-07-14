package synthwave.services.core;

import synthwave.dto.PropertyDTO;
import platform.exceptions.DataException;
import synthwave.models.mongodb.embeddeds.EmbeddedProperty;
import synthwave.repositories.mongodb.v1.UserProfileRepository;
import synthwave.services.base.BaseDocumentService;
import platform.utils.helpers.ParamsManager;
import dev.morphia.Datastore;
import java.util.Arrays;
import java.util.List;
import org.bson.types.ObjectId;
import spark.Request;


/**
 * Base class for work with profile property list
 * @author small-entropy
 */
public abstract class CoreUserProfileService 
        extends BaseDocumentService<UserProfileRepository> {
    
    public CoreUserProfileService(Datastore datastore, List<String> blacList) {
        super(
                datastore,
                new UserProfileRepository(datastore, blacList),
                new String[] {},
                new String[] {},
                new String[] {}
        );
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
    
    /**
     * Method for create user by reqeuet body
     * @param request Spark request object
     * @return created user property
     * @throws DataException throw if con not be found user or property document
     */
    public EmbeddedProperty createUserProperty( Request request) 
            throws DataException {
        ObjectId userId = ParamsManager.getUserId(request);
        PropertyDTO propertyDTO = PropertyDTO.build(
                request, 
                PropertyDTO.class
        ); 
        return getRepository().createUserProperty(userId, propertyDTO);
    }
    
    /**
     * Method for get user profile properties
     * @param request Spark request object
     * @return list of user properties
     * @throws DataException throw if con not be found user or profile
     *                       field is empty
     */
    public List<EmbeddedProperty> getUserProfile(Request request) 
            throws DataException {
        // Get user ID param from request URL
        ObjectId userId = ParamsManager.getUserId(request);
        return getRepository().getList(userId);
    }
    
    /**
     * Method for get user profile property by id
     * @param request Spark request object
     * @return founded user property
     * @throws DataException throw if con not be found user or property document
     */
    public EmbeddedProperty getUserProfilePropertyById(Request request) 
            throws DataException {
        ObjectId userId = ParamsManager.getUserId(request);
        ObjectId propertyId = ParamsManager.getPropertyId(request);
        return getRepository().getUserPropertyById(propertyId, userId);
    }
    
    /**
     * Method for update profile user property by request body
     * @param request Spark request object
     * @param profileRepository profile datasource object
     * @return updated property document
     * @throws DataException throw if con not be found user or property document
     */
    protected EmbeddedProperty updateUserProperty(Request request) 
            throws DataException {
        ObjectId userId = ParamsManager.getUserId(request);
        ObjectId propertyId = ParamsManager.getPropertyId(request);
        PropertyDTO propertyDTO = PropertyDTO.build(
                request, 
                PropertyDTO.class
        );
        return getRepository().updateUserProperty(
                propertyId, 
                userId, 
                propertyDTO
        );

    }
    
    /**
     * Method for delete profile user property from list by request params data
     * @param request Spark requeset object
     * @return actual profule value
     * @throws DataException throw if con not be found user or property document
     */
    protected List<EmbeddedProperty> deleteUserProfileProperty(Request request) 
            throws DataException {
        ObjectId userId = ParamsManager.getUserId(request);
        ObjectId propertyId = ParamsManager.getPropertyId(request);
        return getRepository().removeProperty(propertyId, userId);
    }
}
