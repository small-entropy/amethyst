package synthwave.controllers.v1;

import synthwave.controllers.base.BaseUserPropertyController;
import platform.exceptions.DataException;
import synthwave.models.mongodb.embeddeds.EmbeddedProperty;
import platform.utils.responses.SuccessResponse;
import synthwave.services.v1.UserPropertyService;
import platform.utils.transformers.JsonTransformer;
import dev.morphia.Datastore;
import java.util.List;
import static spark.Spark.*;

/**
 * Static class with routes for work with user property documents
 */
public class UserPropertyController extends BaseUserPropertyController {
    
    /**
     * Method with init routes for work with user property documents
     * @param datastore Morphia datastore
     * @param transformer JSON response transformer
     */
    public static void routes(Datastore datastore, JsonTransformer transformer) {
        UserPropertyService service = new UserPropertyService(datastore);
        // Routes for work with user properties
        // Get user properties list by user UUID
        get("/:user_id/properties", (req, res) -> {
            List<EmbeddedProperty> properties = service.getUserProperties(
                    req,
                    RIGHT, 
                    READ
            );
            return new SuccessResponse<>(MSG_LIST, properties);
        }, transformer);
        
        // Create new user property (user find by UUID)
        // This method only for create public property!
        // For create not public property use other method!
        post("/:user_id/properties", (req, res) -> {
            EmbeddedProperty userProperty = service.createUserProperty(
                    req, 
                    RIGHT, 
                    CREATE
            );
            return new SuccessResponse<>(MSG_CREATED, userProperty);
        }, transformer);
        
        // Get user property by UUID (user find by UUID)
        get("/:user_id/properties/:property_id", (req, res) -> {
            EmbeddedProperty property = service.getUserPropertyById(
                    req, 
                    RIGHT, 
                    READ
            );
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
            EmbeddedProperty property = service.updateProperty(
                            req, 
                            RIGHT,
                            UPDATE
                    );
            return new SuccessResponse<>(MSG_UPDATED, property);
        }, transformer);
        
        // Remove user property by UUID (user find by UUID)
        delete("/:user_id/properties/:property_id", (req, res) -> {
            List<EmbeddedProperty> properties = service.deleteUserProperty(
                    req, 
                    RIGHT,
                    DELETE
            );
            return new SuccessResponse<>(MSG_DELETED, properties);
        }, transformer);
    }
}
