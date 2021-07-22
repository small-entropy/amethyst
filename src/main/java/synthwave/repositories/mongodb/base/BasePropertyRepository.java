package synthwave.repositories.mongodb.base;

import org.bson.types.ObjectId;

import java.util.Iterator;
import java.util.List;

import platform.exceptions.DataException;
import platform.filters.Filter;
import platform.models.mongodb.morphia.Standalone;
import synthwave.dto.PropertyDTO;
import synthwave.models.mongodb.base.DocumentExtended;
import synthwave.models.mongodb.base.StandaloneExtended;
import synthwave.models.mongodb.embeddeds.EmbeddedProperty;
import synthwave.utils.helpers.Searcher;

public class BasePropertyRepository 
	<M extends Standalone, F extends Filter, R> 
{
	private R repository;
	private String field;
	private List<String> blacklist;
	
	public BasePropertyRepository(
			String field,
			List<String> blacklist,
			R repository
	) {
		this.field = field;
		this.blacklist = blacklist;
		this.repository = repository;
	}
	
	protected F getFilter(ObjectId id, String[] excludes) {
		return null;
	}
	
	/**
	 * Method for check exist field by key in list of properties
	 * @param key
	 * @param entity
	 * @return
	 */
	protected boolean hasProperty(String key, M entity) {
		boolean hasProperty = false;
		List<EmbeddedProperty> properties = getPropertiesFromEntity(entity);
		for(EmbeddedProperty property : properties) {
			if (property.getKey().equals(key)) {
				hasProperty = true;
				break;
			}
		}
		return hasProperty;
	}
	
	/**
	 * Method for get properties from document
	 * @param entity data entity
	 * @return list of properties
	 */
	protected List<EmbeddedProperty> getPropertiesFromEntity(M entity) {
		if (entity instanceof StandaloneExtended) {
			return switch (field) {
				case "profile" -> ((StandaloneExtended) entity).getProfile();
				case "properties" -> ((StandaloneExtended) entity).getProperties();
				default -> null;
			};
		} else if (entity instanceof DocumentExtended) {
			return switch (field) {
				case "profile" -> ((DocumentExtended) entity).getProfile();
				case "properties" -> ((DocumentExtended) entity).getProperties();
				default -> null;
			};
		} else {
			return null;
		}
	}
	
	/**
	 * Method for get list of properties by field from document
	 * @param documentId document id
	 * @return list of properties
	 * @throws DataException throw if can't find properties
	 */
	public List<EmbeddedProperty> getPropertiesList (ObjectId entityId) 
			throws DataException {
		M entity = getEntity(entityId);
		List<EmbeddedProperty> properties = getPropertiesFromEntity(entity);
		if (properties != null) {
			return properties;
		} else {
			Error error = new Error("Can not find document properties list");
			throw new DataException("NotFound", error);
		}
	}
	
	/**
	 * Method for get property from document by property id
	 * @param propertyId property id
	 * @param documentId document id
	 * @return founded property
	 * @throws DataException throw if can not find document, properties or property
	 */
	public EmbeddedProperty getDocumentPropertyByEntityIdAndId(
			ObjectId propertyId,
			ObjectId documentId
	) throws DataException {
		List<EmbeddedProperty> properties = getPropertiesList(documentId);
		EmbeddedProperty property = Searcher.getPropertyByIdFromList(propertyId, properties);
		if (property != null) {
			return property;
		} else {
			Error error = new Error("Can't find property");
			throw new DataException("NotFound", error);
		}
	}
	
	/**
	 * Method for create embedded property in field
	 * @param entityId entity/document id
	 * @param propertyDTO property data transfer object
	 * @return created property
	 * @throws DataException throw if can not find some data
	 */
	public EmbeddedProperty createProperty(
			ObjectId entityId,
			PropertyDTO propertyDTO
	) throws DataException {
		M entity = getEntity(entityId);
		boolean hasProperty = hasProperty(propertyDTO.getKey(), entity);
		if (!hasProperty) {
			EmbeddedProperty property = new EmbeddedProperty(
					propertyDTO.getKey(),
					propertyDTO.getValue()
			);
			List<EmbeddedProperty> properties = getPropertiesFromEntity(entity);
			properties.add(property);
			save(entity);
			return property;
		} else {
			Error error = new Error("Can not create property");
			throw new DataException("CanNotCreate", error);
		}
	}
	
	/**
	 * Method for remove property from document properties list
	 * @param propertyId property id
	 * @param entityId entity id
	 * @return updated properties list
	 * @throws DataException throw if can't find some data
	 */
	public List<EmbeddedProperty> removeProperty(
			ObjectId propertyId,
			ObjectId entityId
	) throws DataException {
		M entity = getEntity(entityId);
		List<EmbeddedProperty> properties = getPropertiesFromEntity(entity);
		Iterator<EmbeddedProperty> iterator = properties.iterator();
		String toCheck = propertyId.toString();
		while(iterator.hasNext()) {
			EmbeddedProperty property = iterator.next();
			if (property.getId().equals(toCheck) &&
					!getBlacklist().contains(property.getKey())) {
				iterator.remove();
			}
		}
		save(entity);
		return getPropertiesFromEntity(entity);
	}
	
	/**
	 * Method for update property
	 * @param propertyId property id
	 * @param entityId entity id
	 * @param propertyDTO property data transfer object
	 * @return updated property
	 * @throws DataException
	 */
	public EmbeddedProperty updateProperty(
			ObjectId propertyId,
			ObjectId entityId,
			PropertyDTO propertyDTO
	) throws DataException {
		M entity = getEntity(entityId);
		List<EmbeddedProperty> properties = getPropertiesFromEntity(entity);
		EmbeddedProperty property = Searcher.getPropertyByIdFromList(propertyId, properties);
		property.setValue(propertyDTO.getValue());
		save(entity);
		return property;
	}
	
	/**
	 * Method for get properties list
	 * @param id document id
	 * @return properties list
	 * @throws DataException throw if can not find entity
	 */
	public M getEntity(ObjectId entityId) throws DataException {
		String[] excludes = new String[] {};
		M entity = findOneById(entityId, excludes);
		if (entity != null) {
			return entity;
		} else {
			Error error = new Error("Can not find document by id");
			throw new DataException("NotFound", error);
		}
	}
	
	/**
	 * Method for get entity by id
	 * @param entityId entity id as UUID
	 * @param excludes excludes fields
	 * @return founded entity
	 */
	public M findOneById(ObjectId entityId, String[] excludes) {
		return null;
	}
	
	public void save(M entity) {}
	
	public R getRepository() {
		return repository;
	}
	
	public void setRepository(R repository) {
		this.repository = repository;
	}
	
	public String getField() {
		return field;
	}
	
	public void setField(String field) {
		this.field = field;
	}
	
	public List<String> getBlacklist() {
		return blacklist;
	}
	
	public void setBlacklist(List<String> blacklist) {
		this.blacklist = blacklist;
	}
}
