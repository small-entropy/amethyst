package Controllers.v1;

import Controllers.base.BaseUserController;
import Exceptions.DataException;
import Models.Standalones.User;
import Utils.responses.SuccessResponse;
import Services.v1.UserService;
import Utils.transformers.JsonTransformer;
import dev.morphia.Datastore;
import java.util.List;
import static spark.Spark.*;

/**
 * Class controller of users API
 */
public class UserController extends BaseUserController {
    
    /**
     * Method for get routes for users API
     * @param store datastore object (Morphia connection)
     * @param transformer transformer object (for transform answer to JSON)
     */
    public static void routes(Datastore store, JsonTransformer transformer) {
        
        UserService service = new UserService(store);
        
        // Route for work with users list
        get("", (req, res) -> {
            // Get list of users documents
            List<User> users = service.getList(req, RULE, READ);
            // Check users list size.
            // If size equal - fail method status & message
            // If size not equal - success method status & message
            if (!users.isEmpty()) {
                return new SuccessResponse<>(MSG_LIST, users);
            } else {
                Error error = new Error("Users list is empty");
                throw new DataException("NotFound", error);
            }
        }, transformer);
        
        // Routes for work with user document
        // Get user document by UUID
        get("/:user_id", (req, res) -> {
            User user = service.getUserById(req);
            if (user != null) {
                return new SuccessResponse<>(MSG_ENTITY, user);
            } else {
                Error error = new Error("Can not find user by id");
                throw  new DataException("NotFound", error);
            }
        }, transformer);
        
        // Mark to remove user document by UUID
        delete("/:user_id", (req, res) -> {
            User user = service.markToRemove(req);
            return new SuccessResponse<>(MSG_DELTED, user);
        }, transformer);
    }
}
