package synthwave.services.core;

import synthwave.dto.CompanyDTO;
import platform.exceptions.DataException;
import synthwave.filters.CompaniesFilter;
import synthwave.models.mongodb.standalones.Company;
import synthwave.models.mongodb.standalones.User;
import synthwave.repositories.mongodb.v1.CompaniesRepository;
import platform.services.BaseDocumentService;
import platform.utils.helpers.QueryManager;
import dev.morphia.Datastore;
import java.util.List;
import org.bson.types.ObjectId;
import spark.Request;


/**
 * Class with static methods for work with companies data
 * @author small-entropy
 */
public abstract class CoreCompanyService 
        extends BaseDocumentService<CompaniesRepository> {
    
    public CoreCompanyService(
            Datastore datastore,
            String[] globalExcludes,
            String[] publicExclides,
            String[] privateExcludes
    ) {
        super(
                datastore,
                new CompaniesRepository(datastore),
                globalExcludes,
                publicExclides,
                privateExcludes
        );
    }
    
    /**
     * Method for get company document (with excludes) by full document
     * @param company company document
     * @param excludes exlude fields
     * @return company document
     */
    protected Company getCompanyByDocument(
            Company company,
            String[] excludes
    ) {
        ObjectId ownerId = company.getOwner().getId();
        ObjectId companyId = company.getId();
        return getCompanyById(companyId, ownerId, excludes);
    }
    
    /**
     * Method for get company by id and owner id
     * @param companyId company id
     * @param ownerId owner id
     * @param companiesRepository repository for comanies collection
     * @param excludes exlude fields
     * @return finded document
     */
    protected Company getCompanyById(
            ObjectId companyId,
            ObjectId ownerId,
            String[] excludes
    ) {
        CompaniesFilter filter = new CompaniesFilter(companyId, excludes);
        filter.setOwner(ownerId);
        return getRepository().findOneByOwnerAndId(filter);
    }
    
    /**
     * Method for create company document
     * @param ownerId owner id
     * @param request Spark reqeust object
     * @return created company document
     */
    protected Company createCompany(
            ObjectId ownerId,
            Request request
    ) { 
       User user = getUserById(ownerId);
       CompanyDTO companyDTO = CompanyDTO.build(request, CompanyDTO.class);
       companyDTO.setOwner(user);
       return getRepository().create(companyDTO);
    }
    
    /**
     * Method for get list of companies documents
     * @param request Spark request object
     * @param excludes exlude fileds
     * @param ownerId ownerId
     * @return list of comanies
     */
    protected List<Company> getList(
            Request request, 
            String[] excludes,
            ObjectId ownerId
    ) throws DataException {
        int skip = QueryManager.getSkip(request);
        int limit = QueryManager.getLimit(request);
        CompaniesFilter filter = new CompaniesFilter(skip, limit, excludes);
        if (ownerId != null) {
            filter.setOwner(ownerId);
            return getRepository().findAllByOwnerId(filter);
        } else {
            return getRepository().findAll(filter);
        }
    }
    
    /**
     * Method for update company document
     * @param ownerId owner id
     * @param companyId company id
     * @param request Spark reqeust obejct
     * @return updated document
     * @throws DataException throw if can not find company document
     */
    protected Company updateCompany(
            ObjectId ownerId,
            ObjectId companyId,
            Request request
    ) throws DataException {
        CompanyDTO companyDTO = CompanyDTO.build(request, CompanyDTO.class);
        CompaniesFilter filter = new CompaniesFilter(companyId, new String[] {});
        filter.setOwner(ownerId);
        return getRepository().update(companyDTO, filter);
    }
    
    /**
     * Method for deactivate company document
     * @param userId owner id
     * @param companyId company id
     * @return deactivated document
     * @throws DataException throw if can not find document
     */
    protected Company deleteCompany(
            ObjectId userId, 
            ObjectId companyId
    ) throws DataException {
        CompaniesFilter filter = new CompaniesFilter(new String[] {});
        filter.setId(companyId);
        filter.setOwner(userId);
        return getRepository().deactivate(filter);
    }
}
