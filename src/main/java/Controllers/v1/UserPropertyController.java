package Controllers.v1;

import Controllers.base.BaseUserPropertyController;
import DataTransferObjects.RuleDTO;
import Exceptions.DataException;
import Models.Embeddeds.EmbeddedProperty;
import Responses.SuccessResponse;
import Services.v1.UserPropertyService;
import Repositories.v1.PropertiesRepository;
import Repositories.v1.UsersRepository;
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
     * @param datastore Morphia datastore
     * @param transformer JSON response transformer
     */
    public static void routes(Datastore datastore, JsonTransformer transformer) {
        PropertiesRepository propertiesRepository = new PropertiesRepository(
                datastore,
                BLACK_LIST
        );
        
        UsersRepository usersRepository = new UsersRepository(datastore);
        
        // Routes for work with user properties
        // Get user properties list by user UUID
        get("/:user_id/properties", (req, res) -> {
            RuleDTO rule = getRule(req, usersRepository, RULE, READ);
            List<EmbeddedProperty> properties = UserPropertyService
                    .getUserProperties(req, propertiesRepository, rule);
            return new SuccessResponse<>(MSG_LIST, properties);
        }, transformer);
        
        // Create new user property (user find by UUID)
        // This method only for create public property!
        // For create not public property use other method!
        post("/:user_id/properties", (req, res) -> {
            RuleDTO rule = getRule(req, usersRepository, RULE, CREATE);
            EmbeddedProperty userProperty = UserPropertyService
                    .createUserProperty(req, propertiesRepository, rule);
            return new SuccessResponse<>(MSG_CREATED, userProperty);
        }, transformer);
        
        // Get user property by UUID (user find by UUID)
        get("/:user_id/properties/:property_id", (req, res) -> {
            RuleDTO rule = getRule(req, usersRepository, RULE, READ);
            EmbeddedProperty property = UserPropertyService
                    .getUserPropertyById(req, propertiesRepository, rule);
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
            RuleDTO rule = getRule(req, usersRepository, RULE, UPDATE);
            EmbeddedProperty property = UserPropertyService
                    .updateProperty(req, propertiesRepository, rule);
            return new SuccessResponse<>(MSG_UPDATED, property);
        }, transformer);
        
        // Remove user property by UUID (user find by UUID)
        delete("/:user_id/properties/:property_id", (req, res) -> {
            RuleDTO rule = getRule(req, usersRepository, RULE, DELETE);
            List<EmbeddedProperty> properties = UserPropertyService
                    .deleteUserProperty(req, propertiesRepository, rule);
            return new SuccessResponse<>(MSG_DELETED, properties);
        }, transformer);
    }
}
