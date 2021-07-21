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
		super(
				datastore,
				Arrays.asList("registered")
		);
	}
	
	/**
	 * Method for create catalog profile property
	 * @param request Spark request object
	 * @return created catalog profile property
	 * @throws DataException throw if can't be found some data
	 * @throws TokenException throw if send incorrect token
	 */
	public EmbeddedProperty createCatalogProperty(
			Request request
	) throws DataException, TokenException {
		boolean isTrusted = Comparator.id_fromParam_fromToken(request);
		if (isTrusted) {
			return creteaEmbeddedProperty(request);
		} else {
			Error error = new Error("Id from request not equal with id from token");
			throw new TokenException("NotEquals", error);
		}
	}
	
	/**
	 * Method for update catalog profile property
	 * @param request Spark request object
	 * @param right name of right
	 * @param action name of action
	 * @return updated property
	 */
	public EmbeddedProperty updateCatalogProperty(
			Request request,
			String right,
			String action
	) throws DataException, AccessException {
		boolean hasAccess = checkHasAccess(request, right, action);
		if (hasAccess) {
			return updatEmbeddedProperty(request);
		} else {
			Error error = new Error("Has no access to update catalog profile");
			throw new AccessException("CanNotUpdate", error);
		} 
	}
	
	/**
	 * Method for delete profile property
	 * @param request Spark request object
	 * @param right right for action
	 * @param action action name
	 * @return result of action
	 * @throws AccessException throw if user han't access for delete
	 * 							catalog profile property
	 * @throws DataException throw if can't be found some data
	 */
	public List<EmbeddedProperty> deleteCatalogProperty(
			Request request,
			String right,
			String action
	) 
		throws AccessException, DataException {
		boolean hasAccess = checkHasAccess(request, right, action);
		if (hasAccess) {
			return deletEmbeddedProperty(request);
		} else {
			Error error = new Error("Has no access to delete catalog profile property");
			throw new AccessException("CanNotDelete", error);
		}
	}
	
	@Override
	protected boolean checkExistHasAccess(
			RuleDTO rule, 
			boolean isTrusted
	) {
		return (isTrusted) ? rule.isMyPublic() : rule.isOtherPublic();
	}

}
