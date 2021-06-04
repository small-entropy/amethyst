package Controllers.v1;

import DataTransferObjects.RuleDTO;
import Models.Embeddeds.UserRight;
import Responses.SuccessResponse;
import Services.v1.UserRightService;
import Sources.RightsSource;
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
 * Static class for work with user right document
 * @author small-entropy
 */
public class UserRightsController {
    
    private final static List<String> BLACK_LIST = Arrays.asList("users_right", "catalogs_right");
    
    /**
     * Method with init routes for work with user right documents
     * @param store Morphia datastore (connection) object
     * @param transformer JSON response transformer
     */
    public static void routes (Datastore store, JsonTransformer transformer) {
        RightsSource source = new RightsSource(store, BLACK_LIST);
        
        // Routes for work with user rights
        // Get all rights by user UUID
        get("/:user_id/rights", (req, res) -> {
            RuleDTO rule = RightManager.getRuleByRequest_Token(req, source, DefaultRights.USERS.getName(), DefaultActions.READ.getName());
            List<UserRight> rights = UserRightService.getUserRights(req, source, rule);
            return new SuccessResponse<>(ResponseMessages.RIGHTS.getMessage(), rights);
        }, transformer);
        
        // Create new user rights (user find by UUID)
        post("/:user_id/rights", (req, res) -> {
            RuleDTO rule = RightManager.getRuleByRequest_Token(req, source, DefaultRights.USERS.getName(), DefaultActions.CREATE.getName());
            UserRight right = UserRightService.createUserRight(req, source, rule);
            return new SuccessResponse<>(ResponseMessages.RIGHT_CREATED.getMessage(), right);
        }, transformer);
        
        // Get new user right by UUID (find user by UUID)
        get("/:user_id/rights/:right_id", (req, res) -> {
            RuleDTO rule = RightManager.getRuleByRequest_Token(req, source, DefaultRights.USERS.getName(), DefaultActions.READ.getName());
            UserRight right = UserRightService.getUserRightById(req, source, rule);
            return new SuccessResponse<>(ResponseMessages.RIGHT.getMessage() ,right);
        }, transformer);
        
        // Update user right by UUID (find user by UUID)
        put("/:user_id/rights/:right_id", (req, res) -> {
            RuleDTO rule = RightManager.getRuleByRequest_Token(req, source, DefaultRights.USERS.getName(), DefaultActions.UPDATE.getName());
            UserRight right = UserRightService.updateRight(req, source, rule);
            return new SuccessResponse<>(ResponseMessages.RIGHT_UPDATED.getMessage(), right);
        }, transformer);
        
        // Mark to remove user right (find user by UUID)
        delete("/:user_id/rights/:right_id", (req, res) -> {
            RuleDTO rule = RightManager.getRuleByRequest_Token(req, source, DefaultRights.USERS.getName(), DefaultActions.DELETE.getName());
            List<UserRight> rights = UserRightService.deleteRight(req, source, rule);
            return new SuccessResponse<>(ResponseMessages.RIGHT_DELETED.getMessage(), rights);
        }, transformer);
    }
}
