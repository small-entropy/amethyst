package synthwave.controllers.v1;

import synthwave.controllers.base.BaseUserRightsController;
import synthwave.models.mongodb.embeddeds.EmbeddedRight;
import platform.utils.responses.SuccessResponse;
import synthwave.services.v1.UserRightService;
import platform.utils.transformers.JsonTransformer;
import dev.morphia.Datastore;
import java.util.List;
import static spark.Spark.*;

/**
 * Static class for work with user right document
 * @author small-entropy
 */
public class UserRightsController extends BaseUserRightsController {
    
    /**
     * Method with init routes for work with user right documents
     * @param datastore Morphia datastore (connection) object
     * @param transformer JSON response transformer
     */
    public static void routes (Datastore datastore, JsonTransformer transformer) {
        
        UserRightService service = new UserRightService(datastore);
        
        // Routes for work with user rights
        // Get all rights by user UUID
        get("/:user_id/rights", (req, res) -> {
            List<EmbeddedRight> rights = service.getUserRights(req, RULE, READ);
            return new SuccessResponse<>(MSG_LIST, rights);
        }, transformer);
        
        // Create new user rights (user find by UUID)
        post("/:user_id/rights", (req, res) -> {
            EmbeddedRight right = service.createUserRight(req, RULE, CREATE);
            return new SuccessResponse<>(MSG_CREATED, right);
        }, transformer);
        
        // Get new user right by UUID (find user by UUID)
        get("/:user_id/rights/:right_id", (req, res) -> {
            EmbeddedRight right = service.getUserRightById(req, RULE, READ);
            return new SuccessResponse<>(MSG_ENTITY ,right);
        }, transformer);
        
        // Update user right by UUID (find user by UUID)
        put("/:user_id/rights/:right_id", (req, res) -> {
            EmbeddedRight right = service.updateRight(req, RULE, UPDATE);
            return new SuccessResponse<>(MSG_UPDATED, right);
        }, transformer);
        
        // Mark to remove user right (find user by UUID)
        delete("/:user_id/rights/:right_id", (req, res) -> {
            List<EmbeddedRight> rights = service.deleteRight(req, RULE, DELETE);
            return new SuccessResponse<>(MSG_DELETED, rights);
        }, transformer);
    }
}
