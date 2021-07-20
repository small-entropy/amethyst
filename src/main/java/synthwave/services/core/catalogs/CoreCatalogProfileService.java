package synthwave.services.core.catalogs;

import java.util.Arrays;
import java.util.List;

import org.bson.types.ObjectId;

import dev.morphia.Datastore;
import platform.exceptions.DataException;
import platform.utils.helpers.ParamsManager;
import spark.Request;
import synthwave.dto.PropertyDTO;
import synthwave.models.mongodb.embeddeds.EmbeddedProperty;
import synthwave.repositories.mongodb.v1.CatalogProfileRepository;
import synthwave.services.base.BaseDocumentService;

/**
 * Base class for work with catalog profile property list
 * @author small-entropy
 */
public abstract class CoreCatalogProfileService 
	extends BaseDocumentService<CatalogProfileRepository>{
	
	/**
	 * Default core catalog profile service constructor. Create 
	 * instance by datastore & blacklist
	 * @param datastore Morphia datastore object
	 * @param blacList  blacklist
	 */
	public CoreCatalogProfileService(
			Datastore datastore,
			List<String> blacList
	) {
		super(
				datastore, 
				new CatalogProfileRepository(datastore, blacList),
				new String[] {},
				new String[] {},
				new String[] {}
		);
	}
	
	/**
	 * Method for get default profile properties list
	 * @return list of catalog properties for profile
	 */
	public static List<EmbeddedProperty> getDefaultProfile() {
		Long currentDateTime = System.currentTimeMillis();
		EmbeddedProperty registered = new EmbeddedProperty(
				"registered", 
				currentDateTime
		);
		return Arrays.asList(registered);
	}
	
	/**
	 * Method for crate catalog profile property by request body
	 * @param request Spark request object
	 * @return created profile property
	 * @throws DataException throw if can't be found catalog or
	 * 						 property document
	 */
	public EmbeddedProperty createProperty(Request request) 
		throws DataException {
		ObjectId catalogId = ParamsManager.getCatalogId(request);
		PropertyDTO propertyDTO = PropertyDTO.build(
				request, 
				PropertyDTO.class
		);
		return getRepository().createProperty(catalogId, propertyDTO);
	}
	
	/**
	 * Method for get catalog profile properties list
	 * @param request Spark request object
	 * @return founded catalog profile
	 * @throws DataException throw if can't be found catalog
	 */
	public List<EmbeddedProperty> getProfile(Request request)
		throws DataException {
		ObjectId catalogId = ParamsManager.getCategoryId(request);
		return getRepository().getPropertiesList(catalogId);
	}
	
	/**
	 * Method for get property from catalog profile
	 * @param request Spark request object
	 * @return founded catalog profile property
	 * @throws DataException throw if can't found catalog or
	 * 						 profile property
	 */
	public EmbeddedProperty getProfileProperrtyById(Request request) 
		throws DataException {
		ObjectId catalogId = ParamsManager.getCatalogId(request);
		ObjectId propertyId = ParamsManager.getPropertyId(request);
		return getRepository().getDocumentPropertyByEntityIdAndId(
				propertyId, 
				catalogId
		);
	}

	/**
	 * Method for update profile property for catalog by requst
	 * @param request Spark request object
	 * @return updated catalog profile properties
	 * @throws DataException throw if can't be found catalog or
	 * 						 profile property
	 */
	protected EmbeddedProperty updateeProperty(Request request) 
			throws DataException {
		ObjectId catalogId = ParamsManager.getCatalogId(request);
		ObjectId propertyId = ParamsManager.getPropertyId(request);
		PropertyDTO propertyDTO = PropertyDTO.build(
				request, 
				PropertyDTO.class
		);
		return getRepository().updateProperty(
				propertyId, 
				catalogId, 
				propertyDTO
		);
	}
	
	/**
	 * Method for delete property from catalog profile list
	 * @param request Spark request object
	 * @return actual profile, after delete property
	 * @throws DataException throw if can't be found catalog or property
	 */
	protected List<EmbeddedProperty> deleteProperty(
			Request request
	) throws DataException {
		ObjectId catalogId = ParamsManager.getCatalogId(request);
		ObjectId propertyId = ParamsManager.getPropertyId(request);
		return getRepository().removeProperty(propertyId, catalogId);
	}
}
