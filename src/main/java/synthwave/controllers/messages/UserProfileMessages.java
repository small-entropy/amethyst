package synthwave.controllers.messages;

/**
 * Enum for user profile messages
 * @author small-entopy
 *
 */
public enum UserProfileMessages {
	LIST("Successfully get user profile"),
    ENTITY("Successfully get user profile property"),
    CREATED("Successfully created profile property"),
    UPDATED("Profile property successfully updated"),
    DELETED("Profile property removed");
	/** Property of message */
	private String message;
	
	/**
	 * Default enum constructor
	 * @param message value of message
	 */
	UserProfileMessages(String message) {
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
