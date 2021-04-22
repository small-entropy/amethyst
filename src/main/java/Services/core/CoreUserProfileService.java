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
            throw new DataException("NotFoun", error);
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
}
