package synthwave.constants;

/**
 * Messages for catalog profile controller as enum
 * @author small-entropy
 */
public enum CatalogProfileMessages {
    LIST("Successfully get catalog profile"),
    ENTITY("Successfully get catalog profile property"),
    CREATED("Successfully created profile property"),
    UPDATED("Profile property successfully updated"),
    DELETED("Profile property removed");
	/** Property of message */
	private String message;
	
	/**
	 * Default enum constructor
	 * @param message value of message
	 */
	CatalogProfileMessages(String message) {
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
