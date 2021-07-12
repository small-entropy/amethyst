/**
 * 
 */
package synthwave.controllers.messages;

/**
 * Enum for authorization messages
 * @author small-entropy
 */
public enum AuthorizationMessages {
	REGISTERED("User successfully registered"),
    AUTOLOGIN("Successfully login by token"),
    LOGIN("Login is success"),
    LOGOUT("User successfully logout"),
    PASSWORD_CHANGED("Password successfully changes");
	/** Property with value of message */
	private String message;
	
	/**
	 * Default enum constructor
	 * @param message value for message property
	 */
	AuthorizationMessages(String message) {
		this.message =  message;
	}
	
	/**
	 * Getter for message property
	 * @return value of message property
	 */
	public String getMessage() {
		return message;
	}
}
