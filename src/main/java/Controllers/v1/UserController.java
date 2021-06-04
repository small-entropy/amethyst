package Controllers.v1;
import DataTransferObjects.RuleDTO;
import Exceptions.DataException;
import Models.Standalones.User;
import Responses.SuccessResponse;
import Services.v1.UserService;
import Sources.UsersSource;
import Transformers.JsonTransformer;
import Utils.constants.DefaultActions;
import Utils.constants.DefaultRights;
import Utils.constants.ResponseMessages;
import Utils.v1.RightManager;
import dev.morphia.Datastore;
import java.util.List;
import static spark.Spark.*;

/**
 * Class controller of users API
 */
public class UserController {
    /**
     * Method for get routes for users API
     * @param store datastore object (Morphia connection)
     * @param transformer transformer object (for transform answer to JSON)
     */
    public static void routes(Datastore store, JsonTransformer transformer) {
        UsersSource source = new UsersSource(store);
        
        // Route for work with users list
        get("", (req, res) -> {
            // Get rule data transfer object for request
            RuleDTO rule = RightManager.getRuleByRequest_Token(req, source, DefaultRights.USERS.getName(), DefaultActions.READ.getName());
            // Get list of users documents
            List<User> users = UserService.getList(req, source, rule);
            // Check users list size.
            // If size equal - fail method status & message
            // If size not equal - success method status & message
            if (!users.isEmpty()) {
                return new SuccessResponse<>(ResponseMessages.USERS_LIST.getMessage(), users);
            } else {
                Error error = new Error("Users list is empty");
                throw new DataException("NotFound", error);
            }
        }, transformer);
        // Routes for work with user document
        // Get user document by UUID
        get("/:user_id", (req, res) -> {
            User user = UserService.getUserById(req, source);
            if (user != null) {
                return new SuccessResponse<>(ResponseMessages.USER.getMessage(), user);
            } else {
                Error error = new Error("Can not find user by id");
                throw  new DataException("NotFound", error);
            }
        }, transformer);
        
        // Mark to remove user document by UUID
        delete("/:user_id", (req, res) -> {
            User user = UserService.markToRemove(req, source);
            return new SuccessResponse<>(ResponseMessages.USER_DEACTIVATED.getMessage(), user);
        }, transformer);
    }
}
