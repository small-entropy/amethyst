package Services.core;

import DataTransferObjects.v1.CompanyDTO;
import Exceptions.DataException;
import Filters.CompaniesFilter;
import Models.Standalones.Company;
import Models.Standalones.User;
import Repositories.v1.CompaniesRepository;
import Repositories.v1.UsersRepository;
import Utils.common.QueryManager;
import java.util.List;
import org.bson.types.ObjectId;
import spark.Request;


/**
 * Class with static methods for work with companies data
 * @author small-entropy
 */
public class CoreCompanyService extends AbstractService {
    
    /**
     * Method for get company document (with excludes) by full document
     * @param company company document
     * @param companiesRepository repository for comanies collection
     * @param excludes exlude fields
     * @return company document
     */
    protected static Company getCompanyByDocument(
            Company company,
            CompaniesRepository companiesRepository,
            String[] excludes
    ) {
        ObjectId ownerId = company.getOwner().getId();
        ObjectId companyId = company.getId();
        return getCompanyById(companyId, ownerId, companiesRepository, excludes);
    }
    
    /**
     * Method for get company by id and owner id
     * @param companyId company id
     * @param ownerId owner id
     * @param companiesRepository repository for comanies collection
     * @param excludes exlude fields
     * @return finded document
     */
    protected static Company getCompanyById(
            ObjectId companyId,
            ObjectId ownerId,
            CompaniesRepository companiesRepository,
            String[] excludes
    ) {
        CompaniesFilter filter = new CompaniesFilter(companyId, excludes);
        filter.setOwner(ownerId);
        return companiesRepository.findOneByOwnerAndId(filter);
    }
    
    /**
     * Method for create company document
     * @param ownerId owner id
     * @param request Spark reqeust object
     * @param companiesRepository repository for comanies data
     * @param usersRepository repository for users data
     * @return created company document
     */
    protected static Company createCompany(
            ObjectId ownerId,
            Request request,
            CompaniesRepository companiesRepository,
            UsersRepository usersRepository
    ) { 
       User user = CoreUserService.getUserById(ownerId, usersRepository);
       CompanyDTO companyDTO = CompanyDTO.build(request, CompanyDTO.class);
       return companiesRepository.create(companyDTO);
    }
    
    /**
     * Method for get list of companies documents
     * @param request Spark request object
     * @param companiesRepository repository for comanies collection
     * @param excludes exlude fileds
     * @param ownerId ownerId
     * @return list of comanies
     */
    protected static List<Company> getList(
            Request request, 
            CompaniesRepository companiesRepository, 
            String[] excludes,
            ObjectId ownerId
    ) throws DataException {
        int skip = QueryManager.getSkip(request);
        int limit = QueryManager.getLimit(request);
        CompaniesFilter filter = new CompaniesFilter(skip, limit, excludes);
        if (ownerId != null) {
            filter.setOwner(ownerId);
            return companiesRepository.findAllByOwnerId(filter);
        } else {
            return companiesRepository.findAll(filter);
        }
    }
    
    /**
     * Method for update company document
     * @param ownerId owner id
     * @param companyId company id
     * @param request Spark reqeust obejct
     * @param companiesRepository repository for companies collection
     * @return updated document
     * @throws DataException throw if can not find company document
     */
    protected static Company updateCompany(
            ObjectId ownerId,
            ObjectId companyId,
            Request request,
            CompaniesRepository companiesRepository
    ) throws DataException {
        CompanyDTO companyDTO = CompanyDTO.build(request, CompanyDTO.class);
        CompaniesFilter filter = new CompaniesFilter(companyId, new String[] {});
        filter.setOwner(ownerId);
        return companiesRepository.update(companyDTO, filter);
    }
    
    /**
     * Method for deactivate company document
     * @param userId owner id
     * @param companyId company id
     * @param companiesRepository repository for companies collection
     * @return deactivated document
     * @throws DataException throw if can not find document
     */
    protected static Company deleteCompany(
            ObjectId userId, 
            ObjectId companyId, 
            CompaniesRepository companiesRepository
    ) throws DataException {
        CompaniesFilter filter = new CompaniesFilter(new String[] {});
        filter.setId(companyId);
        filter.setOwner(userId);
        return companiesRepository.deactivated(filter);
    }
}
