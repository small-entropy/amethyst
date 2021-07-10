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
	
	private String message;
	
	AuthorizationMessages(String message) {
		this.message =  message;
	}
	
	public String getMessage() {
		return message;
	}
}
