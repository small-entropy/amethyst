package DataTransferObjects.v1;

import DataTransferObjects.base.BaseDTO;
import Models.Embeddeds.EmbeddedOwner;

/**
 * Class of data trasfer object for tag model
 * @author small-entropy
 */
public class TagDTO extends BaseDTO {
    private String name;
    private String value;
    private String description;
    private EmbeddedOwner owner;

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

    public EmbeddedOwner getOwner() {
        return owner;
    }

    public void setOwner(EmbeddedOwner owner) {
        this.owner = owner;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
