package Controllers.v1;

import DTO.RuleDTO;
import Exceptions.DataException;
import Models.UserProperty;
import Responses.StandardResponse;
import Responses.SuccessResponse;
import Services.v1.UserPropertyService;
import Transformers.JsonTransformer;
import Utils.constants.DefaultActions;
import Utils.constants.DefaultRights;
import Utils.v1.RightManager;
import dev.morphia.Datastore;

import java.util.List;

import static spark.Spark.*;

/**
 * Static class with routes for work with user property documents
 */
public class UserPropertyController {

    /**
     * Enum for exception messages
     */
    private enum Messages {
        PROPERTIES("Successfully get user properties"),
        PROPERTY("Successfully get user property"),
        CREATED("Successfully created user property");
        // Message property
        private final String message;

        /**
         * Constructor for enum
         * @param message text of message
         */
        Messages(String message) {
            this.message = message;
        }

        /**
         * Getter for message property
         * @return value of message property
         */
        public String getMessage() {
            return message;
        }
    }

    /**
     * Method with init routes for work with user property documents
     * @param store Morphia datastore
     * @param transformer JSON response transformer
     */
    public static void routes(Datastore store, JsonTransformer transformer) {
        // Routes for work with user properties
        // Get user properties list by user UUID
        get("/:id/properties", (req, res) -> {
            RuleDTO rule = RightManager.getRuleByRequest_Token(req, store, DefaultRights.USERS.getName(), DefaultActions.READ.getName());
            List<UserProperty> properties = UserPropertyService.getUserProperties(req, store, rule);
            return new SuccessResponse<List<UserProperty>>(Messages.PROPERTIES.getMessage(), properties);
        }, transformer);
        // Create new user property (user find by UUID)
        // This method only for create public property!
        // For create not public property use other method!
        post("/:id/properties", (req, res) -> {
            RuleDTO rule = RightManager.getRuleByRequest_Token(req, store, DefaultRights.USERS.getName(), DefaultActions.CREAT.getName());
            UserProperty userProperty = UserPropertyService.createUserProperty(req, store, rule);
            return new SuccessResponse<UserProperty>(Messages.CREATED.getMessage(), userProperty);
        }, transformer);
        // Get user property by UUID (user find by UUID)
        get("/:id/properties/:property_id", (req, res) -> {
            RuleDTO rule = RightManager.getRuleByRequest_Token(req, store, DefaultRights.USERS.getName(), DefaultActions.READ.getName());
            UserProperty property = UserPropertyService.getUserPropertyById(req, store, rule);
            if (property != null) {
                return new SuccessResponse<UserProperty>(Messages.PROPERTY.getMessage(), property);
            } else {
                Error error = new Error("Can not find user property");
                throw new DataException("NotFound", error);
            }
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
