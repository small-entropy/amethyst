package Services.base;

import DataTransferObjects.v1.RuleDTO;
import Utils.v1.RightManager;
import spark.Request;

/**
 * Base class to create service for server service
 * @author small-entropy
 */
public abstract class BaseService <R> {
	
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
}
