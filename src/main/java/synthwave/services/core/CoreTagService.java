package synthwave.services.core;

import synthwave.dto.v1.TagDTO;
import platform.exceptions.DataException;
import synthwave.filters.TagsFilter;
import synthwave.models.mongodb.standalones.Tag;
import synthwave.models.mongodb.standalones.User;
import synthwave.repositories.mongodb.v1.TagsRepository;
import platform.services.BaseDocumentService;
import platform.utils.helpers.ParamsManager;
import platform.utils.helpers.QueryManager;
import dev.morphia.Datastore;
import java.util.List;
import org.bson.types.ObjectId;
import spark.Request;

/**
 * Class with static methods for work with tags data
 * @author small-entropy
 */
public abstract class CoreTagService 
        extends BaseDocumentService<TagsRepository> {
   
    public CoreTagService(
            Datastore datastore,
            String[] globalExcludes,
            String[] publicExcludes,
            String[] privateExcludes
    ) {
        super(
                datastore,
                new TagsRepository(datastore),
                globalExcludes,
                publicExcludes,
                privateExcludes
        );
    }
    
    /**
     * Method fot get list of tags documents
     * @param request Spark request object
     * @param excludes exlude fields
     * @param ownerId owner (user) id
     * @return list of tags documents
     */
    protected List<Tag> getList(
            Request request,
            String[] excludes,
            ObjectId ownerId
    ) {
        int skip = QueryManager.getSkip(request);
        int limit = QueryManager.getLimit(request);
        TagsFilter filter = new TagsFilter(skip, limit, excludes);
        if (ownerId != null) {
            filter.setOwner(ownerId);
            return getRepository().findAllByOwnerId(filter);
        } else {
            return getRepository().findAll(filter);
        }
    }
    
    /**
     * Method for get tag document with excludes
     * @param tag tag dacument
     * @param excludes exlude fields
     * @return tag document
     */
    protected Tag getTagByDocument(
            Tag tag,  
            String[] excludes
    ) {
        ObjectId ownerId = tag.getOwner().getId();
        ObjectId tagId = tag.getId();
        return getTagById(tagId, ownerId, excludes);
    }
    
    /**
     * Method for get tag document by tag id & owner id
     * @param tagId tag id
     * @param ownerId owner id
     * @param excludes exclude fields
     * @return tag document
     */
    protected Tag getTagById(
            ObjectId tagId,
            ObjectId ownerId,
            String[] excludes
    ) {
        TagsFilter filter = new TagsFilter();
        filter.setId(tagId);
        filter.setOwner(ownerId);
        filter.setExcludes(excludes);
        return getRepository().findOneByOwnerAndId(filter);
    }
    
    /**
     * Method for create tag document by request
     * @param userId user id
     * @param request Spark reqeust object
     * @param tagsRepository repository for tags collection
     * @param usersRepository repository for users collection
     * @return created tag document
     */
    protected Tag createTag(
            ObjectId userId,
            Request request
    ) {
        User user = getUserById(userId);
        TagDTO tagDTO = TagDTO.build(request, TagDTO.class);
        tagDTO.setOwner(user);
        return getRepository().create(tagDTO);
    }
    
    /**
     * Method for get tag document by tag id and owner id
     * @param request Spark request object
     * @param excludes exclude fields
     * @return founded tag document
     * @throws DataException throw if can not get ids from params
     */
    protected Tag getTagByRequestByUserId (
            Request request,
            String[] excludes
    ) throws DataException {
        ObjectId tagId = ParamsManager.getTagId(request);
        ObjectId ownerId = ParamsManager.getUserId(request);
        return getTagById(tagId, ownerId, excludes);
    }
    
    /**
     * Method for update tag document
     * @param userId owner id
     * @param tagId tag id
     * @param request Spark request obejct
     * @return updated tag document
     * @throws DataException throw if can get ids from params or found tag
     */
    protected Tag updateTag(
            ObjectId userId,
            ObjectId tagId,
            Request request
    ) throws DataException {
        TagDTO tagDTO = TagDTO.build(request, TagDTO.class);
        TagsFilter filter = new TagsFilter(new String[] {});
        filter.setOwner(userId);
        filter.setId(tagId);
        return getRepository().update(tagDTO, filter);
    }
    
    /**
     * Method for deactivate tag document
     * @param userId owner id
     * @param tagId tag id
     * @param tagsRepository repository for tags collection
     * @return deactivated tag document
     * @throws DataException throw if can get ids from params or found tag
     */
    protected Tag deleteTag(
            ObjectId userId, 
            ObjectId tagId
    ) throws DataException {
        TagsFilter filter = new TagsFilter(new String[] {});
        filter.setId(tagId);
        filter.setOwner(userId);
        return getRepository().deactivate(filter);
    }
}
