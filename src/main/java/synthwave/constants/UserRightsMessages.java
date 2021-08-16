/**
 * 
 */
package synthwave.constants;

/**
 * Enum for user rights messages
 * @author small-entropy
 *
 */
public enum UserRightsMessages {
	LIST("Successfully get user rights"),
    ENTITY("Successfullly get user rights"),
    CREATED("Successfully create user right"),
    UPDATED("User right successfully updated"),
    DELETED("Successfully removed user right");
	/** Property for message */
	private String message;
	
	/**
	 * Default enum constructor
	 * @param message value of message
	 */
	UserRightsMessages(String message) {
		this.message = message;
	}
	
	/**
	 * Getter for message property
	 * @return value of message
	 */
	public String getMessage() {
		return message;
	}
}
