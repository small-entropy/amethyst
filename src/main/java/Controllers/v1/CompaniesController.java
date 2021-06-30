package Controllers.v1;

import Controllers.base.BaseCompaniesController;
import DataTransferObjects.v1.RuleDTO;
import Models.Standalones.Company;
import Repositories.v1.CompaniesRepository;
import Repositories.v1.UsersRepository;
import Utils.responses.SuccessResponse;
import Services.v1.CompanyService;
import Utils.transformers.JsonTransformer;
import dev.morphia.Datastore;
import java.util.List;
import static spark.Spark.*;

/**
 * Class controller for work with companies routes
 * @author small-entropy
 */
public class CompaniesController extends BaseCompaniesController {
    
    /**
     * Static method for inialize companies routes
     * @param store Morphis datastore object
     * @param transformer converter to JSON
     */
    public static void routes(Datastore store, JsonTransformer transformer) {
        CompaniesRepository companiesRepository = new CompaniesRepository(store);
        UsersRepository usersRepository = new UsersRepository(store);
        
        // Route for get company list
        get("", (req, res) -> {
            RuleDTO rule = getRule(req, usersRepository, RIGHT, READ);
            List<Company> companies = CompanyService.getCompaniesList(
                    req,
                    companiesRepository,
                    rule,
                    false
            );
            return new SuccessResponse<>(MSG_LIST, companies);
        }, transformer);
        // Route for get company list by user
        get("/owner/:user_id", (req, res) -> {
            RuleDTO rule = getRule(req, usersRepository, RIGHT, READ);
            List<Company> companies = CompanyService.getCompaniesList(
                    req,
                    companiesRepository,
                    rule,
                    true
            );
            return new SuccessResponse<>(MSG_LIST, companies);
        }, transformer);
        // Route for create company
        post("/owner/:user_id", (req, res) -> {
            RuleDTO rule = getRule(req, usersRepository, RIGHT, CREATE);
            Company company = CompanyService.createCompany(
                    req,
                    companiesRepository,
                    usersRepository,
                    rule
            );
            return new SuccessResponse<>(MSG_CREATED, company);
        });
        // Reoute for get company
        get("/owner/:user_id/company/:company_id", (req, res) -> {
            RuleDTO rule = getRule(req, usersRepository, RIGHT, READ);
            Company company = CompanyService.getCompanyByOwnerAndId(
                    req, 
                    companiesRepository,
                    rule
            );
            return new SuccessResponse<>(MSG_ENTITY, company);
        }, transformer);
        // Route for update company
        put("/owner/:user_id/company/:company_id", (req, res) -> {
            RuleDTO rule = getRule(req, usersRepository, RIGHT, UPDATE);
            Company company = CompanyService.updateCompany(
                    req,
                    companiesRepository,
                    rule
            );
            return new SuccessResponse<>(MSG_UPDATED, company);
        });
        // Route for mark to remove company
        delete("/owner/:user_id/compamy/:compamy_id", (req, res) -> {
            RuleDTO rule = getRule(req, usersRepository, RIGHT, UPDATE);
            Company company = CompanyService.deleteCompany(
                    req,
                    companiesRepository,
                    rule
            );
            return new SuccessResponse<>(MSG_DELETED, company);
        }, transformer);
    }
}
