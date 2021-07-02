package Services.v1;

import DataTransferObjects.v1.RuleDTO;
import Exceptions.AccessException;
import Exceptions.DataException;
import Models.Standalones.Company;
import Repositories.v1.CompaniesRepository;
import Repositories.v1.UsersRepository;
import Services.core.CoreCompanyService;
import Utils.common.Comparator;
import Utils.common.ParamsManager;
import Utils.v1.RightManager;
import java.util.List;
import org.bson.types.ObjectId;
import spark.Request;

/**
 * Class for work with companies collection
 * @author small-entropy
 * @version 1
 */
public class CompanyService extends CoreCompanyService {
    
    /** Public exludes fields */
    private static final String[] PUBLIC_EXCLUDES = {
        "owner",
        "version",
        "status"
    }; 
    
    /* Private excludes fields */
    private static final String[] PRIVATE_EXCLUDES = {
        "version",
        "status"
    };

    /**
     * Method for get list of companoes
     * @param request Spark request object
     * @param companiesRepository repository for companies collection
     * @param rule rule data transfer object
     * @param byOwner true if list for owner
     * @return list of companies
     * @throws DataException trow if companies list is empty
     */
    public static List<Company> getCompaniesList(
            Request request, 
            CompaniesRepository companiesRepository, 
            RuleDTO rule,
            boolean byOwner
    ) throws DataException {
        String[] excludes = getExcludes(
                request, 
                rule, 
                PUBLIC_EXCLUDES, 
                PRIVATE_EXCLUDES
        );
        var userId = (byOwner)
                ? ParamsManager.getUserId(request)
                : null;
        List<Company> companies = getList(
                request, 
                companiesRepository, 
                excludes,
                userId
        );
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
    public static Company createCompany(
            Request request, 
            CompaniesRepository companiesRepository,
            UsersRepository usersRepository,
            RuleDTO rule
    ) throws AccessException, DataException {
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        boolean hasAccess = (isTrusted)
                ? rule.isMyGlobal()
                : rule.isOtherGlobal();
        if (hasAccess) {
            ObjectId ownerId = ParamsManager.getUserId(request);
            Company company = createCompany(
                    ownerId,
                    request,
                    companiesRepository,
                    usersRepository
            );
            String[] excludes = getExcludes(
                    request, 
                    rule, 
                    PUBLIC_EXCLUDES, 
                    PRIVATE_EXCLUDES
            );
            return getCompanyByDocument(company, companiesRepository, excludes);
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
    public static Company getCompanyByOwnerAndId(
            Request request, 
            CompaniesRepository companiesRepository, 
            RuleDTO rule
    ) throws DataException {
        String[] excludes = getExcludes(
                request, 
                rule, 
                PUBLIC_EXCLUDES, 
                PRIVATE_EXCLUDES
        );
        ObjectId ownerId = ParamsManager.getUserId(request);
        ObjectId companyId = ParamsManager.getCompanyId(request);
        var company = getCompanyById(
                companyId,
                ownerId,
                companiesRepository, 
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
    public static Company updateCompany(
            Request request, 
            CompaniesRepository companiesRepository, 
            RuleDTO rule
    ) throws AccessException, DataException {
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
                    request,
                    companiesRepository
            );
            String[] exludes = getExcludes(
                    request, 
                    rule, 
                    PUBLIC_EXCLUDES, 
                    PRIVATE_EXCLUDES
            );
            return getCompanyByDocument(company, companiesRepository, exludes);
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
     * @throws Exceptions.AccessException throw if user has no access to delete
     *                                    company document
     * @throws Exceptions.DataException throw if can not get from params company
     *                                  id or user id
     */
    public static Company deleteCompany(
            Request request, 
            CompaniesRepository companiesRepository, 
            RuleDTO rule
    ) throws AccessException, DataException {
        boolean hasAccess = RightManager.chechAccess(request, rule);
        if (hasAccess) {
            ObjectId userId = ParamsManager.getUserId(request);
            ObjectId companyId = ParamsManager.getCompanyId(request);
            return deleteCompany(userId, companyId, companiesRepository);
        } else {
            Error error = new Error("Has no access to delete tag document");
            throw new AccessException("CanNotDelete", error);
        }
    }
    
}
