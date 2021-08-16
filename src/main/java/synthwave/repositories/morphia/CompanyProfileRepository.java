package synthwave.repositories.morphia;

import java.util.List;

import org.bson.types.ObjectId;

import dev.morphia.Datastore;
import synthwave.filters.CompaniesFilter;
import synthwave.models.morphia.extend.Company;
import engine.repositories.morphia.BasePropertyRepository;

/**
 * Class of repository for company profile
 * @author small-entropy
 */
public class CompanyProfileRepository 
    extends BasePropertyRepository<Company, CompaniesFilter, CompaniesRepository> {

    /**
     * Default constructor for company profile repository
     * @param datastore Morphia datastore object
     * @param blacklist blacklist of fields
     */
    public CompanyProfileRepository(
        Datastore datastore,
        List<String> blacklist
    ) {
        super("profile", blacklist, new CompaniesRepository(datastore));
    }

    @Override
    public Company findOneById(ObjectId entityId, String[] excludes) {
        CompaniesFilter filter = new CompaniesFilter(entityId, excludes);
        return this.getRepository().findOneById(filter);
    }

    @Override
    public void save(Company company) {
        this.getRepository().save(company);
    }
}