package synthwave.repositories.mongodb.v1;

import synthwave.repositories.mongodb.base.BaseUserPropertyRepository;
import dev.morphia.Datastore;
import java.util.List;

/**
 * Class of repository for profile documents
 * @author small-entropy
 */
public class UserProfileRepository extends BaseUserPropertyRepository {
    
    /**
     * Constructor for source
     * @param datastore Morphia datastore object
     * @param blaclList blaclist for profile documents
     */
    public UserProfileRepository(Datastore datastore, List<String> blaclList) {
        super(datastore, "profile", blaclList);
    }
}
