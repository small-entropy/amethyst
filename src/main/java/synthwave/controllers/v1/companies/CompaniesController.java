package synthwave.controllers.v1.companies;

import synthwave.controllers.messages.CompaniesMessages;
import synthwave.models.mongodb.standalones.Company;
import synthwave.services.v1.companies.CompanyService;
import platform.constants.DefaultRights;
import platform.controllers.BaseController;
import platform.utils.responses.SuccessResponse;
import platform.utils.transformers.JsonTransformer;
import dev.morphia.Datastore;
import java.util.List;
import static spark.Spark.*;

/**
 * Class controller for work with companies routes
 * @author small-entropy
 * @version 1
 */
public class CompaniesController 
	extends BaseController<CompanyService, JsonTransformer> {
    
	/**
	 * Default companies controller constructor
	 * @param datastore Morphia datastore object
	 * @param transformer response transformer object
	 */
	public CompaniesController(
			Datastore datastore,
			JsonTransformer transformer) {
		super(
				 new CompanyService(datastore),
				 transformer,
				 DefaultRights.COMPANIES.getName()
		);
	}
	
	/**
	 * Method for get company list
	 */
	protected void getCompaniesListRoute() {
        get("", (request, response) -> {
            List<Company> companies = getService().getCompaniesList(
                    request,
                    getRight(),
                    getReadActionName(),
                    false
            );
            return new SuccessResponse<>(
            		CompaniesMessages.LIST.getMessage(), 
            		companies
            );
        }, getTransformer());
	}
	
	/**
	 * Method for get company list by user
	 */
	protected void getCompaniesListByUserRoute() {
		get("/owner/:user_id", (request, ressponse) -> {
            List<Company> companies = getService().getCompaniesList(
                    request,
                    getRight(),
                    getReadActionName(),
                    true
            );
            return new SuccessResponse<>(
            		CompaniesMessages.LIST.getMessage(), 
            		companies
            );
        }, getTransformer());
	}
	
	/**
	 * Method for create company entity
	 */
	protected void createCompaniesRoute() {
		post("/owner/:user_id", (request, response) -> {
            Company company = getService().createCompany(
                    request,
                    getRight(),
                    getCreateActionName()
            );
            return new SuccessResponse<>(
            		CompaniesMessages.CREATED.getMessage(), 
            		company
            );
        }, getTransformer());
	}
	
	/**
	 * Method for get company entity by owner id and company id
	 */
	protected void getCompanyByOwnerAndIdRoute() {
		get("/owner/:user_id/company/:company_id", (request, response) -> {
            Company company = getService().getCompanyByOwnerAndId(
                    request, 
                    getRight(),
                    getReadActionName()
            );
            return new SuccessResponse<>(
            		CompaniesMessages.ENTITY.getMessage(), 
            		company
            );
        }, getTransformer());
	}
	
	/**
	 * Method for update company entity
	 */
	protected void updateCompanyRoute() {
		put("/owner/:user_id/company/:company_id", (request, response) -> {
            Company company = getService().updateCompany(
                    request,
                    getRight(),
                    getUpdateActionName()
            );
            return new SuccessResponse<>(
            		CompaniesMessages.UPDATED.getMessage(), 
            		company
            );
        }, getTransformer());
	}
	
	/**
	 * Method for deactivate company document
	 */
	protected void deleteCompanyRoute() {
		delete("/owner/:user_id/compamy/:compamy_id", (request, response) -> {
            Company company = getService().deleteCompany(
                    request,
                    getRight(),
                    getDeleteActionName()
            );
            return new SuccessResponse<>(
            		CompaniesMessages.DELETED.getMessage(), 
            		company
            );
        }, getTransformer());
	}
	
	
    /**
     * Method for register companies routes
     */
	@Override
    public void register() {
		createCompaniesRoute();
    	getCompaniesListRoute();
    	getCompaniesListByUserRoute();
    	getCompanyByOwnerAndIdRoute();
    	updateCompanyRoute();
    	deleteCompanyRoute();
    }
}
