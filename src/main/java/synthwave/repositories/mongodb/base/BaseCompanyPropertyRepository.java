package synthwave.repositories.mongodb.base;

import synthwave.dto.PropertyDTO;
import synthwave.models.mongodb.embeddeds.EmbeddedProperty;
import synthwave.models.mongodb.standalones.Company;
import synthwave.repositories.mongodb.childs.BaseChildCompaniesRepository;
import java.util.List;

import org.bson.types.ObjectId;

import dev.morphia.Datastore;
import platform.exceptions.DataException;
import synthwave.dto.PropertyDTO;

public class BaseCompanyPropertyRepository extends BaseChildCompaniesRepository {
	private final String field;
	private final List<String> blackList;
	
	public BaseCompanyPropertyRepository(
			Datastore datastore,
			String field,
			List<String> blackList
	) {
		super(datastore);
		this.field = field;
		this.blackList = blackList;
	}
	
	public EmbeddedProperty createUserProperty(
			ObjectId companyId,
			PropertyDTO propertyDTO
	) {
		return null;
	}
	
	public List<EmbeddedProperty> getList(ObjectId companyId) throws DataException {
		Company company = getCompanyDocument(companyId);
		List<EmbeddedProperty> properties = getPropertiesByCompany(company);
		if (properties != null) {
			return properties;
		} else {
			Error error = new Error("Can not find company properties list");
			throw new DataException("NotFound", error);
		}
	}
	
	private List<EmbeddedProperty> getPropertiesByCompany(Company company) {
		return switch (field) {
			case "profile" -> company.getProfile();
			case "properties" -> company.getProperties();
			default -> null;
		};
	}
	
	public String getField() {
		return field;
	}
	
	public List<String> getBlackList() {
		return blackList;
	}
}
