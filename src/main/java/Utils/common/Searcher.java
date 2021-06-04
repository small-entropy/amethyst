package Utils.common;

import Exceptions.DataException;
import Models.Embeddeds.UserProperty;
import Models.Embeddeds.UserRight;

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
                if (property.getId().equals(propertyId)) {
                    result = property;
                    break;
                }
            }
        }
        return result;
    }
    
    /**
     * Method for get user right for rights list by id
     * @param rightIdParam right id as string
     * @param rights rights list
     * @return founded right
     * @throws DataException throw if right not found
     */
    public static UserRight getUserRightByIdFromList(String rightIdParam, List<UserRight> rights) throws DataException {
        UserRight result = null;
        for (UserRight right : rights) {
            if (right.getId().equals(rightIdParam)) {
                result = right;
                break;
            }
        }
        
        
        if (result != null) {
            return result;
        } else {
            Error error = new Error("Can not find user right by request params");
            throw new DataException("NotFound", error);
        }
    }
}
