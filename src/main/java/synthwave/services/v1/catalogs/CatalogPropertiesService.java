package synthwave.services.v1.catalogs;

import java.util.Arrays;

import dev.morphia.Datastore;
import engine.dto.RuleDTO;
import synthwave.services.core.catalogs.CoreCatalogPropertiesService;

/**
 * Class for create catalog properties service
 * @author small-entopy
 * @version 1
 */
public class CatalogPropertiesService extends CoreCatalogPropertiesService {
    
    /**
     * Default constructor for servis works with 
     * catalog properties field. Create by datastore
     * @param datastore Morphia datastore object
     */
    public CatalogPropertiesService(Datastore datastore) {
        super(datastore, Arrays.asList("count"));
    }

    @Override
    protected boolean checkExistHasAccess(RuleDTO rule, boolean isTrusted) {
        return (isTrusted) ? rule.isMyGlobal() : rule.isOtherGlobal();
    }
}
