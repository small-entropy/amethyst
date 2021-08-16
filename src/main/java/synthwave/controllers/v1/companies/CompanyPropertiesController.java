package synthwave.controllers.v1.companies;

import dev.morphia.Datastore;
import core.constants.DefaultRights;
import core.response.transformers.JsonTransformer;
import synthwave.controllers.abstracts.EmbeddedPropertiesController;
import synthwave.constants.CompanyPropertiesMessages;
import synthwave.filters.CompaniesFilter;
import synthwave.models.morphia.extend.Company;
import synthwave.repositories.morphia.CompaniesRepository;
import synthwave.repositories.morphia.CompanyPropertiesRepository;
import synthwave.services.v1.companies.CompanyPropertiesService;

/**
 * Class with routes for work with company properties
 * @author small-entropy
 * @version 1
 */
public class CompanyPropertiesController 
    extends EmbeddedPropertiesController<
    Company,
    CompaniesFilter,
    CompaniesRepository,
    CompanyPropertiesRepository,
    CompanyPropertiesService> {
    
    /**
     * Default constructor for create company properties controller.
     * Create instance by datastore & response transformer
     * @param datastore Morphia datastore object
     * @param transformer response transformer
     */
    public CompanyPropertiesController(
        Datastore datastore,
        JsonTransformer transformer
    ) {
        super(
            new CompanyPropertiesService(datastore),
            transformer,
            DefaultRights.COMPANIES.getName(),
            "/:company_id/properties",
            "/:company_id/properties/:property_id"
        );
    }

    @Override
	protected String getSuccessMessage(String message) {
        return switch (message) {
            case "created" -> CompanyPropertiesMessages.CREATED.getMessage();
            case "list" -> CompanyPropertiesMessages.LIST.getMessage();
            case "entity" -> CompanyPropertiesMessages.ENTITY.getMessage();
            case "update" -> CompanyPropertiesMessages.UPDATED.getMessage();
            case "delete" -> CompanyPropertiesMessages.DELETED.getMessage();
            default -> "Successfully";
        };
    }
}
