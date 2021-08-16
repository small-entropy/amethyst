/**
 * 
 */
package synthwave.constants;

/**
 * Enum for tags messages
 * @author small-entropy
 */
public enum TagsMessages {
    LIST("Successfully get tags"),
    ENTITY("Successfully get tag "),
    CREATED("Successfully created tag"),
    UPDATED("Successfully updated tag"),
    DELETED("Successfully deleted tag");
	/** Message property */
	private String message;
	
	/**
	 * Default enum constructor
	 * @param message value of message
	 */
	TagsMessages(String message) {
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
