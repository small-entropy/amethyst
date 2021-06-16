package Utils.common;

import Exceptions.DataException;
import Models.Embeddeds.EmbeddedProperty;
import Models.Embeddeds.EmbeddedRight;

import java.util.List;
import org.bson.types.ObjectId;

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
            ObjectId propertyId, 
            List<EmbeddedProperty> properties
    ) {
        EmbeddedProperty result = null;
        if (properties != null && propertyId != null) {
            String toCheck = propertyId.toString();
            for (EmbeddedProperty property : properties) {
                if (property.getId().equals(toCheck)) {
                    result = property;
                    break;
                }
            }
        }
        return result;
    }
    
    /**
     * Method for get user right for rights list by id
     * @param rightId right id as string
     * @param rights rights list
     * @return founded right
     * @throws DataException throw if right not found
     */
    public static EmbeddedRight getUserRightByIdFromList(
            ObjectId rightId, 
            List<EmbeddedRight> rights
    ) throws DataException {
        EmbeddedRight result = null;
        String toCheck = rightId.toString();
        for (EmbeddedRight right : rights) {
            if (right.getId().equals(toCheck)) {
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
