package Services.core;

import DataTransferObjects.v1.TagDTO;
import Exceptions.DataException;
import Filters.TagsFilter;
import Models.Standalones.Tag;
import Models.Standalones.User;
import Repositories.v1.TagsRepository;
import Repositories.v1.UsersRepository;
import Utils.common.ParamsManager;
import Utils.common.QueryManager;
import java.util.List;
import org.bson.types.ObjectId;
import spark.Request;

/**
 * Class with static methods for work with tags data
 * @author small-entropy
 */
public class CoreTagService extends AbstractService {
   
    /**
     * Method fot get list of tags documents
     * @param request Spark request object
     * @param tagsRepository repository for tags collection
     * @param excludes exlude fields
     * @return list of tags documents
     */
    protected static List<Tag> getList(
            Request request,
            TagsRepository tagsRepository,
            String[] excludes,
            ObjectId ownerId
    ) {
        int skip = QueryManager.getSkip(request);
        int limit = QueryManager.getLimit(request);
        TagsFilter filter = new TagsFilter(skip, limit, excludes);
        if (ownerId != null) {
            filter.setOwner(ownerId);
            return tagsRepository.findAllByOwnerId(filter);
        } else {
            return tagsRepository.findAll(filter);
        }
    }
    
    /**
     * Method for get tag document with excludes
     * @param tag tag dacument
     * @param tagsRepository repository for tags collection
     * @param excludes exlude fields
     * @return tag document
     */
    protected static Tag getTagByDocument(
            Tag tag, 
            TagsRepository tagsRepository, 
            String[] excludes
    ) {
        ObjectId ownerId = tag.getOwner().getId();
        ObjectId tagId = tag.getId();
        return getTagById(tagId, ownerId, tagsRepository, excludes);
    }
    
    /**
     * Method for get tag document by tag id & owner id
     * @param tagId tag id
     * @param ownerId owner id
     * @param tagsRepository repository for tags collection
     * @param excludes exclude fields
     * @return tag document
     */
    protected static Tag getTagById(
            ObjectId tagId,
            ObjectId ownerId,
            TagsRepository tagsRepository,
            String[] excludes
    ) {
        TagsFilter filter = new TagsFilter();
        filter.setId(tagId);
        filter.setOwner(ownerId);
        filter.setExcludes(excludes);
        return tagsRepository.findOneByOwnerAndId(filter);
    }
    
    /**
     * Method for create tag document by request
     * @param userId user id
     * @param request Spark reqeust object
     * @param tagsRepository repository for tags collection
     * @param usersRepository repository for users collection
     * @return created tag document
     */
    protected static Tag createTag(
            ObjectId userId,
            Request request,
            TagsRepository tagsRepository,
            UsersRepository usersRepository
    ) {
        User user = CoreUserService.getUserById(userId, usersRepository);
        TagDTO tagDTO = TagDTO.build(request, TagDTO.class);
        tagDTO.setOwner(user);
        return tagsRepository.create(tagDTO);
    }
    
    /**
     * Method for get tag document by tag id and owner id
     * @param request Spark request object
     * @param tagsRepository repository for tags collection
     * @param excludes exclude fields
     * @return founded tag document
     * @throws DataException throw if can not get ids from params
     */
    protected static Tag getTagByRequestByUserId (
            Request request,
            TagsRepository tagsRepository,
            String[] excludes
    ) throws DataException {
        ObjectId tagId = ParamsManager.getTagId(request);
        ObjectId ownerId = ParamsManager.getUserId(request);
        return getTagById(tagId, ownerId, tagsRepository, excludes);
    }
    
    /**
     * Method for update tag document
     * @param userId owner id
     * @param tagId tag id
     * @param request Spark request obejct
     * @param tagsRepository repository for tags collection
     * @return updated tag document
     * @throws DataException throw if can get ids from params or found tag
     */
    protected static Tag updateTag(
            ObjectId userId,
            ObjectId tagId,
            Request request,
            TagsRepository tagsRepository
    ) throws DataException {
        TagDTO tagDTO = TagDTO.build(request, TagDTO.class);
        TagsFilter filter = new TagsFilter(new String[] {});
        filter.setOwner(userId);
        filter.setId(tagId);
        return tagsRepository.update(tagDTO, filter);
    }
    
    /**
     * Method for deactivate tag document
     * @param userId owner id
     * @param tagId tag id
     * @param tagsRepository repository for tags collection
     * @return deactivated tag document
     * @throws DataException throw if can get ids from params or found tag
     */
    protected static Tag deleteTag(
            ObjectId userId, 
            ObjectId tagId, 
            TagsRepository tagsRepository
    ) throws DataException {
        TagsFilter filter = new TagsFilter(new String[] {});
        filter.setId(tagId);
        filter.setOwner(userId);
        return tagsRepository.deactivate(filter);
    }
}
