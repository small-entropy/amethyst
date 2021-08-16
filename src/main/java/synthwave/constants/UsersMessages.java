package synthwave.constants;

/**
 * Enum for users messages
 * @author small-entropy */
public enum UsersMessages {
	LIST("User list successfully founded"),
    ENTITY("Successfully found user"),
    DELETED("Successfully marked to remove");
	/** Property of message value */
	private String message;
	
	/**
	 * Default enum constructor
	 * @param message value of message
	 */
	UsersMessages(String message) {
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
