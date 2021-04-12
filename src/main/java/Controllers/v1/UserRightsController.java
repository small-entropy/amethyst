package Controllers.v1;

import Models.UserRight;
import Responses.StandardResponse;
import Services.UserRightService;
import Transformers.JsonTransformer;
import dev.morphia.Datastore;

import java.util.List;

import static spark.Spark.*;

/**
 * Static class for work with user right document
 */
public class UserRightsController {
    /**
     * Method with init routes for work with user right documents
     * @param store Morphia datastore (connection) object
     * @param transformer JSON response transformer
     */
    public static void routes (Datastore store, JsonTransformer transformer) {
        // Routes for work with user rights
        // Get all rights by user UUID
        get("/:id/rights", (req, res) -> {
            List<UserRight> rights = UserRightService.getUserRights(req, store);
            String status = (rights != null) ? "success" : "fail";
            String message = (rights != null)
                    ? "Successfully founded rights for user"
                    : "Can not found rights for user";
            return new StandardResponse<List<UserRight>>(status, message, rights);
        }, transformer);
        // Create new user rights (user find by UUID)
        post("/:id/rights", (req, res) -> UserRightService.createRight(req, store), transformer);
        // Get new user right by UUID (find user by UUID)
        get("/:id/rights/:right_id", (req, res) -> {
            UserRight right = UserRightService.getUserRightById(req, store);
            String status = (right != null) ? "success" : "fail";
            String message = (right != null)
                    ? "Successfully found user right"
                    : "Can not found user right";
            return new StandardResponse<UserRight>(status, message, right);
        }, transformer);
        // Update user right by UUID (find user by UUID)
        put("/:id/rights/:right_id", (req, res) -> UserRightService.updateRight(req, store), transformer);
        // Mark to remove user right (find user by UUID)
        delete("/:id/rights/:right_id", (req, res) -> UserRightService.deleteRight(req, store), transformer);
    }
}
