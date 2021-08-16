package engine.models.morphia.extend;

import java.util.List;

import org.bson.types.ObjectId;

import core.models.morphia.standalones.Standalone;
import core.models.morphia.embeddeds.EmbeddedProperty;

public class StandaloneExtended extends Standalone {
	private List<EmbeddedProperty> profile;
	private List<EmbeddedProperty> properties;
	
	public StandaloneExtended() {
		super();
	}
	
	public StandaloneExtended(ObjectId id) {
		super(id);
	}
	
	public StandaloneExtended(
			List<EmbeddedProperty> profile,
			List<EmbeddedProperty> properties
	) {
		super();
		this.profile = profile;
		this.properties = properties;
	}
	
	public StandaloneExtended(
			ObjectId id,
			List<EmbeddedProperty> profile,
			List<EmbeddedProperty> properties
	) {
		super(id);
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
