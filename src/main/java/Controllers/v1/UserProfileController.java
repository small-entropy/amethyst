package Controllers.v1;

import DataTransferObjects.RuleDTO;
import Exceptions.DataException;
import Models.Embeddeds.UserProperty;
import Responses.SuccessResponse;
import Services.v1.UserProfileService;
import Sources.ProfileSource;
import Transformers.JsonTransformer;
import Utils.constants.DefaultActions;
import Utils.constants.DefaultRights;
import Utils.constants.ResponseMessages;
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
     * Method with init routes for work with profile property documents
     * @param store Morphia datastore
     * @param transformer JSON response transformer
     */
    public static void routes(Datastore store, JsonTransformer transformer) {
        ProfileSource source = new ProfileSource(store, BLACK_LIST);
        
        // Route for get user profile
        get("/:user_id/profile", (req, res)-> {
            List<UserProperty> profile = UserProfileService.getUserProfile(req, source);
            return new SuccessResponse<>(ResponseMessages.PROFILE.getMessage(), profile);
        }, transformer);
        
        // Route for create profile property
        post("/:user_id/profile", (req, res) -> {
            UserProperty property = UserProfileService.createUserProfileProperty(req, source);
            return new SuccessResponse<>(ResponseMessages.PROFILE_CREATED.getMessage(), property);
        }, transformer);
        
        // Route for get user profile property by ID
        get("/:user_id/profile/:property_id", (req, res) -> {
            UserProperty property = UserProfileService.getUserProfilePropertyById(req, source);
            if (property != null) {
                return new SuccessResponse<>(ResponseMessages.PROFILE_PROPERTY.getMessage(), property);
            } else {
                Error error = new Error("Can found property with this id");
                throw new DataException("NotFound", error);
            }
        }, transformer);
        
        // Update user profile property by property UUID (user find by UUID)
        put("/:user_id/profile/:property_id", (req, res) -> {
            // Get user rule for update users documents
            RuleDTO rule = RightManager.getRuleByRequest_Token(req, source, DefaultRights.USERS.getName(), DefaultActions.UPDATE.getName());
            // Try update user profile property
            UserProperty property = UserProfileService.updateUserProperty(req, source, rule);
            // Return successfully response
            return new SuccessResponse<>(ResponseMessages.PROFILE_UPDATED.getMessage(), property);
        }, transformer);
        
        // Remove user profile property by UUID (user find by UUID)
        delete("/:user_id/profile/:property_id", (req, res) -> {
            RuleDTO rule = RightManager.getRuleByRequest_Token(req, source, DefaultRights.USERS.getName(), DefaultActions.DELETE.getName());
            List<UserProperty> profile = UserProfileService.deleteProfileProperty(req, source, rule);
            return new SuccessResponse<>(ResponseMessages.PROFILE_DELETED.getMessage(), profile);
        }, transformer);
    }
}
