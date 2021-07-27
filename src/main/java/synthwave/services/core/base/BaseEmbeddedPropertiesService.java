package synthwave.services.core.base;

import java.util.List;

import org.bson.types.ObjectId;

import dev.morphia.Datastore;
import platform.exceptions.DataException;
import platform.filters.Filter;
import platform.models.mongodb.morphia.Standalone;
import platform.utils.helpers.ParamsManager;
import spark.Request;
import synthwave.dto.PropertyDTO;
import synthwave.models.mongodb.embeddeds.EmbeddedProperty;
import synthwave.repositories.mongodb.base.BasePropertyRepository;
import synthwave.services.base.BaseDocumentService;

/**
 * Base class for create service for embedded property fields
 * @author small-entropy
 *
 * @param <M> model for database
 * @param <F> filter for data
 * @param <PR> parent repository
 * @param <R>  property repository
 */
public abstract class BaseEmbeddedPropertiesService 
	<M extends Standalone, 
	 F extends Filter, 
	 PR, 
	 R extends BasePropertyRepository<M, F, PR>
     > 
	extends BaseDocumentService<R>
{
	
	/**
	 * Default service constructor. Create service instance by
	 * datastore object & repository instance
	 * @param datastore
	 * @param repository
	 */
	public BaseEmbeddedPropertiesService(
			Datastore datastore,
			R repository
	) {
		super(
				datastore,
				repository,
				new String[] {},
				new String[] {},
				new String[] {}
		);
	}
	
	/**
	 * Method for get entity id from request
	 * @param request Spark request object
	 * @return founded id
	 */
	protected ObjectId getEntityIdFromRequest(Request request) throws DataException {
		return null;
	}
	
	/**
	 * Method for create embedded property
	 * @param request Spark request object
	 * @return created embedded property
	 * @throws DataException throw if can not be found some data
	 */
	public EmbeddedProperty createEmbeddedProperty(Request request) 
		throws DataException {
		ObjectId entityId = getEntityIdFromRequest(request);
		PropertyDTO propertyDTO = PropertyDTO.build(
				request, 
				PropertyDTO.class
		);
		return getRepository().createProperty(entityId, propertyDTO);
	}
	
	/**
	 * Method for get properties list
	 * @param request Spark request object
	 * @return list of embedded properties
	 * @throws DataException throw if can't be found some data
	 */
	public List<EmbeddedProperty> getEmbeddedProperties(Request request) 
		throws DataException {
		ObjectId entityId = getEntityIdFromRequest(request);
		return getRepository().getPropertiesList(entityId);
	}
	
	/**
	 * Method for get embedded property by id
	 * @param request Spark request object
	 * @return founded embedded property
	 * @throws DataException throw if can't be found some data
	 */
	public EmbeddedProperty getEmbeddedProperty(Request request) 
		throws DataException {
		ObjectId entityId = getEntityIdFromRequest(request);
		ObjectId propertyId = ParamsManager.getPropertyId(request);
		return getRepository().getDocumentPropertyByEntityIdAndId( 
				propertyId,
				entityId
		);		
	}
	
	/**
	 * Method for update embedded property
	 * @param request Spark request object
	 * @return updated embedded property
	 * @throws DataException throw if can't be found some data
	 */
	public EmbeddedProperty updateEmbeddedProperty(Request request) 
		throws DataException {
		ObjectId entityId = getEntityIdFromRequest(request);
		ObjectId propertId = ParamsManager.getPropertyId(request);
		PropertyDTO propertyDTO = PropertyDTO.build(
				request, 
				PropertyDTO.class
		);
		return getRepository().updateProperty(
				propertId, 
				entityId, 
				propertyDTO
		);
	}
	
	/**
	 * Method for remove embedded property from list
	 * @param request Spark request object
	 * @return updated property list
	 * @throws DataException throw if can't be found some data
	 */
	public List<EmbeddedProperty> deletEmbeddedProperty(Request request) 
		throws DataException {
		ObjectId entityId = getEntityIdFromRequest(request);
		ObjectId propertyId = ParamsManager.getPropertyId(request);
		return getRepository().removeProperty(propertyId, entityId);
	}
}
