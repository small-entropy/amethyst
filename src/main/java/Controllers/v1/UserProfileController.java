package Controllers.v1;

import Controllers.base.BaseUserProfileController;
import DataTransferObjects.v1.RuleDTO;
import Exceptions.DataException;
import Models.Embeddeds.EmbeddedProperty;
import Utils.responses.SuccessResponse;
import Services.v1.UserProfileService;
import Repositories.v1.ProfileRepository;
import Repositories.v1.UsersRepository;
import Utils.transformers.JsonTransformer;
import dev.morphia.Datastore;
import java.util.Arrays;
import java.util.List;
import static spark.Spark.*;

/**
 * Class with routes for work with profile documents
 */
public class UserProfileController extends BaseUserProfileController {
    
    private static final List<String> BLACK_LIST = Arrays.asList("registered");
    
    /**
     * Method with init routes for work with profile property documents
     * @param datastore Morphia datastore
     * @param transformer JSON response transformer
     */
    public static void routes(Datastore datastore, JsonTransformer transformer) {
        
        ProfileRepository profileRepository = new ProfileRepository(
                datastore, 
                BLACK_LIST
        );
        
        UsersRepository usersRepository = new UsersRepository(datastore);
        
        // Route for get user profile
        get("/:user_id/profile", (req, res)-> {
            List<EmbeddedProperty> profile = UserProfileService
                    .getUserProfile(req, profileRepository);
            return new SuccessResponse<>(MSG_LIST, profile);
        }, transformer);
        
        // Route for create profile property
        post("/:user_id/profile", (req, res) -> {
            EmbeddedProperty property = UserProfileService
                    .createUserProfileProperty(req, profileRepository);
            return new SuccessResponse<>(MSG_CREATED, property);
        }, transformer);
        
        // Route for get user profile property by ID
        get("/:user_id/profile/:property_id", (req, res) -> {
            EmbeddedProperty property = UserProfileService
                    .getUserProfilePropertyById(req, profileRepository);
            if (property != null) {
                return new SuccessResponse<>(MSG_ENTITY, property);
            } else {
                Error error = new Error("Can found property with this id");
                throw new DataException("NotFound", error);
            }
        }, transformer);
        
        // Update user profile property by property UUID (user find by UUID)
        put("/:user_id/profile/:property_id", (req, res) -> {
            // Get user rule for update users documents
            RuleDTO rule = getRule(req, usersRepository, RULE, UPDATE);
            // Try update user profile property
            EmbeddedProperty property = UserProfileService
                    .updateUserProperty(req, profileRepository, rule);
            // Return successfully response
            return new SuccessResponse<>(MSG_UPDATED, property);
        }, transformer);
        
        // Remove user profile property by UUID (user find by UUID)
        delete("/:user_id/profile/:property_id", (req, res) -> {
            RuleDTO rule = getRule(req, usersRepository, RULE, DELETE);
            List<EmbeddedProperty> profile = UserProfileService
                    .deleteProfileProperty(req, profileRepository, rule);
            return new SuccessResponse<>(MSG_DELETED, profile);
        }, transformer);
    }
}
