package synthwave.constants;

/**
 * Mesasges gor catagory profile controller as enum
 * @author small-entropy
 */
public enum CategoryProfileMessages {
    LIST("Successfully get category profile"),
	ENTITY("Successfully get category profile property"),
	CREATED("Successfully created category profile property"),
	UPDATED("Category profile property successfully updated"),
	DELETED("Successfully removed category profile property");
	/** Property of message */
	private String message;
	
	/**
	 * Default constructor for enum
	 * @param message value of message
	 */
	CategoryProfileMessages(String message) {
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
