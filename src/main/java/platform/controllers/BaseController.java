package platform.controllers;

import platform.constants.DefaultActions;
import platform.exceptions.AccessException;

/**
 * Base class for create controller
 * @author small-entropy
 */
public abstract class BaseController <S, T> {
	/** Property with controller service */
	private S service;
	/** Property for response transformer */
	private T transformer;
	
	 /** Property with name of create action */
    private final String createActionName = DefaultActions.CREATE.getName();
    /** Property with name of read action */
    private final String readActionName = DefaultActions.READ.getName();
    /** Property with name of update action */
    private final String updateActionName = DefaultActions.UPDATE.getName();
    /** Property with name of delete action */
    private final String deleteActionName = DefaultActions.DELETE.getName();
	
    private String right = "default-rule";
	
	/**
	 * Default constructor for base controller. Create instance
	 * by service & adapter
	 * @param service service object
	 * @param adapter adapter object
	 * @param rule rule name
	 */
	public BaseController(
			S service, 
			T transformer,
			String right
	) {
		this.service = service;
		this.transformer = transformer;
		this.right = right;
	}
	
	/**
	 * Getter for right
	 * @return value of right
	 */
	public String getRight() {
		return right;
	}
	
	/**
	 * Setter for right
	 * @param right new value of right
	 */
	public void setRight(String right) {
		this.right = right;
	}

	/**
	 * Method for register middlewares before request
	 */
	protected void registerBefore() {}

	/**
	 * Mthod for register middleware after request
	 */
	protected void registerAfter() {}
	
	/**
	 * Method for get controller routes
	 */
	public void register() {}

	/**
	 * Method for register get entity by id route
	 */
	protected void getEntityByIdRoute() {}

	/**
	 * Method for register get list of entities route
	 */
	protected void getListRoute() {}

	/**
	 * Method for register create entity route
	 */
	protected void createEntityRoute() {}

	/**
	 * Method for register update entity route
	 */
	protected void updateRoute() {}
	
	/**
	 * Method for register delete entity route
	 */
	protected void deleteRoute() {}

	/**
	 * Getter for response transformer
	 * @return current response transformer
	 */
	public T getTransformer() {
		return transformer;
	}
	
	/**
	 * Setter for response transformer
	 * @param transformer new value of response transformer
	 */
	public void setTransformer(T transformer) {
		this.transformer = transformer;
	}
	
	/**
	 * Getter for service property
	 * @return current service value
	 */
	public S getService() {
		return service;
	}
	
	/**
	 * Getter for service property
	 * @param service new value of service
	 */
	public void setService(S service) {
		this.service = service;
	}

	public String getUpdateActionName() {
		return updateActionName;
	}
	
	public String getCreateActionName() {
		return createActionName;
	}
	
	public String getDeleteActionName() {
		return deleteActionName;
	}
	
	public String getReadActionName() {
		return readActionName;
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
}
