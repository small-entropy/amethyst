package synthwave.repositories.mongodb.childs;

import org.bson.types.ObjectId;

import dev.morphia.Datastore;
import platform.exceptions.DataException;
import synthwave.repositories.mongodb.v1.CompaniesRepository;
import synthwave.filters.CompaniesFilter;
import synthwave.models.mongodb.standalones.Company;

/**
 * Abstract class with additional methods for work with companies
 * @author small-entropy
 *
 */
public abstract class BaseChildCompaniesRepository extends CompaniesRepository {
	
	/**
	 * Default constructor for repository
	 * @param datastore Morphia datastore object
	 */
	public BaseChildCompaniesRepository(Datastore datastore) {
		super(datastore);
	}
	
	/**
	 * Method for get full company document
	 * @param companyId company id from request
	 * @return company entity
	 * @throws DataException throw if can not find company
	 */
	public Company getCompanyDocument(ObjectId companyId) throws DataException {
		String[] excludes = new String[] {};
		CompaniesFilter filter = new CompaniesFilter(excludes);
		Company company = findOneById(filter);
		if (company != null) {
			return company;
		} else {
			Error error = new Error("Can not find company by id from request");
			throw new DataException("NotFound", error);
		}
	}
}
