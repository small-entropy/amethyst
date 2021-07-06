package synthwave.controllers.v1;

import synthwave.controllers.base.BaseCompaniesController;
import synthwave.models.mongodb.standalones.Company;
import platform.utils.responses.SuccessResponse;
import synthwave.services.v1.CompanyService;
import platform.utils.transformers.JsonTransformer;
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
        CompanyService service = new CompanyService(store);
        
        // Route for get company list
        get("", (req, res) -> {
            List<Company> companies = service.getCompaniesList(
                    req,
                    RIGHT,
                    READ,
                    false
            );
            return new SuccessResponse<>(MSG_LIST, companies);
        }, transformer);
        // Route for get company list by user
        get("/owner/:user_id", (req, res) -> {
            List<Company> companies = service.getCompaniesList(
                    req,
                    RIGHT,
                    READ,
                    true
            );
            return new SuccessResponse<>(MSG_LIST, companies);
        }, transformer);
        // Route for create company
        post("/owner/:user_id", (req, res) -> {
            Company company = service.createCompany(
                    req,
                    RIGHT,
                    CREATE
            );
            return new SuccessResponse<>(MSG_CREATED, company);
        });
        // Reoute for get company
        get("/owner/:user_id/company/:company_id", (req, res) -> {
            Company company = service.getCompanyByOwnerAndId(
                    req, 
                    RIGHT,
                    READ
            );
            return new SuccessResponse<>(MSG_ENTITY, company);
        }, transformer);
        // Route for update company
        put("/owner/:user_id/company/:company_id", (req, res) -> {
            Company company = service.updateCompany(
                    req,
                    RIGHT,
                    UPDATE
            );
            return new SuccessResponse<>(MSG_UPDATED, company);
        });
        // Route for mark to remove company
        delete("/owner/:user_id/compamy/:compamy_id", (req, res) -> {
            Company company = service.deleteCompany(
                    req,
                    RIGHT,
                    UPDATE
            );
            return new SuccessResponse<>(MSG_DELETED, company);
        }, transformer);
    }
}
