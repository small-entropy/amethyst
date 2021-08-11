package synthwave.controllers.v1.companies;

import platform.constants.DefaultActions;
import platform.constants.DefaultRights;
import platform.dto.RuleDTO;
import platform.exceptions.AccessException;
import platform.utils.transformers.JsonTransformer;
import spark.Request;
import spark.Response;
import synthwave.controllers.base.RESTController;
import synthwave.controllers.messages.CompaniesMessages;
import synthwave.models.mongodb.standalones.Company;
import synthwave.repositories.mongodb.v1.CompaniesRepository;
import synthwave.services.v1.companies.CompanyService;
import synthwave.utils.helpers.Comparator;
import dev.morphia.Datastore;
/**
 * Class controller for work with companies routes
 * @author small-entropy
 * @version 1
 */
public class CompaniesController 
	extends RESTController<Company, CompaniesRepository, CompanyService> {
	
	@Override
	protected void beforeCreateEntity(Request request, Response response) 
		throws AccessException {
		RuleDTO rule = getService().getRule(
			request, 
			getRight(), 
			DefaultActions.CREATE.getName()
		);
		boolean hasAccess = getService().checkHasGlobalAccess(request, rule);
		nextIfHasAccess(hasAccess, "CanNotCreate", "Has no access to create tag");
	}

	@Override
	protected void beforeUpdateRoute(Request request, Response response) 
		throws AccessException {
		RuleDTO rule = getService().getRule(
			request, 
			getRight(), 
			DefaultActions.UPDATE.getName()
		);
		boolean isTrusted = Comparator.id_fromParam_fromToken(request);
		boolean hasAccess = getService().checkHasAccess(rule, isTrusted);
		nextIfHasAccess(
			hasAccess, 
			"CanNotUpdate", 
			"Has no access to update company document"
		);
	}

	@Override
	protected void beforeDeleteRoute(Request request, Response response)
		throws AccessException {
		boolean hasAccess = getService().checkHasGlobalAccess(
			request, 
			getRight(),
			DefaultActions.DELETE.getName() 
		);
		nextIfHasAccess(
			hasAccess, 
			"CanNotDelete", 
			"Has no access to delete tag document"
		);
	}
    
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
