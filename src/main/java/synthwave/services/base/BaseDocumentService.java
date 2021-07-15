package synthwave.services.base;

import platform.dto.RuleDTO;
import synthwave.filters.UsersFilter;
import synthwave.models.mongodb.standalones.User;
import synthwave.repositories.mongodb.v1.UsersRepository;
import synthwave.utils.access.RightManager;
import synthwave.utils.helpers.Comparator;
import dev.morphia.Datastore;
import org.bson.types.ObjectId;
import spark.Request;

/**
 * Abstract class for create service (for document)
 * @author small-entropy
 */
public abstract class BaseDocumentService <R> extends BaseService<R> {
	
	/** Property with repository for work with users data */
	UsersRepository usersRepository;
	
	public boolean checkHasAccess(Request request, String right, String action) {
		RuleDTO rule = getRule(request, right, action);
		return checkHasAccess(request, rule);
	}
	
	public boolean checkHasAccess(Request request, RuleDTO rule) {
		boolean isTrusted = Comparator.id_fromParam_fromToken(request);
		return checkHasAccess(rule, isTrusted);
	}
	
	public boolean checkHasAccess(RuleDTO rule, boolean isTrusted) {
		boolean hasAccess;
		if (rule != null) {
			 return checkExistHasAccess(rule, isTrusted);
		} else {
			hasAccess = false;
		}
		return hasAccess;
	}
	
	public boolean checkHasGlobalAccess(Request request, String right, String action) {
		RuleDTO rule = getRule(request, right, action);
        return  checkHasGlobalAccess(request, rule);
	}
	
	public boolean checkHasGlobalAccess(Request request, RuleDTO rule) {
		boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        return (isTrusted) ? rule.isMyGlobal(): rule.isOtherGlobal();
	}
	
	protected boolean checkExistHasAccess(RuleDTO rule, boolean isTrusted) {
		return (isTrusted) ? rule.isMyPrivate() : rule.isOtherPrivate();
	}
	
	/**
	 * Default constructor for create base document service. Create instance 
	 * by datastore & repository
	 * @param datastore Morphia datastore object
	 * @param repository service main repository
	 */
	public BaseDocumentService(Datastore datastore, R repository) {
		super(repository);
		this.usersRepository = new UsersRepository(datastore);
	}
	
	/**
	 * Constructor for create base document service. Create instance
	 * by datastore, repository & exclude fields (for some levels)
	 * @param datastore Morphia datastore object
	 * @param repository service main repository
	 * @param globalExcludes global exclude fields
	 * @param publicExcludes public exclude fields
	 * @param privateExcludes private exclude fields
	 */
	public BaseDocumentService(
           Datastore datastore,
           R repository,
           String[] globalExcludes,
           String[] publicExcludes,
           String[] privateExcludes
    ) {
		super(repository, globalExcludes, publicExcludes, privateExcludes);
		this.usersRepository = new UsersRepository(datastore);
	}
	
	/**
	 * Getter for user repository
	 * @return users repository
	 */
	public UsersRepository getUsersRepository() {
        return usersRepository;
    }

	/**
	 * Setter for users repository
	 * @param usersRepository new value for users repository
	 */
    public void setUsersRepository(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }
    
    /**
     * Method for get for user document by entity id
     * @param id entity id
     * @return user document
     */
    protected User getUserById(ObjectId id) {
        UsersFilter filter = new UsersFilter(id, new String[] {});
        return getUsersRepository().findOneById(filter);
    }
    
    /**
     * Method for get rule data transfer object by request, 
     * right name & action name
     * @param request Spark request object
     * @param right right name
     * @param action action name
     * @return rule data transfer object
     */
    protected RuleDTO getRule(
            Request request,
            String right,
            String action
    ) {
        return RightManager.getRuleByRequest_Token(
                request, 
                getUsersRepository(), 
                right, 
                action
        );
    }
}
