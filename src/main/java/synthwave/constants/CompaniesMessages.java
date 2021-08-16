package synthwave.constants;

/**
 * Enum with messages for companies routes
 * @author small-entropy
 */
public enum CompaniesMessages {
	LIST("Successfull get companies"),
    ENTITY("Successfull get tag"),
    CREATED("Successfull created company"),
    UPDATED("Successfull updated company"),
    DELETED("Successfull deleted company");
	/** Property with value of message */
	private String message;
	
	/**
	 * Default enum constructor
	 * @param message value for message property
	 */
	CompaniesMessages(String message) {
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
