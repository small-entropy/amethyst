package synthwave.services.v1.users;

import synthwave.services.core.users.CoreUserPropertyService;
import dev.morphia.Datastore;
import java.util.Arrays;

/**
 * Class controller for work with user properties
 */
public class UserPropertyService extends CoreUserPropertyService {
    
    public UserPropertyService(Datastore datastore) {
        super(datastore,  Arrays.asList("banned"));
    }
}
