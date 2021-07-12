package synthwave.controllers.messages;

public enum CatalogsMessages {
	LIST("Successfully get list of catalogs"),
    ENTITY("Successfully get catalog"),
    CREATED("Successfully created catalog"),
    UPDATED("Successfully catapog updated"),
    DELETED("Successfully deleted catalog");
	
	private String message;
	
	CatalogsMessages(String message) {
		this.message = message;
	}
	
	public String getMessage() {
		return message;
	}
}
