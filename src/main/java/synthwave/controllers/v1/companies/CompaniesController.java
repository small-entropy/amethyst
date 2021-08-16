package synthwave.controllers.v1.companies;

import core.constants.DefaultRights;
import engine.dto.RuleDTO;
import core.exceptions.AccessException;
import core.response.transformers.JsonTransformer;
import spark.Request;
import spark.Response;
import synthwave.controllers.abstracts.RESTController;
import synthwave.constants.CompaniesMessages;
import synthwave.models.morphia.extend.Company;
import synthwave.repositories.morphia.CompaniesRepository;
import synthwave.services.v1.companies.CompanyService;
import core.utils.Comparator;
import dev.morphia.Datastore;
/**
 * Class controller for work with companies routes
 * @author small-entropy
 * @version 1
 */
public class CompaniesController 
	extends RESTController<Company, CompaniesRepository, CompanyService> {
	
	@Override
	protected void beforeCreateEntityRoute(Request request, Response response) 
		throws AccessException {
		RuleDTO rule = getService().getRule(
			request, 
			getRight(), 
			getDeleteActionName()
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
			getUpdateActionName()
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
			getDeleteActionName()
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
