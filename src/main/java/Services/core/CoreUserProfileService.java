package Services.core;

import DataTransferObjects.UserPropertyDTO;
import Exceptions.DataException;
import Models.UserProperty;
import Services.base.BasePropertyService;
import Sources.UsersSource;
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
    protected static List<UserProperty> getDefaultProfile() {
        Long currentDateTime = System.currentTimeMillis();
        UserProperty registered = new UserProperty("registered", currentDateTime);
        return Arrays.asList(registered);
    }


    /**
     * Mthod for create user profile property by requst daa
     * @param request Spark request object
     * @param source source for work with users collection
     * @return created user property document
     * @throws DataException 
     */
    protected static UserProperty createUserProfilePropertyByRequest(Request request, UsersSource source) throws DataException {
        return CoreUserProfileService.createUserProperty("profile", request, source);
    }

    /**
     * Method fot get user profile nested document by request params
     * @param idParam user id as string
     * @param source source for work with users collection
     * @return list of user profile properties
     * @throws DataException
     */
    protected static List<UserProperty> getUserProfile(String idParam, UsersSource source) throws DataException {
        return CoreUserProfileService.getPropertiesList("profile", idParam, source);
    }

    /**
     * Method for get user profile property by request params (id, property_id)
     * @param idParam user ud as string
     * @param propertyIdParam user profile property id as string
     * @param source source for work with users collection
     * @return user profile property
     * @throws DataException
     */
    protected static UserProperty getUserProfilePropertyById(String idParam, String propertyIdParam, UsersSource source) throws DataException {
        return CoreUserPropertyService.getPropertyById("profile", propertyIdParam, idParam, source);
    }

    /**
     * Method for update user profile property in user document
     * @param request Spark request object
     * @param source source for work with users collection
     * @param userPropertyDTO user property data transfer object
     * @return updated user property
     * @throws DataException return exception if not founded user or user property in profile list
     */
    protected static UserProperty updateUserProperty(Request request, UsersSource source, UserPropertyDTO userPropertyDTO) throws DataException {
        return CoreUserProfileService.updateUserProperty(request, source, userPropertyDTO, "profile");
    }
}
