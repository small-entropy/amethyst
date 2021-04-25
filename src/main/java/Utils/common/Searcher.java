package Utils.common;

import Models.UserProperty;

import java.util.List;

/**
 * Class for utils methods for search some in lists or other collections
 */
public class Searcher {

    /**
     * Method for get user property in properties list by property ID
     * @param propertyId property ID as string
     * @param properties list of user properties
     * @return user property
     */
    public static UserProperty getUserPropertyByIdFromList(String propertyId, List<UserProperty> properties) {
        UserProperty result = null;
        if (properties != null && propertyId != null) {
            for (UserProperty property : properties) {
                if (property.getId().toString().equals(propertyId)) {
                    result = property;
                    break;
                }
            }
        }
        return result;
    }
}
