package Services.base;

import DataTransferObjects.UserPropertyDTO;
import Exceptions.DataException;
import Models.User;
import Models.UserProperty;
import Services.core.CoreUserService;
import Sources.UsersSource;
import Utils.common.Searcher;
import Utils.constants.UsersParams;
import com.google.gson.Gson;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import spark.Request;

/**
 * Abstract core class for work with user properties nested documents
 * @author entropy
 */
public abstract class BasePropertyService { 
    
    private static final List<String> PROFILE_BLACK_LIST = Arrays.asList("registered");
    private static final List<String> PROPERTY_BLACK_LIST = Arrays.asList("banned");

    /**
     * Method for get properties list from  user document by field
     * @param field name of fueld
     * @param user user document
     * @return properties list
     */
    private static List<UserProperty> getPropertiesByFieldName(String field, User user) {
        return switch (field) {
            case "profile" -> user.getProfile();
            case "properties" -> user.getProperties();
            default -> null;
        };
    }

    /**
     * Method for get properties list from user document by request id param
     * @param field name of field
     * @param idParam user id as string
     * @param source source for work with users collection
     * @return user properties list
     * @throws DataException throw exception if user can not be found by request params
     */
    protected static List<UserProperty> getPropertiesList(String field,
                                                          String idParam,
                                                          UsersSource source) throws DataException {
        User user = CoreUserService.getUserById(idParam, source);
        if (user != null) {
            return getPropertiesByFieldName(field, user);
        } else {
            Error error = new Error("Can not find user by request params");
            throw new DataException("NotFound", error);
        }
    }
    
    protected static List<UserProperty> removeFromList(String field, Request request, UsersSource source) throws DataException {
        String idParam = request.params(UsersParams.ID.getName());
        String propertyIdParam = request.params(UsersParams.PROPERTY_ID.getName());
        User user = CoreUserService.getUserById(idParam, source);
        if (user != null) {
            List<UserProperty> profile = getPropertiesByFieldName(field, user);
            Iterator iterator = profile.iterator();
            while (iterator.hasNext()) {
                UserProperty property = (UserProperty)iterator.next();
                if (property.getId().toString().equals(propertyIdParam) && 
                        field.equals("profile") && !PROFILE_BLACK_LIST.contains(property.getKey()) || 
                        field.equals("properties") && !PROPERTY_BLACK_LIST.contains(property.getKey())) {
                    iterator.remove();
                }
            }
            source.save(user);
            return user.getProfile();
        } else {
            Error error = new Error("Can not find user by requeset params");
            throw new DataException("NotFound", error);
        }
    }

    /**
     * Method for get user property by id from properties list
     * @param propertyIdParam property id as string
     * @param properties properties list
     * @return founded property
     */
    protected static UserProperty getPropertyById(String propertyIdParam, List<UserProperty> properties) {
        return Searcher.getUserPropertyByIdFromList(propertyIdParam, properties);
    }

    /**
     * Method for get user property from document field by request params
     * @param field name of field
     * @param propertyIdParam property id
     * @param idParam user id
     * @param source source for work with users collection
     * @return founded user property
     * @throws DataException throw exception of some data not founded
     */
    protected static UserProperty getPropertyById(String field,
                                                  String propertyIdParam,
                                                  String idParam,
                                                  UsersSource source) throws DataException {
        List<UserProperty> properties = getPropertiesList(field, idParam, source);
        return getPropertyById(propertyIdParam, properties);
    }

    /**
     * Method for create user property on document field by request body dta
     * @param field name of field in document
     * @param request Spark request field
     * @param source source for work with users collection
     * @return created user property
     * @throws DataException throw exception if some data can not be founded
     */
    protected static UserProperty createUserProperty(String field,
                                                     Request request,
                                                     UsersSource source) throws DataException {
        User user = CoreUserService.getUserById(request.params(UsersParams.ID.getName()), source);
        if (user != null) {
            UserPropertyDTO userPropertyDTO = new Gson().fromJson(request.body(), UserPropertyDTO.class);
            final boolean hasProperty = keyProperty_fromUser(field, userPropertyDTO.getKey(), user);
            if (!hasProperty) {
                UserProperty userProperty = new UserProperty(
                        userPropertyDTO.getKey(),
                        userPropertyDTO.getValue()
                );
                List<UserProperty> properties = getPropertiesByFieldName(field, user);
                properties.add(userProperty);
                source.save(user);
                return userProperty;
            } else {
                return null;
            }
        } else {
            Error error = new Error("Can not find user by requst params");
            throw new DataException("NotFound", error);
        }
    }

    /**
     * Method for compare key and keys from properties
     * @param field name of flied
     * @param key name of new property
     * @param user user document
     * @return result of compare key and properties keys
     */
    protected static boolean keyProperty_fromUser(String field, String key, User user) {
        boolean hasProperty = false;
        List<UserProperty> properties = getPropertiesByFieldName(field, user);
        for (UserProperty property : properties) {
            if (property.getKey().equals(key)) {
                hasProperty = true;
                break;
            }
        }
        return hasProperty;
    }

    /**
     * Method for update user property by request data
     * @param request Spark request object
     * @param source source for work with users collection
     * @param userPropertyDTO user property data transfer object
     * @param field name of field
     * @return updated user property
     * @throws DataException throw exception if come data can not be founded
     */
    protected static UserProperty updateUserProperty(Request request,
                                                     UsersSource source,
                                                     UserPropertyDTO userPropertyDTO,
                                                     String field) throws DataException {
        String idParam = request.params(UsersParams.ID.getName());
        String propertyIdParam = request.params(UsersParams.PROPERTY_ID.getName());
        List<UserProperty> properties = null;
        User user = CoreUserService.getUserById(idParam, source);
        if (user != null) {
            properties = getPropertiesByFieldName(field, user);
            UserProperty property = getPropertyById(propertyIdParam, properties);
            if (property != null) {
                property.setValue(userPropertyDTO.getValue());
                source.save(user);
                return property;
            } else {
                Error error = new Error("Can not find user property by request params");
                throw new DataException("NotFound", error);
            }
        } else {
            Error error = new Error("Can not find user by request params");
            throw new DataException("NotFound", error);
        }
    }
}
