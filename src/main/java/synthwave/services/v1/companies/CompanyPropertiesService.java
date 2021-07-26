package synthwave.services.v1.companies;

import java.util.Arrays;

import dev.morphia.Datastore;
import synthwave.services.core.companies.CoreCompanyPropertiesService;

public class CompanyPropertiesService extends CoreCompanyPropertiesService {
    
    public CompanyPropertiesService(Datastore datastore) {
        super(datastore, Arrays.asList("created"));
    }

}
