package Services.core;

import DataTransferObjects.UserPropertyDTO;
import Exceptions.DataException;
import Models.Embeddeds.EmbeddedProperty;
import Services.base.BasePropertyService;
import Repositories.v1.ProfileRepository;
import Utils.constants.RequestParams;
import com.google.gson.Gson;
import java.util.Arrays;
import java.util.List;
import spark.Request;


/**
 * Base class for work with profile property list
 * @author entropy
 */
public abstract class CoreUserProfileService extends BasePropertyService {

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
     * @param profileRepository datasource for Profile
     * @return created user property
     * @throws DataException throw if con not be found user or property document
     */
    public static EmbeddedProperty createUserProperty(
            Request request, 
            ProfileRepository profileRepository
    ) throws DataException {
        String idParam = request.params(RequestParams.RIGHT_ID.getName());
        UserPropertyDTO userPropertyDTO = new Gson()
                .fromJson(request.body(), UserPropertyDTO.class);
        return createUserProperty(idParam, userPropertyDTO, profileRepository);
    }
    
    /**
     * Method for get user profile properties
     * @param request Spark request object
     * @param profileRepository source for work with users collection
     * @return list of user properties
     * @throws DataException throw if con not be found user or profile
     *                       field is empty
     */
    public static List<EmbeddedProperty> getUserProfile(
            Request request, 
            ProfileRepository profileRepository
    ) throws DataException {
        // Get user ID param from request URL
        String idParam = request.params(RequestParams.RIGHT_ID.getName());
        return getPropertiesList(idParam, profileRepository);
    }
    
    /**
     * Method for get user profile property by id
     * @param request Spark request object
     * @param profileRepository source for work with users collection
     * @return founded user property
     * @throws DataException throw if con not be found user or property document
     */
    public static EmbeddedProperty getUserProfilePropertyById(
            Request request, 
            ProfileRepository profileRepository
    ) throws DataException {
        String idParam = request.params(RequestParams.RIGHT_ID.getName());
        String propertyIdParam = request
                .params(RequestParams.PROPERTY_ID.getName());
        return getPropertyById(idParam, propertyIdParam, profileRepository);
    }
    
    /**
     * Method for update profile user property by request body
     * @param request Spark request object
     * @param profileRepository profile datasource object
     * @return updated property document
     * @throws DataException throw if con not be found user or property document
     */
    protected static EmbeddedProperty updateUserProperty(
            Request request, 
            ProfileRepository profileRepository
    ) throws DataException {
        String idParam = request.params(RequestParams.RIGHT_ID.getName());
        String propertyIdParam = request
                .params(RequestParams.PROPERTY_ID.getName());
        UserPropertyDTO userPropertyDTO = new Gson()
                .fromJson(request.body(), UserPropertyDTO.class);
        return updateUserProperty(
                propertyIdParam, 
                idParam, 
                userPropertyDTO, 
                profileRepository
        );
    }
    
    /**
     * Method for delete profile user property from list by request params data
     * @param request Spark requeset object
     * @param profileRepository profile datasource object
     * @return actual profule value
     * @throws DataException throw if con not be found user or property document
     */
    protected static List<EmbeddedProperty> deleteUserProfileProperty(
            Request request, 
            ProfileRepository profileRepository
    ) throws DataException {
        String idParam = request.params(RequestParams.RIGHT_ID.getName());
        String propertyIdParam = request
                .params(RequestParams.PROPERTY_ID.getName());
        return profileRepository.removeProperty(propertyIdParam, idParam);
    }
}
