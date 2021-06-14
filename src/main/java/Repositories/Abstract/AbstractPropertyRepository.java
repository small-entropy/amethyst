package Repositories.Abstract;

import DataTransferObjects.UserPropertyDTO;
import Exceptions.DataException;
import Models.Standalones.User;
import Models.Embeddeds.EmbeddedProperty;
import Utils.common.Searcher;
import dev.morphia.Datastore;
import java.util.Iterator;
import java.util.List;

/**
 * Abstract class for source works with user properties fields
 * @author small-entropy
 */
public class AbstractPropertyRepository extends AbstractChildUserRepository {
    // Property with name of field
    private final String field;
    // Black list for user proeprties
    private final List<String> blackList;
            
    /**
     * Constructor for source instanse
     * @param datastore Morphia datastore object
     * @param field name of field
     * @param blaclList blacl list
     */
    public AbstractPropertyRepository(
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
     * Method for craete user proeprty document
     * @param idParam user id from request params
     * @param userPropertyDTO user property data transfer object
     * @return user property document
     * @throws DataException throw exception if can not find user document
     */
    public EmbeddedProperty createUserProperty(
            String idParam,
            UserPropertyDTO userPropertyDTO
    ) throws DataException {
        User user = getUserDocument(idParam);
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
     * Method for check on exist propertie in properties list
     * @param key name of property
     * @param user user document
     * @return ckeck result
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
     * @param propertyIdParam property id from request params
     * @param idParam user id from request params
     * @return user property document
     * @throws DataException throw if can not find list of user properties
     */
    public EmbeddedProperty getUserPropertyById(
            String propertyIdParam, 
            String idParam
    ) throws DataException {
        List<EmbeddedProperty> properties = getList(idParam);
        return getUserPropertyById(propertyIdParam, properties);
    }

    /**
     * Method for get user property by id from requst & user document
     * @param propertyIdParam property id from request
     * @param user user document
     * @return user property document
     * @throws DataException throw if not found usr or property
     */
    private EmbeddedProperty getUserPropertyById(
            String propertyIdParam, 
            User user
    ) throws DataException {
        List<EmbeddedProperty> properties = getPropertiesByUser(user);
        return getUserPropertyById(propertyIdParam, properties);
    }
    
    /**
     * Method for get property by id from reqyst in properties list
     * @param propertyIdParam property id from request
     * @param properties list of user properties
     * @return user property document
     * @throws DataException throw if not found usr or property
     */
    private EmbeddedProperty getUserPropertyById(
            String propertyIdParam, 
            List<EmbeddedProperty> properties
    ) throws DataException {
        EmbeddedProperty result = Searcher
                .getUserPropertyByIdFromList(propertyIdParam, properties);
        if (result != null) {
            return result;
        } else {
            Error error = new Error("Can not find user property");
            throw new DataException("NotFound", error);
        }
    }
    
    /**
     * Method for get user properties list by user id from request params
     * @param idParam user id from request params
     * @return list of user properties
     * @throws DataException throw if can not find list of user properties
     */
    public List<EmbeddedProperty> getList(String idParam) throws DataException {
        User user = getUserDocument(idParam);
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
     * @param propertyIdParam property id from request params
     * @param idParam user id from request params
     * @return list of user properties
     * @throws DataException throw if can not find list of user properties
     */
    public List<EmbeddedProperty> removeProperty(
            String propertyIdParam,
            String idParam
    ) throws DataException {
        User user = getUserDocument(idParam);
        List<EmbeddedProperty> properties = getPropertiesByUser(user);
        Iterator iterator = properties.iterator();
        while(iterator.hasNext()) {
            EmbeddedProperty property = (EmbeddedProperty) iterator.next();
            if (property.getId().equals(propertyIdParam) &&
                    !getBlackList().contains(property.getKey())) {
                iterator.remove();
            }
        }
        save(user);
        return getPropertiesByUser(user);
    }
    
    /**
     * Method for update user property document
     * @param propertyIdParam property id param fom requiers
     * @param idParam user id param from request
     * @param userPropertyDTO user property data transfer object
     * @return updated user property document
     * @throws DataException throw if not found user or property
     */
    public EmbeddedProperty updateUserProperty(
            String propertyIdParam,
            String idParam,
            UserPropertyDTO userPropertyDTO
    ) throws DataException {
        User user = getUserDocument(idParam);
        EmbeddedProperty property = getUserPropertyById(propertyIdParam, user);
        property.setValue(userPropertyDTO.getValue());
        save(user);
        return property;
    }
}
