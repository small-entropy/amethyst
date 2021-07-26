package synthwave.services.v1.companies;

import java.util.Arrays;

import dev.morphia.Datastore;
import platform.dto.RuleDTO;
import synthwave.services.core.companies.CoreCompanyProfileService;

/**
 * Class for work with company profile
 * @author small-entropy
 * @version 1
 */
public class CompanyProfileService extends CoreCompanyProfileService {
    
    /**
     * Default constructor for company profile service
     * @param datastore Morphia datastore object
     */
    public CompanyProfileService(Datastore datastore) {
        super(datastore, Arrays.asList("created"));
    }

    @Override
    protected boolean checkExistHasAccess(RuleDTO rule, boolean isTrusted) {
        return (isTrusted) ? rule.isMyPublic() : rule.isOtherPublic();
    }
}
