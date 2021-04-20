package Controllers.v1;
import DTO.RuleDTO;
import Models.User;
import Responses.StandardResponse;
import Services.v1.UserService;
import Transformers.JsonTransformer;
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
        // Route for work with users list
        get("", (req, res) -> {
            // Get rule data transfer object for request
            RuleDTO rule = RightManager.getRuleByRequest_Token(req, store, "users_right", "read");
            // Get list of users documents
            List<User> users = UserService.getList(req, store, rule);
            // Check users list size.
            // If size equal - fail method status & message
            // If size not equal - success method status & message
            boolean isEmptyUsers = users.size() != 0;
            // Set status, message
            String status = isEmptyUsers ? "success" : "fail";
            String message = isEmptyUsers ? "Success finding users" : "Can not finding users";
            return new StandardResponse<List<User>>(status, message, users);
        }, transformer);
        // Routes for work with user document
        // Get user document by UUID
        get("/:id", (req, res) -> {
            User user = UserService.getUserById(req, store);
            String status = (user != null) ? "success" : "fails";
            String message = (user != null)
                    ? "Successfully user found"
                    : "Can not find user";
            return new StandardResponse<User>(status, message, user);
        }, transformer);
        // Update user document by UUID
        put("/:id", (request, response) -> UserService.updateUser());
        // Mark to remove user document by UUID
        delete("/:id", (req, res) -> {
            User user = UserService.markToRemove(req, store);
            String status = (user != null) ? "success" : "fail";
            String message = (user != null)
                    ? "User successfully deactivated"
                    : "Can not deactivate user";
            return new StandardResponse<User>(status, message, user);
        }, transformer);
    }
}
