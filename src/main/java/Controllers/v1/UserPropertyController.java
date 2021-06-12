package Controllers.v1;

import Controllers.base.BaseUserPropertyController;
import DataTransferObjects.RuleDTO;
import Exceptions.DataException;
import Models.Embeddeds.EmbeddedProperty;
import Responses.SuccessResponse;
import Services.v1.UserPropertyService;
import Sources.PropertiesSource;
import Transformers.JsonTransformer;
import dev.morphia.Datastore;
import java.util.Arrays;
import java.util.List;
import static spark.Spark.*;

/**
 * Static class with routes for work with user property documents
 */
public class UserPropertyController extends BaseUserPropertyController {
    
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
            RuleDTO rule = getRule(req, source, RULE, READ);
            List<EmbeddedProperty> properties = UserPropertyService
                    .getUserProperties(req, source, rule);
            return new SuccessResponse<>(MSG_LIST, properties);
        }, transformer);
        
        // Create new user property (user find by UUID)
        // This method only for create public property!
        // For create not public property use other method!
        post("/:user_id/properties", (req, res) -> {
            RuleDTO rule = getRule(req, source, RULE, CREATE);
            EmbeddedProperty userProperty = UserPropertyService
                    .createUserProperty(req, source, rule);
            return new SuccessResponse<>(MSG_CREATED, userProperty);
        }, transformer);
        
        // Get user property by UUID (user find by UUID)
        get("/:user_id/properties/:property_id", (req, res) -> {
            RuleDTO rule = getRule(req, source, RULE, READ);
            EmbeddedProperty property = UserPropertyService
                    .getUserPropertyById(req, source, rule);
            if (property != null) {
                return new SuccessResponse<>(MSG_ENTITY, property);
            } else {
                Error error = new Error("Can not find user property");
                throw new DataException("NotFound", error);
            }
        }, transformer);
        
        // Update user property by property UUID (user find by UUID)
        put("/:user_id/properties/:property_id", (req, res) -> {
            // Get user rule for update users documents
            RuleDTO rule = getRule(req, source, RULE, UPDATE);
            EmbeddedProperty property = UserPropertyService
                    .updateProperty(req, source, rule);
            return new SuccessResponse<>(MSG_UPDATED, property);
        }, transformer);
        
        // Remove user property by UUID (user find by UUID)
        delete("/:user_id/properties/:property_id", (req, res) -> {
            RuleDTO rule = getRule(req, source, RULE, DELETE);
            List<EmbeddedProperty> properties = UserPropertyService
                    .deleteUserProperty(req, source, rule);
            return new SuccessResponse<>(MSG_DELETED, properties);
        }, transformer);
    }
}
