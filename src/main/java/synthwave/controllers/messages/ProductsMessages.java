/**
 * 
 */
package synthwave.controllers.messages;

/**
 * Enum with default products messages
 * @author small-entropy
 */
public enum ProductsMessages {
	LIST("Successfully get products"),
	ENTITY("Successfully get product"),
	CREATED("Successfully created product"),
	UPDATED("Successfully updated product"),
	DELETED("Successfully deleted product");
	/** Property for message */
	private String message;
	
	/**
	 * Default constructor for enum
	 * @param message value of message 
	 */
	ProductsMessages(String message) {
		this.message = message;
	}
	
	/**
	 * Getter for products message
	 * @return value of message
	 */
	public String getMessage() {
		return message;
	}
}
