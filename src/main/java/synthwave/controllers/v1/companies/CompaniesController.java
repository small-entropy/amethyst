package synthwave.controllers.v1.companies;

import platform.constants.DefaultRights;
import platform.utils.transformers.JsonTransformer;

import synthwave.controllers.base.RESTController;
import synthwave.controllers.messages.CompaniesMessages;
import synthwave.models.mongodb.standalones.Company;
import synthwave.repositories.mongodb.v1.CompaniesRepository;
import synthwave.services.v1.companies.CompanyService;

import dev.morphia.Datastore;
/**
 * Class controller for work with companies routes
 * @author small-entropy
 * @version 1
 */
public class CompaniesController 
	extends RESTController<Company, CompaniesRepository, CompanyService> {
    
	/**
	 * Default companies controller constructor
	 * @param datastore Morphia datastore object
	 * @param transformer response transformer object
	 */
	public CompaniesController(
			Datastore datastore,
			JsonTransformer transformer
	) {
		super(
				new CompanyService(datastore),
				transformer,
				DefaultRights.COMPANIES.getName(),
				null,
				"",
				"/owner/:user_id/company/:company_id",
				"/owner/:user_id",
				true,
				true,
				true,
				true,
				true,
				CompaniesMessages.CREATED.getMessage(),
				CompaniesMessages.LIST.getMessage(),
				CompaniesMessages.ENTITY.getMessage(),
				CompaniesMessages.UPDATED.getMessage(),
				CompaniesMessages.DELETED.getMessage()
		);
	}
}
