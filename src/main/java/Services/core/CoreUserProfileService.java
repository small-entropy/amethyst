package Services.core;

import DTO.UserPropertyDTO;
import Exceptions.DataException;
import Models.User;
import Models.UserProperty;
import Utils.common.Comparator;
import Utils.constants.UsersParams;
import dev.morphia.Datastore;
import org.bson.types.ObjectId;
import spark.Request;

import java.util.Arrays;
import java.util.List;


public abstract class CoreUserProfileService {

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
     * Method for create profile property by data
     * @param id user id
     * @param userPropertyDTO user property data transfer object
     * @param datastore Morphia datastore object
     * @return profile property
     * @throws DataException exception with empty data (if user not find)
     */
    protected static UserProperty createUserProfileProperty(ObjectId id, UserPropertyDTO userPropertyDTO, Datastore datastore) throws DataException {
        // Get user ID param from request URL
        User user = CoreUserService.getUserById(id, datastore);
        // Check founded user.
        // If user not equal null - try create user property and add to document.
        // If user equal null - throw error.
        if (user != null) {
            final boolean hasProperty = Comparator.keyProperty_fromUser(userPropertyDTO.getKey(), user);
            // Check result compare key property and key from user properties.
            // If user document has property with this key - throw error.
            // If user document has not property with this - create it.
            if (!hasProperty) {
                // Create user property
                UserProperty userProperty = new UserProperty(
                        userPropertyDTO.getKey(),
                        userPropertyDTO.getValue()
                );
                user.getProfile().add(userProperty);
                datastore.save(user);
                return userProperty;
            } else {
                return null;
            }
        } else {
            Error error = new Error("User not found. Send not valid id");
            throw new DataException("NotFound", error);
        }
    }

    /**
     * Method fot get user profile nested document by request params
     * @param idParam user id as string
     * @param datastore Morphia datastore object
     * @return list of user profile properties
     */
    protected static List<UserProperty> getUserProfile(String idParam, Datastore datastore) throws DataException {
        User user = CoreUserService.getUserById(idParam, datastore);
        if (user != null) {
            return user.getProfile();
        } else {
            Error error = new Error("Can not find user by request params");
            throw new DataException("NotFound", error);
        }
    }

    /**
     * Method for get user profile property by request params (id, property_id)
     * @param idParam user ud as string
     * @param propertyIdParam user profile property id as string
     * @param datastore Morphia datastore object
     * @return user profile property
     */
    protected static UserProperty getUserProfilePropertyById(String idParam, String propertyIdParam, Datastore datastore) throws DataException {
        List<UserProperty> profile = CoreUserProfileService.getUserProfile(idParam, datastore);
        return getPropertyFromProfileById(propertyIdParam, profile);
    }

    /**
     * Private method for find user profile property in list of user profile properties
     * @param propertyIdParam property id as string
     * @param profile profile properties list
     * @return founded user property
     */
    private static UserProperty getPropertyFromProfileById(String propertyIdParam, List<UserProperty> profile) {
        UserProperty result = null;
        if (profile != null && propertyIdParam != null) {
            for (UserProperty profile_property : profile) {
                if (profile_property.getId().toString().equals(propertyIdParam)) {
                    result = profile_property;
                }
            }
        }
        return result;
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
        // Get user ID from request params
        String idParam = request.params(UsersParams.ID.getName());
        // Property ID for profile properties from request params
        String propertyIdParam = request.params(UsersParams.PROPERTY_ID.getName());
        // Initialize empty profile variable
        List<UserProperty> profile = null;
        // Get user by ID
        User user = CoreUserService.getUserById(idParam, datastore);
        // Check user on exist.
        // If user exist - get profile property from document.
        // If user not exist - throw error,
        if (user != null) {
            profile = user.getProfile();
        } else {
            Error error = new Error("Can not find user by request params");
            throw new DataException("NotFound", error);
        }
        // Get profile property by ID from request
        UserProperty property = getPropertyFromProfileById(propertyIdParam, profile);
        // Check on exist user profile property.
        // If property exist - set new value & save changes, then return updated value
        // If property not exist - throw error.
        if (property != null) {
            // Set new value for profile property
            property.setValue(userPropertyDTO.getValue());
            // Save change in database.
            // Attention! For update nested documents in Morphia you must update all document, not
            // user update for this.
            datastore.save(user);
            return property;
        } else {
            Error error = new Error("Can not find user property by request params");
            throw new DataException("NotFound", error);
        }
    }
}
