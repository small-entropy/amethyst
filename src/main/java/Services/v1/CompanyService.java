package Services.v1;

import DataTransferObjects.v1.RuleDTO;
import Models.Standalones.Company;
import Repositories.v1.CompaniesRepository;
import java.util.List;
import spark.Request;

/**
 * Class for work with companies collection
 * @author small-entropy
 * @version 1
 */
public class CompanyService {

    /**
     * Method for get list of companoes
     * @param request Spark request object
     * @param companiesRepository repository for companies collection
     * @param rule rule data transfer object
     * @return list of companies
     */
    public static List<Company> getCompaniesList(
            Request request, 
            CompaniesRepository companiesRepository, 
            RuleDTO rule
    ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Method for get list of companies by user (owner) id
     * @param request Spark request object
     * @param companiesRepository repository for companies collection
     * @param rule rule data transfer object
     * @return list of companies
     */
    public static List<Company> getCompaniesListByUser(
            Request request, 
            CompaniesRepository companiesRepository, 
            RuleDTO rule
    ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Method for create company
     * @param request Spark request object
     * @param companiesRepository repository for companies collection
     * @param rule rule data transfer object
     * @return created company document
     */
    public static Company createCompany(
            Request request, 
            CompaniesRepository companiesRepository, 
            RuleDTO rule
    ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Method for get company by owner id and company id from request params
     * @param request Spark request object
     * @param companiesRepository repository for companies collection
     * @param rule rule data transfer object
     * @return finded document
     */
    public static Company getCompanyByOwnerAndId(
            Request request, 
            CompaniesRepository companiesRepository, 
            RuleDTO rule
    ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Method for update company document
     * @param request Spark request object
     * @param companiesRepository repository for companies collection
     * @param rule rule data transfer object
     * @return updated document
     */
    public static Company updateCompany(
            Request request, 
            CompaniesRepository companiesRepository, 
            RuleDTO rule
    ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Method for delete company (deactivate document)
     * @param request Spark request object
     * @param companiesRepository repository for companies collection
     * @param rule rule data transfer object
     * @return deactivated document
     */
    public static Company deleteCompany(
            Request request, 
            CompaniesRepository companiesRepository, 
            RuleDTO rule
    ) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
