package synthwave.services.v1.categories;

import java.util.Arrays;
import java.util.List;

import spark.Request;

import dev.morphia.Datastore;
import platform.dto.RuleDTO;
import platform.exceptions.AccessException;
import platform.exceptions.DataException;

import synthwave.models.mongodb.embeddeds.EmbeddedProperty;
import synthwave.services.core.categories.CoreCategoryPropertiesService;

/**
 * Class or create service for work with category propeties
 * @author small-entropy
 * @version 1
 */
public class CategoryPropertiesService extends CoreCategoryPropertiesService {

    /**
     * Default constructore for category properties service. Create
     * instnce by datastore
     * @param datastore Morphia datastore object
     */
    public CategoryPropertiesService(Datastore datastore) {
        super(datastore, Arrays.asList("count"));
    }

    @Override
    protected boolean checkExistHasAccess(RuleDTO rule,boolean isTrusted) {
        return (isTrusted) ? rule.isMyGlobal() : rule.isOtherGlobal();
    }
}
