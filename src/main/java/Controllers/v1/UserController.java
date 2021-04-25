package Controllers.v1;
import DTO.RuleDTO;
import Exceptions.DataException;
import Models.User;
import Responses.SuccessResponse;
import Services.v1.UserService;
import Transformers.JsonTransformer;
import Utils.constants.DefaultActions;
import Utils.constants.DefaultRights;
import Utils.v1.RightManager;
import dev.morphia.Datastore;

import java.util.List;

import static spark.Spark.*;

/**
 * Class controller of users API
 */
public class UserController {

    /**
     * Enum with exception messages for controller
     */
    private enum Messages {
        LIST("User list successfully founded"),
        USER("Successfully found user"),
        MARK_TO_REMOVE("Successfully marked to remove");
        // Property for text message
        private final String message;

        /**
         * Constructor for enum
         * @param message text message
         */
        Messages(String message) {
            this.message = message;
        }

        /**
         * Getter for messages
         * @return text of message
         */
        public String getMessage() {
            return message;
        }
    };

    /**
     * Method for get routes for users API
     * @param store datastore object (Morphia connection)
     * @param transformer transformer object (for transform answer to JSON)
     */
    public static void routes(Datastore store, JsonTransformer transformer) {
        // Route for work with users list
        get("", (req, res) -> {
            // Get rule data transfer object for request
            RuleDTO rule = RightManager.getRuleByRequest_Token(req, store, DefaultRights.USERS.getName(), DefaultActions.READ.getName());
            // Get list of users documents
            List<User> users = UserService.getList(req, store, rule);
            // Check users list size.
            // If size equal - fail method status & message
            // If size not equal - success method status & message
            if (users.size() != 0) {
                return new SuccessResponse<List<User>>(Messages.LIST.getMessage(), users);
            } else {
                Error error = new Error("Users list is empty");
                throw new DataException("NotFound", error);
            }
        }, transformer);
        // Routes for work with user document
        // Get user document by UUID
        get("/:id", (req, res) -> {
            User user = UserService.getUserById(req, store);
            if (user != null) {
                return new SuccessResponse<User>(Messages.USER.getMessage(), user);
            } else {
                Error error = new Error("Can not find user by id");
                throw  new DataException("NotFound", error);
            }
        }, transformer);
        // Update user document by UUID
        put("/:id", (request, response) -> UserService.updateUser());
        // Mark to remove user document by UUID
        delete("/:id", (req, res) -> {
            User user = UserService.markToRemove(req, store);
            return new SuccessResponse<User>(Messages.MARK_TO_REMOVE.getMessage(), user);
        }, transformer);
    }
}
