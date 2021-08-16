package synthwave.controllers.v1.companies;

import dev.morphia.Datastore;
import core.constants.DefaultRights;
import core.response.transformers.JsonTransformer;
import synthwave.controllers.abstracts.EmbeddedPropertiesController;
import synthwave.constants.CompanyProfileMessages;
import synthwave.filters.CompaniesFilter;
import synthwave.models.morphia.extend.Company;
import synthwave.repositories.morphia.CompaniesRepository;
import synthwave.repositories.morphia.CompanyProfileRepository;
import synthwave.services.v1.companies.CompanyProfileService;

/**
 * Class with routes for work with company profile documents
 * @author small-entropy
 * @version 1
 */
public class CompanyProfileController 
    extends EmbeddedPropertiesController<
        Company,
        CompaniesFilter,
        CompaniesRepository,
        CompanyProfileRepository,
        CompanyProfileService> {
    
    /**
     * Default company profile controller. Create instance by
     * datastore & response transformer
     * @param datastore Morphia datastore object
     * @param transformer response transformer
     */
    public CompanyProfileController(
        Datastore datastore,
        JsonTransformer transformer
    ) {
        super(
            new CompanyProfileService(datastore),
            transformer,
            DefaultRights.COMPANIES.getName(),
            "/:company_id/profile",
            "/:company_id/profile/:property_id",
            false,
            false
        );
    }

    @Override
    protected String getSuccessMessage(String message) {
        return switch (message) {
            case "created" -> CompanyProfileMessages.CREATED.getMessage();
            case "list" -> CompanyProfileMessages.LIST.getMessage();
            case "entity" -> CompanyProfileMessages.ENTITY.getMessage();
            case "update" -> CompanyProfileMessages.UPDATED.getMessage();
            case "delete" -> CompanyProfileMessages.DELETED.getMessage();
            default -> "Unknown success action";
        };
    }
}
