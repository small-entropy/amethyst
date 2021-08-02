package synthwave.controllers.messages;

/**
 * Messages gor category profile controller as enum
 * @author small-entropy
 */
public enum CategoryPropertiesMessages {
    LIST("Successfully get category properties"),
	ENTITY("Successfully get category property"),
	CREATED("Successfully created category property"),
	UPDATED("Category property successfully updated"),
	DELETED("Successfully removed category property");
	/** Property of message */
	private String message;
	
	/**
	 * Default constructor for enum
	 * @param message value of message
	 */
	CategoryPropertiesMessages(String message) {
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
