package synthwave.services.v1.companies;

import platform.dto.RuleDTO;
import platform.exceptions.DataException;
import synthwave.models.mongodb.standalones.Company;
import synthwave.services.core.companies.CoreCompanyService;
import platform.utils.helpers.ParamsManager;
import dev.morphia.Datastore;
import java.util.List;
import org.bson.types.ObjectId;
import spark.Request;

/**
 * Class for work with companies collection
 * @author small-entropy
 * @version 1
 */
public class CompanyService extends CoreCompanyService {

    public CompanyService(Datastore datastore) {
        super(
                datastore,
                new String[] {},
                new String[] { "owner", "version", "status" },
                new String[] { "version", "status" }
        );
    }


    /**
     * Method for get list of companoes
     * @param request Spark request object
     * @param rule rule data transfer object
     * @param byOwner true if list for owner
     * @return list of companies
     * @throws DataException trow if companies list is empty
     */
    public List<Company> getEntitiesList(
            Request request,
            String right,
            String action,
            boolean byOwner
    ) throws DataException {
        RuleDTO rule = getRule(request, right, action);
        String[] excludes = getExcludes(request, rule);
        var userId = (byOwner)
                ? ParamsManager.getUserId(request)
                : null;
        List<Company> companies = getList(request, excludes, userId);
        if (!companies.isEmpty()) {
            return companies;
        } else {
            Error error = new Error("Can not find companies documents");
            throw new DataException("NotFound", error);
        }
    }

    @Override
    public Company createEntity(
            Request request,
            String right,
            String action
    ) throws DataException {
        RuleDTO rule = getRule(request, right, action);
        ObjectId ownerId = ParamsManager.getUserId(request);
        Company company = createCompany(
                ownerId,
                request
        );
        String[] excludes = getExcludes(request, rule);
        return getCompanyByDocument(company, excludes);
    }

    @Override
    public Company getEntityByIdByOwner(
            Request request,
            String right,
            String action
    ) throws DataException {
        RuleDTO rule = getRule(request, right, action);
        String[] excludes = getExcludes(request, rule);
        ObjectId ownerId = ParamsManager.getUserId(request);
        ObjectId companyId = ParamsManager.getCompanyId(request);
        var company = getCompanyById(
                companyId,
                ownerId,
                excludes
        );
        if (company != null) {
            return company;
        } else {
            Error error = new Error("Can not find company document");
            throw new DataException("NotFound", error);
        }
    }

    @Override
    public Company updateEntity(Request request, String right, String action) 
        throws DataException {
        RuleDTO rule = getRule(request, right, action);
        ObjectId userId = ParamsManager.getUserId(request);
        ObjectId companyId = ParamsManager.getCompanyId(request);
        Company company = updateCompany(userId, companyId, request);
        String[] exludes = getExcludes(request, rule);
        return getCompanyByDocument(company, exludes);
    }

    @Override
    public Company deleteEntity(Request request, String right, String action) 
        throws DataException {
        ObjectId userId = ParamsManager.getUserId(request);
        ObjectId companyId = ParamsManager.getCompanyId(request);
        return deleteCompany(userId, companyId);
    }
}
