/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sources;

import DataTransferObjects.UserPropertyDTO;
import Exceptions.DataException;
import Filters.UsersFilter;
import Models.User;
import Models.UserProperty;
import dev.morphia.Datastore;
import java.util.List;
import org.bson.types.ObjectId;

/**
 *
 * @author igrav
 */
public class AbstractPropertySource extends UsersSource {
    
    private String field;
    
    public AbstractPropertySource(Datastore datastore, String field) {
        super(datastore);
        this.field = field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }
    
    public UserProperty createUserProperty(String idParam, UserPropertyDTO userPropertyDTO) throws DataException {
        User user = getUserDocument(idParam);
        boolean hasProperty = hasProperty(userPropertyDTO.getKey(), user);
        if (!hasProperty) {
            UserProperty userProperty = new UserProperty(
                        userPropertyDTO.getKey(),
                        userPropertyDTO.getValue()
                );
            List<UserProperty> properties = getPropertiesByUser(user);
            properties.add(userProperty);
            save(user);
            return userProperty;
        } else {
            Error error = new Error("Can not find user by requst params");
            throw new DataException("NotFound", error);
        }
    }
    
    private boolean hasProperty(String key, User user) {
        boolean hasProperty = false;
        List<UserProperty> properties = getPropertiesByUser(user);
        for (UserProperty property : properties) {
            if (property.getKey().equals(key)) {
                hasProperty = true;
                break;
            }
        }
        return hasProperty;
    }
    
    public UserProperty UserPropertyById(String propertyIdParam, String idParam) throws DataException {
        List<UserProperty> properties = getList(idParam);
        UserProperty result = null;
        if (properties != null && propertyIdParam != null) {
            for(UserProperty property: properties) {
                if(property.getId().toString().equals(propertyIdParam)) {
                    result = property;
                    break;
                }
            }
        }
        return result;
    }
    
    private User getUserDocument(String idString) throws DataException {
        ObjectId id = new ObjectId(idString);
        String[] excludes = new String[]{};
        UsersFilter filter = new UsersFilter(id, excludes);
        User user = findOneById(filter);
        if (user != null) {
            return user;
        } else {
            Error error = new Error("Can not find user by id from request");
            throw new DataException("NotFound", error);
        }
    }
    
    /**
     *
     * @param idParam
     * @return
     * @throws DataException
     */
    public List<UserProperty> getList(String idParam) throws DataException {
        User user = getUserDocument(idParam);
        List<UserProperty> properties = getPropertiesByUser(user);
        if (properties != null) {
            return properties;
        } else {
            Error error = new Error("Can not find user properties list");
            throw new DataException("NotFound", error);
        }
    }
    
    private List<UserProperty> getPropertiesByUser(User user) {
        return switch (field) {
            case "profile" -> user.getProfile();
            case "properties" -> user.getProperties();
            default -> null;
        };
    }
}
