package synthwave.models.mongodb.base;

import synthwave.models.mongodb.embeddeds.EmbeddedOwner;
import synthwave.models.mongodb.embeddeds.EmbeddedProperty;
import java.util.List;

import org.bson.types.ObjectId;

import platform.models.mongodb.morphia.Document;

public class DocumentExtended extends Document {
	private List<EmbeddedProperty> profile;
	private List<EmbeddedProperty> properties;
	
	public DocumentExtended() {
		super();
	}
	
	public DocumentExtended(
			String name, 
			String title, 
			EmbeddedOwner owner
	) {
        super(name, title, owner);
    }
	
	public DocumentExtended(
			String name, 
			String title, 
			EmbeddedOwner owner,
			List<EmbeddedProperty> profile,
			List<EmbeddedProperty> properties
	) {
        super(name, title, owner);
        this.profile = profile;
        this.properties = properties;
    }

     public DocumentExtended(
             ObjectId id, 
             String name, 
             String title, 
             EmbeddedOwner owner
     ) {
        super(id, name, title, owner);
    }
     
     public DocumentExtended(
             String name, 
             String title, 
             String description, 
             EmbeddedOwner owner
     ) {
         super(name, title, description, owner);
     }
	
	public DocumentExtended(
            ObjectId id,
            String name, 
            String title, 
            String description, 
            EmbeddedOwner owner
    ) {
        super(id, name, title, description, owner);
    }
	
	public DocumentExtended(
            ObjectId id, 
            String name, 
            String title, 
            String description, 
            EmbeddedOwner owner,
            List<EmbeddedProperty> profile,
            List<EmbeddedProperty> properties
    ) {
        super(id, name, title, description, owner);
        this.profile = profile;
        this.properties = properties;
    }
	
	public DocumentExtended(
            String name, 
            String title, 
            String description, 
            EmbeddedOwner owner,
            List<EmbeddedProperty> profile,
            List<EmbeddedProperty> properties
    ) {
        super(name, title, description, owner);
        this.profile = profile;
        this.properties = properties;
    }
	
	public List<EmbeddedProperty> getProfile() {
		return profile;
	}
	
	public void setProfile(List<EmbeddedProperty> profile) {
		this.profile = profile;
	}
	
	public List<EmbeddedProperty> getProperties() {
		return properties;
	}
	
	public void setProperties(List<EmbeddedProperty> properties) {
		this.properties = properties;
	}
}
