package Controllers.v1;

import Models.UserProperty;
import Responses.StandardResponse;
import Services.v1.UserProfileService;
import Transformers.JsonTransformer;
import dev.morphia.Datastore;
import java.util.List;
import static spark.Spark.*;

/**
 * Class with routes for work with profile documents
 */
public class UserProfileController {
    /**
     * Method with init routes for work with profile property documents
     * @param store Morphia datastore
     * @param transformer JSON response transformer
     */
    public static void routes(Datastore store, JsonTransformer transformer) {
        // Route for get user profile
        get("/:id/profile", (req, res)-> {
            List<UserProperty> profile = UserProfileService.getUserProfile(req, store);
            String status = (profile != null) ? "success" : "fail";
            String message = (profile != null)
                    ? "Successfully found user profile"
                    : "Can not found user profile";
            return new StandardResponse<List<UserProperty>>(status, message, profile);
        }, transformer);
        // Route for create profile property
        post("/:id/profile", (req, res) -> {
            UserProperty property = UserProfileService.createUserProfileProperty(req, store);
            String status = (property != null) ? "success" : "fail";
            String message = (property != null)
                    ? "Successfully created profile property"
                    : "Can not create profile property";
            return new StandardResponse<UserProperty>(status, message, property);
        }, transformer);
        // Route for get user profile property by ID
        get("/:id/profile/:property_id", (req, res) -> {
            UserProperty property = UserProfileService.getUserProfilePropertyById(req, store);
            String status = (property != null) ? "success" : "fail";
            String message = (property != null)
                    ? "Founded profile property"
                    : "Can not found profile property";
            return new StandardResponse<UserProperty>(status, message, property);
        }, transformer);
        // Update user profile property by property UUID (user find by UUID)
        put("/:id/profile/:property_id", (request, response) -> "Update user profile property");
        // Remove user profile property by UUID (user find by UUID)
        delete("/:id/profile/:property_id", (request, response) -> "Remove user profile property");
    }
}
