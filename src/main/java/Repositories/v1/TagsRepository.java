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
                tagDTO.getValue(),
                tagDTO.getDescription(),
                owner
        );
        save(tag);
        return tag;
    }
    
    /**
     * Method for update tag document
     * @param tagDTO tag data transfer object
     * @param filter filter object
     * @return updated tag document
     * @throws DataException throw if tag document not found
     */
    public Tag update(
            TagDTO tagDTO,
            TagsFilter filter
    ) throws DataException {
        var tag = findOneByOwnerAndId(filter);
        var description = tagDTO.getDescription();
        var value = tagDTO.getValue();
        if (tag != null) {
            if (description != null && (tag.getDescription() == null 
                    || !tag.getDescription().equals(description))) {
                tag.setDescription(description);
            }
            
            if(value != null && (tag.getValue() == null
                    || tag.getValue().equals(value))) {
                tag.setValue(value);
            }
            
            save(tag);
            return tag;
        } else {
            Error error = new Error("Can not find tag document");
            throw new DataException("NotFound", error);
        }
        
    }
    
    /**
     * Method for deactivate (remove) tag document
     * @param filter filter object
     * @return deactivated tag document
     * @throws DataException throw if tag document not found
     */
    public Tag deactivate(TagsFilter filter) throws DataException {
        Tag tag = findOneByOwnerAndId(filter);
        if (tag != null) {
            tag.deactivate();
            save(tag);
            return tag;
        } else {
            Error error = new Error("Can not find tag document");
            throw new DataException("NotFound", error);
        } 
    }
}
