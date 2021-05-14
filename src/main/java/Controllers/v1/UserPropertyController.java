package Controllers.v1;

import DataTransferObjects.RuleDTO;
import Exceptions.DataException;
import Models.UserProperty;
import Responses.SuccessResponse;
import Services.v1.UserPropertyService;
import Sources.PropertiesSource;
import Transformers.JsonTransformer;
import Utils.constants.DefaultActions;
import Utils.constants.DefaultRights;
import Utils.constants.ResponseMessages;
import Utils.v1.RightManager;
import dev.morphia.Datastore;
import java.util.Arrays;
import java.util.List;
import static spark.Spark.*;

/**
 * Static class with routes for work with user property documents
 */
public class UserPropertyController {
    
    private static final List<String> BLACK_LIST = Arrays.asList("banned");

    /**
     * Method with init routes for work with user property documents
     * @param store Morphia datastore
     * @param transformer JSON response transformer
     */
    public static void routes(Datastore store, JsonTransformer transformer) {
        PropertiesSource source = new PropertiesSource(store, BLACK_LIST);
        
        // Routes for work with user properties
        // Get user properties list by user UUID
        get("/:user_id/properties", (req, res) -> {
            RuleDTO rule = RightManager.getRuleByRequest_Token(req, source, DefaultRights.USERS.getName(), DefaultActions.READ.getName());
            List<UserProperty> properties = UserPropertyService.getUserProperties(req, source, rule);
            return new SuccessResponse<>(ResponseMessages.USER_PROPERTIES.getMessage(), properties);
        }, transformer);
        
        // Create new user property (user find by UUID)
        // This method only for create public property!
        // For create not public property use other method!
        post("/:user_id/properties", (req, res) -> {
            RuleDTO rule = RightManager.getRuleByRequest_Token(req, source, DefaultRights.USERS.getName(), DefaultActions.CREATE.getName());
            UserProperty userProperty = UserPropertyService.createUserProperty(req, source, rule);
            return new SuccessResponse<>(ResponseMessages.USER_PROPERTY_CREATED.getMessage(), userProperty);
        }, transformer);
        
        // Get user property by UUID (user find by UUID)
        get("/:user_id/properties/:property_id", (req, res) -> {
            RuleDTO rule = RightManager.getRuleByRequest_Token(req, source, DefaultRights.USERS.getName(), DefaultActions.READ.getName());
            UserProperty property = UserPropertyService.getUserPropertyById(req, source, rule);
            if (property != null) {
                return new SuccessResponse<>(ResponseMessages.USER_PROPERTY.getMessage(), property);
            } else {
                Error error = new Error("Can not find user property");
                throw new DataException("NotFound", error);
            }
        }, transformer);
        
        // Update user property by property UUID (user find by UUID)
        put("/:user_id/properties/:property_id", (req, res) -> {
            // Get user rule for update users documents
            RuleDTO rule = RightManager.getRuleByRequest_Token(req, source, DefaultRights.USERS.getName(), DefaultActions.UPDATE.getName());
            UserProperty property = UserPropertyService.updateProperty(req, source, rule);
            return new SuccessResponse<>(ResponseMessages.USER_PROPERTY_UPDATED.getMessage(), property);
        }, transformer);
        
        // Remove user property by UUID (user find by UUID)
        delete("/:user_id/properties/:property_id", (req, res) -> {
            RuleDTO rule = RightManager.getRuleByRequest_Token(req, source, DefaultRights.USERS.getName(), DefaultActions.DELETE.getName());
            List<UserProperty> properties = UserPropertyService.deleteUserProperty(req, source, rule);
            return new SuccessResponse<>(ResponseMessages.USER_PROPETY_DELETED.getMessage(), properties);
        }, transformer);
    }
}
