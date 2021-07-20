package synthwave.services.core.users;

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
     * Method for create user by request body
     * @param request Spark request object
     * @return created user property
     * @throws DataException throw if can't be found user or 
     * 						 property document
     */
    public EmbeddedProperty createProperty(Request request) 
            throws DataException {
        ObjectId userId = ParamsManager.getUserId(request);
        PropertyDTO propertyDTO = PropertyDTO.build(
                request, 
                PropertyDTO.class
        ); 
        return getRepository().createProperty(userId, propertyDTO);
    }
    
    /**
     * Method for get user profile properties
     * @param request Spark request object
     * @return list of user properties
     * @throws DataException throw if con not be found user or profile
     *                       field is empty
     */
    public List<EmbeddedProperty> getProperties(Request request) 
            throws DataException {
        // Get user ID param from request URL
        ObjectId userId = ParamsManager.getUserId(request);
        return getRepository().getPropertiesList(userId);
    }
    
    /**
     * Method for get user profile property by id
     * @param request Spark request object
     * @return founded user property
     * @throws DataException throw if can't be found user or property document
     */
    public EmbeddedProperty getPropertyById(Request request) 
            throws DataException {
        ObjectId userId = ParamsManager.getUserId(request);
        ObjectId propertyId = ParamsManager.getPropertyId(request);
        return getRepository().getDocumentPropertyByEntityIdAndId(propertyId, userId);
    }
    
    /**
     * Method for update profile user property by request body
     * @param request Spark request object
     * @param profileRepository profile datasource object
     * @return updated property document
     * @throws DataException throw if can't be found user or property document
     */
    protected EmbeddedProperty updateProperty(Request request) 
            throws DataException {
        ObjectId userId = ParamsManager.getUserId(request);
        ObjectId propertyId = ParamsManager.getPropertyId(request);
        PropertyDTO propertyDTO = PropertyDTO.build(
                request, 
                PropertyDTO.class
        );
        return getRepository().updateProperty(
                propertyId, 
                userId, 
                propertyDTO
        );

    }
    
    /**
     * Method for delete profile user property from list by request params data
     * @param request Spark request object
     * @return actual profile value
     * @throws DataException throw if can't be found user or property document
     */
    protected List<EmbeddedProperty> deleteProperty(
    		Request request
    ) throws DataException {
        ObjectId userId = ParamsManager.getUserId(request);
        ObjectId propertyId = ParamsManager.getPropertyId(request);
        return getRepository().removeProperty(propertyId, userId);
    }
}
