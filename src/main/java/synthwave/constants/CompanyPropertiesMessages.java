package synthwave.constants;

/**
 * Messages for company properties controller as enum
 * @author small-entropy
 */
public enum CompanyPropertiesMessages {
    LIST("Successfully get company properties"),
	ENTITY("Successfully get company property"),
	CREATED("Successfully created company property"),
	UPDATED("Company property successfully updated"),
	DELETED("Successfully removed company property");
	/** Property of message */
	private String message;
	
	/**
	 * Default constructor for enum
	 * @param message value of message
	 */
	CompanyPropertiesMessages(String message) {
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
