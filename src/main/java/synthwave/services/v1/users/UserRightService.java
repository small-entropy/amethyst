package synthwave.services.v1.users;

import engine.dto.RuleDTO;
import synthwave.services.core.users.CoreRightService;
import dev.morphia.Datastore;
import java.util.Arrays;

/**
 * Class service for work with user right document
 */
public class UserRightService extends CoreRightService {
    
    public UserRightService(Datastore datastore) {
        super(
                datastore,
                Arrays.asList("users_right", "catalogs_right")
        );
    }

    @Override
    protected boolean checkExistHasAccess(RuleDTO rule, boolean isTrusted) {
		return (isTrusted) ? rule.isMyGlobal() : rule.isOtherGlobal();
	}
}
