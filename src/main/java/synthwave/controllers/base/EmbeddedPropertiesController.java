package synthwave.controllers.base;

import platform.controllers.BaseController;
import platform.exceptions.AccessException;
import platform.exceptions.TokenException;
import platform.models.mongodb.morphia.Standalone;
import platform.utils.responses.SuccessResponse;
import platform.utils.transformers.JsonTransformer;
import spark.Request;
import platform.filters.Filter;
import synthwave.models.mongodb.embeddeds.EmbeddedProperty;
import synthwave.repositories.mongodb.base.BasePropertyRepository;
import synthwave.services.core.base.CRUDEmbeddedPropertyService;
import synthwave.utils.helpers.Comparator;
import static spark.Spark.*;
import java.util.List;

/**
 * Base class for create controllers for embedded properties fields (profile or properties)
 * @author small-entropy
 */
public abstract class EmbeddedPropertiesController
    <M extends Standalone,
     F extends Filter,
     PR,
     R extends BasePropertyRepository<M, F, PR>,
     S extends CRUDEmbeddedPropertyService<M, F, PR, R>
    > 
    extends BaseController<S, JsonTransformer> {

    /** Property with entity url */
    private String entityUrl;
    /** Property with list url */
    private String listUrl;
    /** State of call protected method for create */
    private boolean protectedCreate = true;
    /** State of call protected method for read */
    private boolean protectedRead = true;
    
    /**
     * Constructor for create controller wihtout call state settings.
     * Instance create with default values for protected calls
     * @param service used service
     * @param transformer response transformer
     * @param rights name of right
     * @param listUrl url for list routes
     * @param entityUrl urls for entity routes
     */
    public EmbeddedPropertiesController(
        S service,
        JsonTransformer transformer,
        String rights,
        String listUrl,
        String entityUrl
    ) {
        super(service, transformer, rights);
        this.entityUrl = entityUrl;
        this.listUrl = listUrl;
    }

    /**
     * Constructor for create controller wiht call state settings.
     * Instance create with setted values for protected calls
     * @param service used service
     * @param transformer response transformer
     * @param rights name of right
     * @param listUrl url for list routes
     * @param entityUrl urls for entity routes
     */
    public EmbeddedPropertiesController(
        S service,
        JsonTransformer transformer,
        String rights,
        String listUrl,
        String entityUrl,
        boolean protectedCreate,
        boolean protectedRead
    ) {
        super(service, transformer, rights);
        this.listUrl = listUrl;
        this.entityUrl = entityUrl;
        this.protectedCreate = protectedCreate;
        this.protectedRead = protectedRead;
    }

    /**
     * Getter for entity url
     * @return entity url
     */
    public String getEntityUrl() {
        return entityUrl;
    }

    /**
     * Getter for list url
     * @return list url
     */
    public String getListUrl() {
        return listUrl;
    }

    protected void nextIfHasAccess(
        boolean hasAccess, 
        String message,
        String text
    ) throws AccessException {
        if (!hasAccess) {
            Error error = new Error(text);
            throw new AccessException(message, error);
        }
    }

    protected void accessControllCreate(Request request) throws AccessException, TokenException {
        if (protectedCreate) {
            boolean hasAccess = getService().checkHasAccess(
                request, 
                getRight(), 
                getCreateActionName()
            );
            nextIfHasAccess(
                hasAccess, 
                "CanNotCreate", 
                "Hasn't access to create property"
            );
        } else {
            boolean isTrusted = Comparator.id_fromParam_fromToken(request);
            if (isTrusted) {
                Error error = new Error("Hasn't access to create profile property");
                throw new TokenException("NotEquals", error);
            }
        }    
    }

    protected void accessControllList(Request request) throws AccessException {
        if (protectedRead) {
            boolean hasAccess = getService().checkHasAccess(
                request, 
                getRight(), 
                getReadActionName()
            );
            nextIfHasAccess(
                hasAccess, 
                "CanNotRead", 
                "Hasn't access to get propertie list"
            );
        }
    }


    protected void registerBeforForList() {
        before(getListUrl(), (request, response) -> {
            switch (request.requestMethod()) {
                case "POST":
                    accessControllCreate(request);
                    break;
                case "GET":
                    accessControllList(request);
                    break;
                default: 
                    break;
            }
                       
        });
    }

    protected void accessControllEntity(Request request) throws AccessException {
        if (protectedRead) {
            boolean hasAccess = getService().checkHasAccess(
                request, 
                getRight(), 
                getReadActionName()
            );
            nextIfHasAccess(
                hasAccess, 
                "CanNotRead", 
                "Hasn't access to get property"
            );
        }
    }

    protected void accessControllUpdate(Request request) throws AccessException {
        boolean hasAccess = getService().checkHasAccess(
            request, 
            getRight(), 
            getUpdateActionName()    
        );
        nextIfHasAccess(
            hasAccess, 
            "CanNotUpdate", 
            "Hasn't access to update property"
        );
    }

    protected void accessControllDelete(Request request) throws AccessException {
        boolean hasAccess = getService().checkHasAccess(
            request, 
            getRight(), 
            getDeleteActionName()
        );
        nextIfHasAccess(
            hasAccess, 
            "CanNotDelete", 
            "Hasn't access for delete property from list"
        );
    }

    protected void registerBeforForEntity() {
        before(getEntityUrl(), (request, response) -> {
            switch (request.requestMethod()) {
                case "DELETE":
                    accessControllDelete(request);
                    break;
                case "GET":
                    accessControllDelete(request);
                    break;
                case "PUT":
                    accessControllUpdate(request);
                    break;
                default: 
                    break;
            }
                       
        });
    }

    /**
     * Method with route for create embedded property
     */
    protected void createRoute() {
        post(getListUrl(), (request, response) -> {
            EmbeddedProperty property = getService().create(request);
            return new SuccessResponse<>(
                getSuccessMessage("createed"),
                property
            );
        }, getTransformer());
    }

    /**
     * Method with route for get list embedded properties
     */
    protected void listRoute() {
        get(getListUrl(), (request, response) -> {
            List<EmbeddedProperty> properties = getService().list(request);
            return new SuccessResponse<>(
                getSuccessMessage("list"),
                properties
            );
        }, getTransformer());
    }
    
    /**
     * Method with route for get embedded property by id
     */
    protected void entityRoute() {
        get(getEntityUrl(), (request, response) -> {
            EmbeddedProperty property = getService().entity(request);
            return new SuccessResponse<>(
                getSuccessMessage("entity"),
                property
            );
        }, getTransformer());
    }

    /**
     * Method with route for update embedded property
     */
    protected void updateRoute() {
        put(getEntityUrl(), (request, response) -> {
            EmbeddedProperty property = getService().update(request);
            return new SuccessResponse<>(getSuccessMessage("update"), property);
        }, getTransformer());
    }

    /**
     * Method for delete embedded property
     */
    protected void deleteRoute() {
        delete(getEntityUrl(), (request, response) -> {
            List<EmbeddedProperty> properties = getService().delete(request);
            return new SuccessResponse<>(
                getSuccessMessage("delete"), 
                properties
            );
        }, getTransformer());
    }

    /**
     * Method return messages for success answers
     * @param message name of message
     * @return message for response
     */
    protected String getSuccessMessage(String message) {
        return switch (message) {
            case "created" -> "Successfully created property";
            case "list" -> "Successfully get properties";
            case "entity" -> "Successfully get property";
            case "update" -> "Successfully update property";
            case "delete" -> "Successfully delete property";
            default -> "Successfully";
        };
    }

    /**
     * Method for register custom (additionals) routes
     */
    protected void registerCustomRoutes() {}

    @Override
    public final void register() {
        if (listUrl != null) {
            registerBeforForList();
            createRoute();
            listRoute();
        }
        if (entityUrl != null) {
            registerBeforForEntity();
            entityRoute();
            updateRoute();
            deleteRoute();
        }
        registerCustomRoutes();
    }
}
