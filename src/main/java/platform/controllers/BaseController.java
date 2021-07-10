/**
 * 
 */
package platform.controllers;

import com.mongodb.internal.thread.DaemonThreadFactory;

import platform.constants.DefaultActions;

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
	
    private String rule = "default-rule";
	
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
			String rule
	) {
		this.service = service;
		this.transformer = transformer;
		this.rule = rule;
	}
	
	public String getRule() {
		return rule;
	}
	
	public void setRule(String rule) {
		this.rule = rule;
	}
	
	/**
	 * Method for get controller routes
	 */
	public void getRoutes() {}

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
}
