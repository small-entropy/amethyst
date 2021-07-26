package synthwave.repositories.mongodb.v1;

import java.util.List;

import org.bson.types.ObjectId;

import dev.morphia.Datastore;
import synthwave.filters.CompaniesFilter;
import synthwave.models.mongodb.standalones.Company;
import synthwave.repositories.mongodb.base.BasePropertyRepository;

public class CompanyPropertiesRepository 
    extends BasePropertyRepository<Company, CompaniesFilter, CompaniesRepository> {

    public CompanyPropertiesRepository(
        Datastore datastore,
        List<String> blacklist
    ) {
        super("properties", blacklist, new CompaniesRepository(datastore));
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
