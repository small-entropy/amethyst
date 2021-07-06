package synthwave.dto.v1;

import platform.dto.BaseDTO;
import synthwave.models.mongodb.embeddeds.EmbeddedProperty;
import synthwave.models.mongodb.standalones.User;
import java.util.List;

/**
 * Class data transfer object for company model
 * @author small-entropy
 */
public class CompanyDTO extends BaseDTO {
    private String name;
    private String title;
    private String description;
    private User owner;
    private List<EmbeddedProperty> profile;

    public CompanyDTO() {}

    public CompanyDTO(
            String name, 
            String title, 
            String description, 
            User owner, 
            List<EmbeddedProperty> profile
    ) {
        this.name = name;
        this.title = title;
        this.description = description;
        this.owner = owner;
        this.profile = profile;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public List<EmbeddedProperty> getProfile() {
        return profile;
    }

    public void setProfile(List<EmbeddedProperty> profile) {
        this.profile = profile;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
