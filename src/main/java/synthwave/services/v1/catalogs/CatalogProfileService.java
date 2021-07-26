package synthwave.services.v1.catalogs;

import java.util.Arrays;
import java.util.List;

import dev.morphia.Datastore;
import platform.dto.RuleDTO;
import platform.exceptions.AccessException;
import platform.exceptions.DataException;
import platform.exceptions.TokenException;
import spark.Request;
import synthwave.models.mongodb.embeddeds.EmbeddedProperty;
import synthwave.services.core.catalogs.CoreCatalogProfileService;
import synthwave.utils.helpers.Comparator;

/**
 * Class for work with catalog profile
 * @author small-entropy
 * @version 1
 */
public class CatalogProfileService extends CoreCatalogProfileService {
	
	/**
	 * Default constructor for catalog profile service
	 * @param datastore MOrphia datastore object
	 */
	public CatalogProfileService(Datastore datastore) {
		super(datastore, Arrays.asList("created"));
	}
	
	@Override
	protected boolean checkExistHasAccess(RuleDTO rule, boolean isTrusted) {
		return (isTrusted) ? rule.isMyPublic() : rule.isOtherPublic();
	}

}
