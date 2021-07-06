package synthwave.services.v1;

import synthwave.dto.RuleDTO;
import platform.exceptions.AccessException;
import platform.exceptions.DataException;
import synthwave.models.mongodb.standalones.Company;
import synthwave.services.core.CoreCompanyService;
import platform.utils.helpers.Comparator;
import platform.utils.helpers.ParamsManager;
import platform.utils.access.v1.RightManager;
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
    public List<Company> getCompaniesList(
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

    /**
     * Method for create company
     * @param request Spark request object
     * @param companiesRepository repository for companies collection
     * @param rule rule data transfer object
     * @return created company document
     */
    public Company createCompany(
            Request request,
            String right,
            String action
    ) throws AccessException, DataException {
        RuleDTO rule = getRule(request, right, action);
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        boolean hasAccess = (isTrusted)
                ? rule.isMyGlobal()
                : rule.isOtherGlobal();
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

    /**
     * Method for get company by owner id and company id from request params
     * @param request Spark request object
     * @param companiesRepository repository for companies collection
     * @param rule rule data transfer object
     * @return finded document
     */
    public Company getCompanyByOwnerAndId(
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

    /**
     * Method for update company document
     * @param request Spark request object
     * @param companiesRepository repository for companies collection
     * @param rule rule data transfer object
     * @return updated document
     * @throws AccessException throw if has no access to update document
     * @throws DataException throw if can not find company document
     */
    public Company updateCompany(
            Request request,
            String right,
            String action
    ) throws AccessException, DataException {
        RuleDTO rule = getRule(request, right, action);
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        boolean hasAccess = (isTrusted)
                ? rule.isMyPrivate()
                : rule.isOtherPrivate();
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

    /**
     * Method for delete company (deactivate document)
     * @param request Spark request object
     * @param companiesRepository repository for companies collection
     * @param rule rule data transfer object
     * @return deactivated document
     * @throws platform.exceptions.AccessException throw if user has no access to delete
     *                                    company document
     * @throws platform.exceptions.DataException throw if can not get from params company
     *                                  id or user id
     */
    public Company deleteCompany(
            Request request,
            String right,
            String action
    ) throws AccessException, DataException {
        RuleDTO rule = getRule(request, right, action);
        boolean hasAccess = RightManager.chechAccess(request, rule);
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
