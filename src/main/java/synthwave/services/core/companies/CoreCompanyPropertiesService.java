package synthwave.services.core.companies;

import java.util.Arrays;
import java.util.List;

import dev.morphia.Datastore;
import synthwave.filters.CompaniesFilter;
import core.models.morphia.embeddeds.EmbeddedProperty;
import synthwave.models.morphia.extend.Company;
import synthwave.repositories.morphia.CompaniesRepository;
import synthwave.repositories.morphia.CompanyPropertiesRepository;
import synthwave.services.abstracts.CRUDEmbeddedPropertyService;

public abstract class CoreCompanyPropertiesService 
    extends CRUDEmbeddedPropertyService<Company, CompaniesFilter, CompaniesRepository, CompanyPropertiesRepository> {

    /**
     * Default core company properties service constructor. Create
     * instance by database & blacklist
     * @param datastore Modphia datastore object
     * @param blacklist blaclist of fileds
     */
    public CoreCompanyPropertiesService(
        Datastore datastore, 
        List<String> 
    blacklist) {
        super(datastore, new CompanyPropertiesRepository(datastore, blacklist));
    }
    
    /**
     * Static method for get default properties for company
     * @return default list of properties
     */
    public static List<EmbeddedProperty> getDefaultProperties() {
        long currentTime = System.currentTimeMillis();
        EmbeddedProperty created = new EmbeddedProperty(
            "created",
            currentTime
        );
        return Arrays.asList(created);
    }
}
