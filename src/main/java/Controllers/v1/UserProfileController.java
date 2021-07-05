package Controllers.v1;

import Controllers.base.BaseUserProfileController;
import Exceptions.DataException;
import Models.Embeddeds.EmbeddedProperty;
import Utils.responses.SuccessResponse;
import Services.v1.UserProfileService;
import Utils.transformers.JsonTransformer;
import dev.morphia.Datastore;
import java.util.List;
import static spark.Spark.*;

/**
 * Class with routes for work with profile documents
 */
public class UserProfileController extends BaseUserProfileController {
    
    /**
     * Method with init routes for work with profile property documents
     * @param datastore Morphia datastore
     * @param transformer JSON response transformer
     */
    public static void routes(Datastore datastore, JsonTransformer transformer) {
        
        UserProfileService service = new UserProfileService(datastore);
        
        // Route for get user profile
        get("/:user_id/profile", (req, res)-> {
            List<EmbeddedProperty> profile = service.getUserProfile(req);
            return new SuccessResponse<>(MSG_LIST, profile);
        }, transformer);
        
        // Route for create profile property
        post("/:user_id/profile", (req, res) -> {
            EmbeddedProperty property = service.createUserProfileProperty(req);
            return new SuccessResponse<>(MSG_CREATED, property);
        }, transformer);
        
        // Route for get user profile property by ID
        get("/:user_id/profile/:property_id", (req, res) -> {
            EmbeddedProperty property = service.getUserProfilePropertyById(
                    req
            );
            if (property != null) {
                return new SuccessResponse<>(MSG_ENTITY, property);
            } else {
                Error error = new Error("Can found property with this id");
                throw new DataException("NotFound", error);
            }
        }, transformer);
        
        // Update user profile property by property UUID (user find by UUID)
        put("/:user_id/profile/:property_id", (req, res) -> {
            // Try update user profile property
            EmbeddedProperty property = service.updateUserProperty(
                    req, 
                    RIGHT, 
                    UPDATE
            );
            // Return successfully response
            return new SuccessResponse<>(MSG_UPDATED, property);
        }, transformer);
        
        // Remove user profile property by UUID (user find by UUID)
        delete("/:user_id/profile/:property_id", (req, res) -> {
            List<EmbeddedProperty> profile = service.deleteProfileProperty(
                    req, 
                    RIGHT, 
                    DELETE
            );
            return new SuccessResponse<>(MSG_DELETED, profile);
        }, transformer);
    }
}
