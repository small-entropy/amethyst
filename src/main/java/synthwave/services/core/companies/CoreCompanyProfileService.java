package synthwave.services.core.companies;

import java.util.Arrays;
import java.util.List;

import org.bson.types.ObjectId;

import dev.morphia.Datastore;
import core.exceptions.DataException;
import core.utils.ParamsManager;
import spark.Request;
import synthwave.filters.CompaniesFilter;
import core.models.morphia.embeddeds.EmbeddedProperty;
import synthwave.models.morphia.extend.Company;
import synthwave.repositories.morphia.CompaniesRepository;
import synthwave.repositories.morphia.CompanyProfileRepository;
import synthwave.services.abstracts.CRUDEmbeddedPropertyService;

/**
 * Base class for work with company profile property list
 * @author small-entropy
 */
public abstract class CoreCompanyProfileService 
    extends CRUDEmbeddedPropertyService<Company, CompaniesFilter, CompaniesRepository, CompanyProfileRepository> {

    /**
     * Default core company profile service constructor. Create
     * instance by database & blacklist
     * @param datastore Modphia datastore object
     * @param blacklist blaclist of fileds
     */
    public CoreCompanyProfileService(
        Datastore datastore,
        List<String> blacklist
    ) {
        super(datastore, new CompanyProfileRepository(datastore, blacklist));
    }
    
    @Override
    protected ObjectId getEntityIdFromRequest(Request request) throws DataException {
        return ParamsManager.getCompanyId(request);
    }

    /**
     * Static method for get default company profile
     * @return default company profile
     */
    public static List<EmbeddedProperty> getDefaultProfile() {
        long currentDateAndTime = System.currentTimeMillis();
        EmbeddedProperty created = new EmbeddedProperty(
            "created", 
            currentDateAndTime
        );
        return Arrays.asList(created);
    }
}
