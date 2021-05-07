package Controllers.v1;

import DataTransferObjects.RuleDTO;
import Models.UserRight;
import Responses.SuccessResponse;
import Services.v1.UserRightService;
import Sources.RightsSource;
import Transformers.JsonTransformer;
import Utils.constants.DefaultActions;
import Utils.constants.DefaultRights;
import Utils.v1.RightManager;
import dev.morphia.Datastore;
import java.util.List;
import static spark.Spark.*;

/**
 * Static class for work with user right document
 * @author small-entropy
 */
public class UserRightsController {
    
    /**
     * Enum with messages
     */
    private enum Messages {
        RIGHTS("Successfully get user rights"),
        RIGHT("Successfullly get user rights"),
        CREATED("Successfully create user right"),
        UPDATED("User right successfully updated"),
        DELETED("Successfully removed user right");
        // Field for contain message
        private final String message;
        
        /**
         * Constructor for enum
         * @param message current message
         */
        Messages(String message) {
            this.message = message;
        }

        /**
         * Getter for message field
         * @return message
         */
        public String getMessage() {
            return message;
        }
    }
    
    
    /**
     * Method with init routes for work with user right documents
     * @param store Morphia datastore (connection) object
     * @param transformer JSON response transformer
     */
    public static void routes (Datastore store, JsonTransformer transformer) {
        RightsSource source = new RightsSource(store);
        
        // Routes for work with user rights
        // Get all rights by user UUID
        get("/:id/rights", (req, res) -> {
            RuleDTO rule = RightManager.getRuleByRequest_Token(req, source, DefaultRights.USERS.getName(), DefaultActions.READ.getName());
            System.out.println(rule);
            List<UserRight> rights = UserRightService.getUserRights(req, source, rule);
            return new SuccessResponse<>(Messages.RIGHTS.getMessage(), rights);
        }, transformer);
        
        // Create new user rights (user find by UUID)
        post("/:id/rights", (req, res) -> UserRightService.createRight(req, source), transformer);
        
        // Get new user right by UUID (find user by UUID)
        get("/:id/rights/:right_id", (req, res) -> {
            RuleDTO rule = RightManager.getRuleByRequest_Token(req, source, DefaultRights.USERS.getName(), DefaultActions.READ.getName());
            UserRight right = UserRightService.getUserRightById(req, source, rule);
            return new SuccessResponse<>(Messages.RIGHT.getMessage() ,right);
        }, transformer);
        
        // Update user right by UUID (find user by UUID)
        put("/:id/rights/:right_id", (req, res) -> UserRightService.updateRight(req, source), transformer);
        
        // Mark to remove user right (find user by UUID)
        delete("/:id/rights/:right_id", (req, res) -> UserRightService.deleteRight(req, source), transformer);
    }
}
