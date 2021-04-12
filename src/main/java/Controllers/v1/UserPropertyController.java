package Controllers.v1;

import Models.UserProperty;
import Responses.StandardResponse;
import Services.UserPropertyService;
import Transformers.JsonTransformer;
import dev.morphia.Datastore;

import java.util.List;

import static spark.Spark.*;

/**
 * Static class with routes for work with user property documents
 */
public class UserPropertyController {
    /**
     * Method with init routes for work with user property documents
     * @param store Morphia datastore
     * @param transformer JSON response transformer
     */
    public static void routes(Datastore store, JsonTransformer transformer) {
        // Routes for work with user properties
        // Get user properties list by user UUID
        get("/:id/properties", (req, res) -> {
            List<UserProperty> properties = UserPropertyService.getUserProperties(req, store);
            String status = (properties != null) ? "success" : "fail";
            String message = (properties != null)
                    ? "User properties found"
                    : "Can not find user properties";
            return new StandardResponse<List<UserProperty>>(status, message, properties);
        }, transformer);
        // Create new user property (user find by UUID)
        // This method only for create public property!
        // For create not public property use other method!
        post("/:id/properties", (req, res) -> {
            UserProperty userProperty = UserPropertyService.createUserProperty(req, store);
            String status = (userProperty != null) ? "success" : "fail";
            String message = (userProperty != null)
                    ? "Successfully create user property"
                    : "Can not create user property";
            return new StandardResponse<UserProperty>(status, message, userProperty);
        }, transformer);
        // Get user property by UUID (user find by UUID)
        get("/:id/properties/:property_id", (req, res) -> {
            UserProperty property = UserPropertyService.getUserPropertyById(req, store);
            String status = (property != null) ? "success" : "fail";
            String message = (property != null)
                    ? "User property found"
                    : "Can not find user property";
            return new StandardResponse<UserProperty>(status, message, property);
        }, transformer);
        // Update user property by property UUID (user find by UUID)
        put("/:id/properties/:property_id", (request, response) -> "Update user property");
        // Remove user property by UUID (user find by UUID)
        delete("/:id/properties/:property_id", (req, res) -> {
            List<UserProperty> properties = UserPropertyService.removeUserProperty(req, store);
            String status = (properties != null) ? "success" : "fail";
            String message = (properties != null)
                    ? "Successfully remove user property"
                    : "Can not remove user property";
            return new StandardResponse<List<UserProperty>>(status, message, properties);
        }, transformer);
    }
}
