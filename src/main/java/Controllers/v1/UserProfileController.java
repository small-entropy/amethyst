package Controllers.v1;

import DataTransferObjects.RuleDTO;
import Exceptions.DataException;
import Models.UserProperty;
import Responses.SuccessResponse;
import Services.v1.UserProfileService;
import Sources.ProfileSource;
import Transformers.JsonTransformer;
import Utils.constants.DefaultActions;
import Utils.constants.DefaultRights;
import Utils.v1.RightManager;
import dev.morphia.Datastore;
import java.util.Arrays;
import java.util.List;
import static spark.Spark.*;

/**
 * Class with routes for work with profile documents
 */
public class UserProfileController {
    
    private static final List<String> BLACK_LIST = Arrays.asList("registered");

    /**
     * Enum for exception messages
     */
    private enum Messages {
        PROFILE("Successfully get user profile"),
        PROPERTY("Successfully get user profile property"),
        UPDATED("Profile property successfully updated"),
        DELETED("Profile property removed");
        // Property for text message
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
     * Method with init routes for work with profile property documents
     * @param store Morphia datastore
     * @param transformer JSON response transformer
     */
    public static void routes(Datastore store, JsonTransformer transformer) {
        ProfileSource source = new ProfileSource(store, BLACK_LIST);
        
        // Route for get user profile
        get("/:id/profile", (req, res)-> {
            List<UserProperty> profile = UserProfileService.getUserProfile(req, source);
            return new SuccessResponse<>(Messages.PROFILE.getMessage(), profile);
        }, transformer);
        
        // Route for create profile property
        post("/:id/profile", (req, res) -> {
            UserProperty property = UserProfileService.createUserProfileProperty(req, source);
            return new SuccessResponse<>(Messages.PROPERTY.getMessage(), property);
        }, transformer);
        
        // Route for get user profile property by ID
        get("/:id/profile/:property_id", (req, res) -> {
            UserProperty property = UserProfileService.getUserProfilePropertyById(req, source);
            if (property != null) {
                return new SuccessResponse<>(Messages.PROPERTY.getMessage(), property);
            } else {
                Error error = new Error("Can found property with this id");
                throw new DataException("NotFound", error);
            }
        }, transformer);
        
        // Update user profile property by property UUID (user find by UUID)
        put("/:id/profile/:property_id", (req, res) -> {
            // Get user rule for update users documents
            RuleDTO rule = RightManager.getRuleByRequest_Token(req, source, DefaultRights.USERS.getName(), DefaultActions.UPDATE.getName());
            // Try update user profile property
            UserProperty property = UserProfileService.updateUserProperty(req, source, rule);
            // Return successfully response
            return new SuccessResponse<>(Messages.UPDATED.getMessage(), property);
        }, transformer);
        
        // Remove user profile property by UUID (user find by UUID)
        delete("/:id/profile/:property_id", (req, res) -> {
            RuleDTO rule = RightManager.getRuleByRequest_Token(req, source, DefaultRights.USERS.getName(), DefaultActions.DELETE.getName());
            List<UserProperty> profile = UserProfileService.deleteProfileProperty(req, source, rule);
            return new SuccessResponse<>(Messages.DELETED.getMessage(), profile);
        }, transformer);
    }
}
