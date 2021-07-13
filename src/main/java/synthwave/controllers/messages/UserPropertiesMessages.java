package synthwave.controllers.messages;

/**
 * Enum for user properties messages
 * @author small-entropy
 *
 */
public enum UserPropertiesMessages {
	LIST("Successfully get user properties"),
	ENTITY("Successfully get user property"),
	CREATED("Successfully created user property"),
	UPDATED("User property successfully updated"),
	DELETED("Successfully removed user property");
	/** Property of message */
	private String message;
	
	/**
	 * Default constructor for enum
	 * @param message value of message
	 */
	UserPropertiesMessages(String message) {
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
