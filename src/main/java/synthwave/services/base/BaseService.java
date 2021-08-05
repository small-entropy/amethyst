package synthwave.services.base;

import java.util.List;

import platform.dto.RuleDTO;
import platform.exceptions.AccessException;
import platform.exceptions.DataException;
import platform.exceptions.ServerException;
import platform.exceptions.TokenException;
import platform.models.mongodb.morphia.Standalone;
import synthwave.utils.access.RightManager;
import spark.Request;

/**
 * Base class to create service for server service
 * @author small-entropy
 */
public abstract class BaseService <M extends Standalone, R>  {
	
	/** Service repository */
	R repository;
    
    /** Exclude fields for public access */
	private String[] publicExcludes = new String[] {};
    
	/** Exclude fields for private access */
	private String[] privateExcludes = new String[] {};
    
	/** Exclude fields for global access */
	private String[] globalExcludes = new String[] {};
    
    /**
     * Default constructor for create service object
     * @param repository instance of repository object
     */
    public BaseService(R repository) {
        this.repository = repository;
    }
    
    /**
     * Constructor for base service object. Create instance by
     * repository & level access (public, private, global)
     * @param repository instance of repository
     * @param globalExcludes exclude fields for global access
     * @param publicExcludes exclude fields for public access
     * @param privateExcludes exclude fields for private access
     */
    public BaseService(
            R repository,
            String[] globalExcludes,
            String[] publicExcludes,
            String[] privateExcludes
        ) {
        this.repository = repository;
        this.publicExcludes = publicExcludes;
        this.privateExcludes = privateExcludes;
        this.globalExcludes = globalExcludes;
    }

    /**
     * Getter for repository property
     * @return current value of repository
     */
    public R getRepository() {
        return repository;
    }

    /**
     * Setter for repository property
     * @param repository new value for repository property
     */
    public void setRepository(R repository) {
        this.repository = repository;
    }

    /**
     * Getter for public exclude fields
     * @return current value of public exclude fields
     */
    public String[] getPublicExcludes() {
        return publicExcludes;
    }

    /**
     * Setter public exclude fields
     * @param publicExcludes new value of public exclude fields
     */
    public void setPublicExcludes(String[] publicExcludes) {
        this.publicExcludes = publicExcludes;
    }

    /**
     * Getter private exclude fields
     * @return current value of private exclude fields
     */
    public String[] getPrivateExcludes() {
        return privateExcludes;
    }

    /**
     * Setter for private exclude fields
     * @param privateExcludes new value for private exclude fields
     */
    public void setPrivateExcludes(String[] privateExcludes) {
        this.privateExcludes = privateExcludes;
    }

    /**
     * Getter global exclude fields
     * @return current value of global exclude fields
     */
    public String[] getGlobalExcludes() {
        return globalExcludes;
    }
    
    /**
     * Setter for global exclude fields
     * @param globalExcludes new value for global exclude fields
     */
    public void setGlobalExcludes(String[] globalExcludes) {
        this.globalExcludes = globalExcludes;
    }
    
    /**
     * Method for get exclude fields by request object & rule data transfer
     * @param request Spark request object
     * @param rule rule data transfer object
     * @return exclude fields by access level
     */
    protected String[] getExcludes(
            Request request,
            RuleDTO rule
    ) {
        return RightManager.getExcludes(
                request, 
                rule,
                globalExcludes,
                publicExcludes, 
                privateExcludes
        );
    }
    
    /**
     * Method for get exclude fields 
     * @param isTrusted state of trust request
     * @param rule rule data transfer object
     * @return exclude fields
     */
    protected String[] getExcludes(
            boolean isTrusted,
            RuleDTO rule
    ) {
        return RightManager.getExludesByRule(
                isTrusted, 
                rule,
                globalExcludes,
                publicExcludes, 
                privateExcludes
        );
    }
    
    /**
     * Getter for exclude fields
     * @param rule rule data transfer object
     * @param other flag for check other rights
     * @return exclude fields
     */
    protected String[] getExcludes(
            RuleDTO rule,
            boolean other
    ) {
        if (rule != null) {
            var state = (other) ? rule.getMyAccess() : rule.getOtherAccess();
            return switch(state) {
                case "Full" -> globalExcludes;
                case "PublicAndPrivate" -> privateExcludes;
                default -> publicExcludes;
            };
        } else {
            return publicExcludes;
        }
    }

     /**
     * Method for create entity without access check
     * @param request Spark request object
     * @return created entity
     * @throws AccessException throw if user hasn't access to this method
     * @throws TokenException throw if token not sent or user from url & request not equals
     * @throws ServerException throw if some error in server logic
     * @throws DataException throw if some error with data
     */
    public M createEntity(
        Request request
    ) throws AccessException, TokenException, ServerException, DataException {
        Error error = new Error("Method for create entity not implemented");
        throw new ServerException("NotImplemented", error);
    }

    /**
     * Method for create entity with access check
     * @param request Spark request object
     * @param right name of right
     * @param action name of action
     * @return created entity
     * @throws AccessException throw if user hasn't access to this method
     * @throws TokenException throw if token not sent or user from url & request not equals
     * @throws ServerException throw if some error in server logic
     * @throws DataException throw if some error with data
     */
    public M createEntity(
        Request request, 
        String right, 
        String action
    ) throws AccessException, TokenException, ServerException, DataException {
        Error error = new Error("Method for create entity not implemented");
        throw new ServerException("NotImplemented", error);
    }

    /**
     * Method for update entity withoud access check
     * @param request Spark request object
     * @return updated entity
     * @throws AccessException throw if user hasn't access to this method
     * @throws TokenException throw if token not sent or user from url & request not equals
     * @throws ServerException throw if some error in server logic
     * @throws DataException throw if some error with data
     */
    public M updateEntity(
        Request request
    ) throws AccessException, TokenException, ServerException, DataException {
        Error error = new Error("Method for update entity not implemented");
        throw new ServerException("NotImplemented", error);
    }

     /**
     * Method for update entity with access check
     * @param request Spark request object
     * @param right name of right
     * @param action name of action
     * @return updated entity
     * @throws AccessException throw if user hasn't access to this method
     * @throws TokenException throw if token not sent or user from url & request not equals
     * @throws ServerException throw if some error in server logic
     * @throws DataException throw if some error with data
     */
    public M updateEntity(
        Request request, 
        String right, 
        String action
    ) throws AccessException, TokenException, ServerException, DataException {
        Error error = new Error("Method for update entity not implemented");
        throw new ServerException("NotImplemented", error);
    }

    /**
     * Method for delete entity by id without access check
     * @param request Spark request object
     * @return deleted entity
     * @throws AccessException throw if user hasn't access to this method
     * @throws TokenException throw if token not sent or user from url & request not equals
     * @throws ServerException throw if some error in server logic
     * @throws DataException throw if some error with data
     */
    public M deleteEntity(
        Request request
    ) throws AccessException, TokenException, ServerException, DataException {
        Error error = new Error("Method for delete entity not implemented");
        throw new ServerException("NotImplemented", error);
    }

    /**
     * Method for delete entity by id with access check
     * @param request Spark request object
     * @param right name of right
     * @param action name of action
     * @return deleted entity
     * @throws AccessException throw if user hasn't access to this method
     * @throws TokenException throw if token not sent or user from url & request not equals
     * @throws ServerException throw if some error in server logic
     * @throws DataException throw if some error with data
     */
    public M deleteEntity(
        Request request, 
        String right, 
        String action
    ) throws AccessException, TokenException, ServerException, DataException {
        Error error = new Error("Method for delete entity not implemented");
        throw new ServerException("NotImplemented", error);
    }

    /**
     * Method for get entity by id without access check
     * @param request Spark request object
     * @return founded entity
     * @throws AccessException throw if user hasn't access to this method
     * @throws TokenException throw if token not sent or user from url & request not equals
     * @throws ServerException throw if some error in server logic
     * @throws DataException throw if some error with data
     */
    public M getEntityById(
        Request request
    ) throws AccessException, TokenException, ServerException, DataException {
        Error error = new Error("Method for get entity by id not implemented");
        throw new ServerException("NotImplemented", error);
    }

    /**
     * Method for get entity by id with access check
     * @param request Spark request object
     * @param right name of right
     * @param action name of action
     * @return founded entity
     * @throws AccessException throw if user hasn't access to this method
     * @throws TokenException throw if token not sent or user from url & request not equals
     * @throws ServerException throw if some error in server logic
     * @throws DataException throw if some error with data
     */
    public M getEntityById(
        Request request, 
        String right, 
        String action
    ) throws AccessException, TokenException, ServerException, DataException {
        Error error = new Error("Method for get entity by id not implemented");
        throw new ServerException("NotImplemented", error);
    }

    /**
     * Method for get entity for owner with access check
     * @param request Spark request object
     * @param right name of right
     * @param action name of action
     * @return founded entity
     * @throws AccessException throw if user hasn't access to this method
     * @throws TokenException throw if token not sent or user from url & request not equals
     * @throws ServerException throw if some error in server logic
     * @throws DataException throw if some error with data
     */
    public M getEntityByIdByOwner(
        Request request, 
        String right, 
        String action
    ) throws AccessException, TokenException, ServerException, DataException {
        Error error = new Error("Method for get entity by id not implemented");
        throw new ServerException("NotImplemented", error);
    }

    /**
     * Method for get entity for owner without access check
     * @param request Spark request object
     * @return founded entity
     * @throws AccessException throw if user hasn't access to this method
     * @throws TokenException throw if token not sent or user from url & request not equals
     * @throws ServerException throw if some error in server logic
     * @throws DataException throw if some error with data
     */
    public M getEntityByIdByOwner(
        Request request
    ) throws AccessException, TokenException, ServerException, DataException {
        Error error = new Error("Method for get entity by id not implemented");
        throw new ServerException("NotImplemented", error);
    }

    /**
     * Method for get entities list for owner with access check
     * @param request Spark request object
     * @param right name of right
     * @param action name of action
     * @return entities list
     * @throws AccessException throw if user hasn't access to this method
     * @throws TokenException throw if token not sent or user from url & request not equals
     * @throws ServerException throw if some error in server logic
     * @throws DataException throw if some error with data
     */
    public List<M> getEntitiesListByOwner(
        Request request, 
        String right, 
        String action
    ) throws AccessException, TokenException, ServerException, DataException {
        Error error = new Error("Method for get entitites list by owner not implemented");
        throw new ServerException("NotImplemented", error);
    }

    /**
     * Method for get entities list for owner without access check
     * @param request Spark request object
     * @return entities list
     * @throws AccessException throw if user hasn't access to this method
     * @throws TokenException throw if token not sent or user from url & request not equals
     * @throws ServerException throw if some error in server logic
     * @throws DataException throw if some error with data
     */
    public List<M> getEntitiesListByOwner(
        Request request
    ) throws AccessException, TokenException, ServerException, DataException {
        Error error = new Error("Method for get entitites list by owner not implemented");
        throw new ServerException("NotImplemented", error);
    }  

    /**
     * Method for get entities list with access check
     * @param request Spark request object
     * @param right name of right
     * @param action name of action
     * @return entities list
     * @throws AccessException throw if user hasn't access to this method
     * @throws TokenException throw if token not sent or user from url & request not equals
     * @throws ServerException throw if some error in server logic
     * @throws DataException throw if some error with data
     */
    public List<M> getEntitiesList(
        Request request, 
        String right, 
        String action
    ) throws AccessException, TokenException, ServerException, DataException {
        Error error = new Error("Method for get entitites list not implemented");
        throw new ServerException("NotImplemented", error);
    } 

    /**
     * Method for get entities list without access check
     * @param request Spark request object
     * @return entities list
     * @throws AccessException throw if user hasn't access to this method
     * @throws TokenException throw if token not sent or user from url & request not equals
     * @throws ServerException throw if some error in server logic
     * @throws DataException throw if some error with data
     */
    public List<M> getEntitiesList(
        Request request
    ) throws AccessException, TokenException, ServerException, DataException {
        Error error = new Error("Method for get entitites list not implemented");
        throw new ServerException("NotImplemented", error);
    } 
}
