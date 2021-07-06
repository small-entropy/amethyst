package synthwave.dto.v1;

import platform.dto.BaseDTO;
import synthwave.models.mongodb.standalones.User;

/**
 * Class of data trasfer object for tag model
 * @author small-entropy
 */
public class TagDTO extends BaseDTO {
    private String name;
    private String title;
    private String description;
    private User owner;

    public TagDTO() {}

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
