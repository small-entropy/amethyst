package Utils.common;

import Exceptions.DataException;
import Models.Embeddeds.EmbeddedProperty;
import Models.Embeddeds.EmbeddedRight;

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
    public static EmbeddedProperty getUserPropertyByIdFromList(
            String propertyId, 
            List<EmbeddedProperty> properties
    ) {
        EmbeddedProperty result = null;
        if (properties != null && propertyId != null) {
            for (EmbeddedProperty property : properties) {
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
    public static EmbeddedRight getUserRightByIdFromList(
            String rightIdParam, 
            List<EmbeddedRight> rights
    ) throws DataException {
        EmbeddedRight result = null;
        for (EmbeddedRight right : rights) {
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
