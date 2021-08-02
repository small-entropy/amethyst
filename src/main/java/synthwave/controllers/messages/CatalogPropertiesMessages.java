package synthwave.controllers.messages;

/**
 * Messages for catalog properties controller as enum
 * @author small-entropy
 */
public enum CatalogPropertiesMessages {
    LIST("Successfully get catalog properties"),
	ENTITY("Successfully get catalog property"),
	CREATED("Successfully created catalog property"),
	UPDATED("Catalog property successfully updated"),
	DELETED("Successfully removed catalog property");
	/** Property of message */
	private String message;
	
	/**
	 * Default constructor for enum
	 * @param message value of message
	 */
	CatalogPropertiesMessages(String message) {
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
