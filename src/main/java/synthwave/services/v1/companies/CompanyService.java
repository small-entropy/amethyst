package synthwave.services.v1.companies;

import platform.dto.RuleDTO;
import platform.exceptions.AccessException;
import platform.exceptions.DataException;
import synthwave.models.mongodb.standalones.Company;
import synthwave.services.core.companies.CoreCompanyService;
import synthwave.utils.helpers.Comparator;
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
    ) throws AccessException, DataException {
        RuleDTO rule = getRule(request, right, action);
        boolean hasAccess = checkHasGlobalAccess(request, rule);
        if (hasAccess) {
            ObjectId ownerId = ParamsManager.getUserId(request);
            Company company = createCompany(
                    ownerId,
                    request
            );
            String[] excludes = getExcludes(request, rule);
            return getCompanyByDocument(company, excludes);
        } else {
            Error error = new Error("Has no access to create tag");
            throw new AccessException("CanNotCreate", error);
        }
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
    public Company updateEntity(
            Request request,
            String right,
            String action
    ) throws AccessException, DataException {
        RuleDTO rule = getRule(request, right, action);
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        boolean hasAccess = checkHasAccess(rule, isTrusted);
        if (hasAccess) {
            ObjectId userId = ParamsManager.getUserId(request);
            ObjectId companyId = ParamsManager.getCompanyId(request);
            Company company = updateCompany(
                    userId,
                    companyId,
                    request
            );
            String[] exludes = getExcludes(request, rule);
            return getCompanyByDocument(company, exludes);
        } else {
            Error error = new Error("Has no access to update company document");
            throw new AccessException("CanNotUpdate", error);
        }
    }

    @Override
    public Company deleteEntity(
            Request request,
            String right,
            String action
    ) throws AccessException, DataException {
        boolean hasAccess = checkHasGlobalAccess(request, right, action);
        if (hasAccess) {
            ObjectId userId = ParamsManager.getUserId(request);
            ObjectId companyId = ParamsManager.getCompanyId(request);
            return deleteCompany(userId, companyId);
        } else {
            Error error = new Error("Has no access to delete tag document");
            throw new AccessException("CanNotDelete", error);
        }
    }
}
