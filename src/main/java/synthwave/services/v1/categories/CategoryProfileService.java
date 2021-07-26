package synthwave.services.v1.categories;

import java.util.Arrays;


import dev.morphia.Datastore;
import platform.dto.RuleDTO;
import synthwave.services.core.categories.CoreCategoryProfileService;

/**
 * Class for work with categories profile
 * @author small-entropy
 * @version 1
 */
public class CategoryProfileService extends CoreCategoryProfileService {

    /**
     * Default constructor for category profile service
     * @param datastore Morphia datastore object
     */
    public CategoryProfileService(Datastore datastore) {
        super(datastore, Arrays.asList("created"));
    }

    @Override
    protected boolean checkExistHasAccess(RuleDTO rule, boolean isTrusted) {
        return (isTrusted) ? rule.isMyPublic() : rule.isMyPublic();
    }
}
