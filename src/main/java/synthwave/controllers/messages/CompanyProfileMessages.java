package synthwave.controllers.messages;

/**
 * Messages for company profile controller as enum
 * @author small-entropy
 */
public enum CompanyProfileMessages {
    LIST("Successfully get company profile"),
    ENTITY("Successfully get company profile property"),
    CREATED("Successfully created profile property"),
    UPDATED("Profile property successfully updated"),
    DELETED("Profile property removed");
	/** Property of message */
	private String message;
	
	/**
	 * Default enum constructor
	 * @param message value of message
	 */
	CompanyProfileMessages(String message) {
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
