package synthwave.services.v1.users;
import engine.dto.RuleDTO;
import synthwave.services.core.users.CoreUserProfileService;
import dev.morphia.Datastore;
import java.util.Arrays;

/**
 * Class controller for work with user profile properties
 * @author small-entropy
 * @version 1
 */
public class UserProfileService extends CoreUserProfileService {
    
    public UserProfileService(Datastore datastore) {
        super(
                datastore,
                Arrays.asList("registered")
        );
    }
    
    @Override
    protected boolean checkExistHasAccess(RuleDTO rule, boolean isTrusted) {
		return (isTrusted) ? rule.isMyPublic() : rule.isOtherPublic();
	}
}
