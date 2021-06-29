package Repositories.v1;

import DataTransferObjects.v1.TagDTO;
import Exceptions.DataException;
import Filters.TagsFilter;
import Models.Embeddeds.EmbeddedOwner;
import Models.Standalones.Tag;
import Repositories.Core.MorphiaRepository;
import dev.morphia.Datastore;

/**
 * Class of repository for tags collection
 * @author small-entropy
 */
public class TagsRepository 
        extends MorphiaRepository<Tag, TagsFilter, TagDTO> {

    /**
     * Constructor of repository for tags collection
     * @param datastore Morphia datastore object
     */
    public TagsRepository(Datastore datastore) {
        super(datastore, Tag.class);
    }
    
    /**
     * Method for create tag document in collection
     * @param tagDTO tag data transfer object
     * @return created tag document
     */
    @Override
    public Tag create(TagDTO tagDTO) {
        EmbeddedOwner owner = new EmbeddedOwner(
                tagDTO.getOwner().getId(),
                tagDTO.getOwner().getUsername()
        );
        
        Tag tag = new Tag(
                tagDTO.getName(),
                tagDTO.getTitle(),
                tagDTO.getDescription(),
                owner
        );
        save(tag);
        return tag;
    }
    
    /**
     * Handler for update method
     * @param tagDTO tag data transfer obejct
     * @param tag tag document
     * @return result of update
     */
    @Override
    protected boolean updateHandler(TagDTO tagDTO, Tag tag) {
        boolean changed = false;
        var description = tagDTO.getDescription();
        var value = tagDTO.getTitle();
        if (description != null && (tag.getDescription() == null
                || !tag.getDescription().equals(description))) {
            tag.setDescription(description);
            changed = true;
        }
        if(value != null && (tag.getTitle()== null
                || tag.getTitle().equals(value))) {
            tag.setTitle(value);
            if (!changed) {
                changed = true;
            }
        }
        return changed;
    }
}
