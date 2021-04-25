package Services.core;

import DTO.UserPropertyDTO;
import Exceptions.DataException;
import Models.User;
import Models.UserProperty;
import Utils.common.Comparator;
import dev.morphia.Datastore;
import org.bson.types.ObjectId;
import spark.Request;

import java.util.Arrays;
import java.util.List;


public abstract class CoreUserProfileService extends CorePropertyService {

    /**
     * Method for get default profile properties list
     * @return list of user properties for profile
     */
    protected static List<UserProperty> getDefaultProfile() {
        Long currentDateTime = System.currentTimeMillis();
        UserProperty registered = new UserProperty("registered", currentDateTime);
        return Arrays.asList(registered);
    }


    protected static UserProperty createUserProfilePropertyByRequest(Request request, Datastore datastore) throws DataException {
        return CoreUserProfileService.createUserProperty("profile", request, datastore);
    }

    /**
     * Method fot get user profile nested document by request params
     * @param idParam user id as string
     * @param datastore Morphia datastore object
     * @return list of user profile properties
     */
    protected static List<UserProperty> getUserProfile(String idParam, Datastore datastore) throws DataException {
        return CoreUserProfileService.getPropertiesList("profile", idParam, datastore);
    }

    /**
     * Method for get user profile property by request params (id, property_id)
     * @param idParam user ud as string
     * @param propertyIdParam user profile property id as string
     * @param datastore Morphia datastore object
     * @return user profile property
     */
    protected static UserProperty getUserProfilePropertyById(String idParam, String propertyIdParam, Datastore datastore) throws DataException {
        return CoreUserPropertyService.getPropertyById("profile", propertyIdParam, idParam, datastore);
    }

    /**
     * Method for update user profile property in user document
     * @param request Spark request object
     * @param datastore Morphia datastore object
     * @param userPropertyDTO user property data transfer object
     * @return updated user property
     * @throws DataException return exception if not founded user or user property in profile list
     */
    protected static UserProperty updateUserProperty(Request request, Datastore datastore, UserPropertyDTO userPropertyDTO) throws DataException {
        return CoreUserProfileService.updateUserProperty(request, datastore, userPropertyDTO, "profile");
    }
}
