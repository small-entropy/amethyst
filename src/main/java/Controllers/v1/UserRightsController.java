package Controllers.v1;

import Controllers.base.BaseUserRightsController;
import DataTransferObjects.RuleDTO;
import Models.Embeddeds.EmbeddedRight;
import Responses.SuccessResponse;
import Services.v1.UserRightService;
import Repositories.v1.RightsRepository;
import Repositories.v1.UsersRepository;
import Transformers.JsonTransformer;
import dev.morphia.Datastore;
import java.util.Arrays;
import java.util.List;
import static spark.Spark.*;

/**
 * Static class for work with user right document
 * @author small-entropy
 */
public class UserRightsController extends BaseUserRightsController {
    
    private final static List<String> BLACK_LIST = Arrays.asList(
            "users_right",
            "catalogs_right"
    );
    
    /**
     * Method with init routes for work with user right documents
     * @param datastore Morphia datastore (connection) object
     * @param transformer JSON response transformer
     */
    public static void routes (Datastore datastore, JsonTransformer transformer) {
        
        RightsRepository rightsRepository = new RightsRepository(
                datastore, 
                BLACK_LIST
        );
        
        UsersRepository usersRepository = new UsersRepository(datastore);
        
        // Routes for work with user rights
        // Get all rights by user UUID
        get("/:user_id/rights", (req, res) -> {
            RuleDTO rule = getRule(req, usersRepository, RULE, READ);
            List<EmbeddedRight> rights = UserRightService
                    .getUserRights(req, rightsRepository, rule);
            return new SuccessResponse<>(MSG_LIST, rights);
        }, transformer);
        
        // Create new user rights (user find by UUID)
        post("/:user_id/rights", (req, res) -> {
            RuleDTO rule = getRule(req, usersRepository, RULE, CREATE);
            EmbeddedRight right = UserRightService
                    .createUserRight(req, rightsRepository, rule);
            return new SuccessResponse<>(MSG_CREATED, right);
        }, transformer);
        
        // Get new user right by UUID (find user by UUID)
        get("/:user_id/rights/:right_id", (req, res) -> {
            RuleDTO rule = getRule(req, usersRepository, RULE, READ);
            EmbeddedRight right = UserRightService.getUserRightById(
                    req,
                    rightsRepository,
                    rule
            );
            return new SuccessResponse<>(MSG_ENTITY ,right);
        }, transformer);
        
        // Update user right by UUID (find user by UUID)
        put("/:user_id/rights/:right_id", (req, res) -> {
            RuleDTO rule = getRule(req, usersRepository, RULE, UPDATE);
            EmbeddedRight right = UserRightService.updateRight(
                    req, 
                    rightsRepository, 
                    rule
            );
            return new SuccessResponse<>(MSG_UPDATED, right);
        }, transformer);
        
        // Mark to remove user right (find user by UUID)
        delete("/:user_id/rights/:right_id", (req, res) -> {
            RuleDTO rule = getRule(req, usersRepository, RULE, DELETE);
            List<EmbeddedRight> rights = UserRightService
                    .deleteRight(req, rightsRepository, rule);
            return new SuccessResponse<>(MSG_DELETED, rights);
        }, transformer);
    }
}
