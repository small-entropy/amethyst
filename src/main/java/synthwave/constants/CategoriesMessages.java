package synthwave.constants;

/**
 * Enum with messages for categories routes
 * @author small-entropy
 */
public enum CategoriesMessages {
	LIST("Successfully get categories"),
    ENTITY("Successfully get category"),
    CREATED("Successfully created category"),
    UPDATED("Successfully updated category"),
    DELETED("Successfulle deleted category");
	/** Property with value of message */
	private String message;
	
	/**
	 * Default enum constructor
	 * @param message value for message property
	 */
	CategoriesMessages(String message) {
		this.message = message;
	}
	
	/**
	 * Getter for message property
	 * @return value of message property
	 */
	public String getMessage() {
		return message;
	}
}
