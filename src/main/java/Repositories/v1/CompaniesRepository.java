package Repositories.v1;

import DataTransferObjects.v1.CompanyDTO;
import Exceptions.DataException;
import Filters.CompaniesFilter;
import Models.Embeddeds.EmbeddedOwner;
import Models.Standalones.Company;
import Repositories.Core.MorphiaRepository;
import dev.morphia.Datastore;

/**
 * Class repository for companies collection
 * @author small-entropy
 */
public class CompaniesRepository
    extends MorphiaRepository<Company, CompaniesFilter, CompanyDTO>{

    /**
     * Constructor of repository for companies collection
     * @param datastore Morphia datastore object 
     */
    public CompaniesRepository(Datastore datastore) {
        super(datastore, Company.class);
    }
    
    /**
     * Method for create company document
     * @param companyDTO company data transfer object
     * @return created company
     */
    @Override
    public Company create(CompanyDTO companyDTO) {
        // Create embedded owner document
        EmbeddedOwner owner = new EmbeddedOwner(
                companyDTO.getOwner().getId(),
                companyDTO.getOwner().getUsername()
        );
        // Create company document
        Company company = new Company(
                companyDTO.getName(),
                companyDTO.getTitle(),
                companyDTO.getDescription(),
                owner,
                companyDTO.getProfile()
        );
        // Save company document in database
        save(company);
        // Return company document
        return company;
    }
    
    /**
     * Method for update company document
     * @param companyDTO company data transfer object
     * @param filter filter object
     * @return updated company document
     * @throws DataException 
     */
    public Company update(CompanyDTO companyDTO, CompaniesFilter filter)
            throws DataException {
        Company company = findOneByOwnerAndId(filter);
        var title = companyDTO.getDescription();
        var description = companyDTO.getTitle();
        if (company != null) {
            if (title != null && (company.getTitle() == null
                    || !company.getTitle().equals(title))) {
                company.setTitle(title);
            }
            
            if (description != null && (company.getDescription() == null
                    || !company.getDescription().equals(description))) {
                company.setDescription(description);
            }
            
            save(company);
            return company;
        } else {
            Error error = new Error("Can not find company by request params");
            throw new DataException("NotFound", error);
        }
    }
}