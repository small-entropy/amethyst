package platform.repositories.mongodb.base;

import synthwave.dto.UserPropertyDTO;
import platform.exceptions.DataException;
import synthwave.models.mongodb.standalones.User;
import synthwave.models.mongodb.embeddeds.EmbeddedProperty;
import platform.utils.helpers.Searcher;
import dev.morphia.Datastore;
import java.util.Iterator;
import java.util.List;
import org.bson.types.ObjectId;

/**
 * Abstract class for source works with user properties fields
 * @author small-entropy
 */
public class BasePropertyRepository extends BaseChildUserRepository {
    /** Property with name of field */
    private final String field;
    /** Black list for user properties */
    private final List<String> blackList;
            
    /**
     * Constructor for source instance
     * @param datastore Morphia datastore object
     * @param field name of field
     * @param blaclList black list
     */
    public BasePropertyRepository(
            Datastore datastore,
            String field,
            List<String> blaclList
    ) {
        super(datastore);
        this.field = field;
        this.blackList = blaclList;
    }
    
    /**
     * Getter for get list field
     * @return black list properties
     */
    public List<String> getBlackList() {
        return blackList;
    }

    /**
     * Getter for field property
     * @return current value
     */
    public String getField() {
        return field;
    }
    
    /**
     * Method for create user property document
     * @param userId user id from request params
     * @param userPropertyDTO user property data transfer object
     * @return user property document
     * @throws DataException throw exception if can not find user document
     */
    public EmbeddedProperty createUserProperty(
            ObjectId userId,
            UserPropertyDTO userPropertyDTO
    ) throws DataException {
        User user = getUserDocument(userId);
        boolean hasProperty = hasProperty(userPropertyDTO.getKey(), user);
        if (!hasProperty) {
            EmbeddedProperty userProperty = new EmbeddedProperty(
                        userPropertyDTO.getKey(),
                        userPropertyDTO.getValue()
                );
            List<EmbeddedProperty> properties = getPropertiesByUser(user);
            properties.add(userProperty);
            save(user);
            return userProperty;
        } else {
            Error error = new Error("Can not create user property");
            throw new DataException("CanNotCreate", error);
        }
    }
    
    /**
     * Method for check on exist properties in properties list
     * @param key name of property
     * @param user user document
     * @return check result
     */
    private boolean hasProperty(String key, User user) {
        boolean hasProperty = false;
        List<EmbeddedProperty> properties = getPropertiesByUser(user);
        for (EmbeddedProperty property : properties) {
            if (property.getKey().equals(key)) {
                hasProperty = true;
                break;
            }
        }
        return hasProperty;
    }
    
    /**
     * MEthod for get user property by id from request params
     * @param propertyId property id from request params
     * @param userId user id from request params
     * @return user property document
     * @throws DataException throw if can not find list of user properties
     */
    public EmbeddedProperty getUserPropertyById(
            ObjectId propertyId, 
            ObjectId userId
    ) throws DataException {
        List<EmbeddedProperty> properties = getList(userId);
        return getUserPropertyById(propertyId, properties);
    }

    /**
     * Method for get user property by id from request & user document
     * @param propertyId property id from request
     * @param user user document
     * @return user property document
     * @throws DataException throw if not found user or property
     */
    private EmbeddedProperty getUserPropertyById(
            ObjectId propertyId, 
            User user
    ) throws DataException {
        List<EmbeddedProperty> properties = getPropertiesByUser(user);
        return getUserPropertyById(propertyId, properties);
    }
    
    /**
     * Method for get property by id from request in properties list
     * @param propertyId property id from request
     * @param properties list of user properties
     * @return user property document
     * @throws DataException throw if not found user or property
     */
    private EmbeddedProperty getUserPropertyById(
            ObjectId propertyId, 
            List<EmbeddedProperty> properties
    ) throws DataException {
        EmbeddedProperty result = Searcher
                .getUserPropertyByIdFromList(propertyId, properties);
        if (result != null) {
            return result;
        } else {
            Error error = new Error("Can not find user property");
            throw new DataException("NotFound", error);
        }
    }
    
    /**
     * Method for get user properties list by user id from request params
     * @param userId user id from request params
     * @return list of user properties
     * @throws DataException throw if can not find list of user properties
     */
    public List<EmbeddedProperty> getList(ObjectId userId) throws DataException {
        User user = getUserDocument(userId);
        List<EmbeddedProperty> properties = getPropertiesByUser(user);
        if (properties != null) {
            return properties;
        } else {
            Error error = new Error("Can not find user properties list");
            throw new DataException("NotFound", error);
        }
    }
    
    /**
     * Method for get user properties list from user document by field name
     * @param user user document
     * @return properties list
     */
    private List<EmbeddedProperty> getPropertiesByUser(User user) {
        return switch (field) {
            case "profile" -> user.getProfile();
            case "properties" -> user.getProperties();
            default -> null;
        };
    }
    
    /**
     * Method for remove user property from list of user properties
     * @param propertyId property id from request params
     * @param userId user id from request params
     * @return list of user properties
     * @throws DataException throw if can not find list of user properties
     */
    public List<EmbeddedProperty> removeProperty(
            ObjectId propertyId,
            ObjectId userId
    ) throws DataException {
        User user = getUserDocument(userId);
        List<EmbeddedProperty> properties = getPropertiesByUser(user);
        Iterator iterator = properties.iterator();
        String toCheck = propertyId.toString();
        while(iterator.hasNext()) {
            EmbeddedProperty property = (EmbeddedProperty) iterator.next();
            if (property.getId().equals(toCheck) &&
                    !getBlackList().contains(property.getKey())) {
                iterator.remove();
            }
        }
        save(user);
        return getPropertiesByUser(user);
    }
    
    /**
     * Method for update user property document
     * @param propertyId property id param from request
     * @param userId user id param from request
     * @param userPropertyDTO user property data transfer object
     * @return updated user property document
     * @throws DataException throw if not found user or property
     */
    public EmbeddedProperty updateUserProperty(
            ObjectId propertyId,
            ObjectId userId,
            UserPropertyDTO userPropertyDTO
    ) throws DataException {
        User user = getUserDocument(userId);
        EmbeddedProperty property = getUserPropertyById(propertyId, user);
        property.setValue(userPropertyDTO.getValue());
        save(user);
        return property;
    }
}
