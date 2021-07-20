package synthwave.services.v1.tags;

import platform.dto.RuleDTO;
import platform.exceptions.AccessException;
import platform.exceptions.DataException;
import synthwave.models.mongodb.standalones.Tag;
import synthwave.services.core.tags.CoreTagService;
import synthwave.utils.helpers.Comparator;
import platform.utils.helpers.ParamsManager;
import synthwave.utils.access.RightManager;
import dev.morphia.Datastore;
import java.util.List;
import org.bson.types.ObjectId;
import spark.Request;

/**
 * Class service for work with tags collectoin
 * @author small-entropy
 * @version 1
 */
public class TagService extends CoreTagService {
    
    public TagService(Datastore datastore) {
        super(
                datastore,
                new String[] {},
                new String[] {
                    "owner",
                    "properties",
                    "version",
                    "status"
                },
                new String[] {
                    "version",
                    "status"
                }
        );
    }

    /**
     * Method fot get list of tags documents
     * @param request Spark request object
     * @param rule rule data transfer object
     * @return list of tags documents
     * @throws DataException throw if can not found tag documents
     */
    public List<Tag> getTagsList(
            Request request,
            String right,
            String action,
            boolean byOwner
    ) throws DataException {
        RuleDTO rule = getRule(request, right, action);
        String[] excludes = getExcludes(request, rule);
        var userId = (byOwner)
                ? ParamsManager.getUserId(request)
                : null;
        List<Tag> tags = getList(
                request,
                excludes,
                userId
        );
        if (!tags.isEmpty()) {
            return tags;
        } else {
            Error error = new Error("Can not find tag documents");
            throw new DataException("NotFound", error);
        }
    }
    
    /**
     * Method fot create tag documents
     * @param request Spark request object
     * @param rule rule data transfer object
     * @return created tag document
     * @throws AccessException throw if user has no access to 
     *                         create tag documents
     * @throws DataException throw if can not found createad 
     *                       documents or creator user
     */
    public Tag createTag(
            Request request,
            String right,
            String action
    ) throws AccessException, DataException {
        RuleDTO rule = getRule(request, right, action);
        boolean hasAccess = checkHasGlobalAccess(request, rule);
        if (hasAccess) {
            ObjectId userId = ParamsManager.getUserId(request);
            Tag tag = createTag(userId, request);
            String[] excludes = getExcludes(request, rule);
            return getTagByDocument(tag, excludes);
        } else {
            Error error = new Error("Has no access to create tag");
            throw new AccessException("CanNotCreate", error);
        }
    }

    /**
     * Method get tag document 
     * @param request Spark request object
     * @return founded tag document
     * @throws DataException throw if can not found tag document
     */
    public Tag getTagById(
            Request request,
            String right,
            String action
    ) throws DataException {
        RuleDTO rule = getRule(request, right, action);
        String[] excludes = getExcludes(request, rule);
        var tag = getTagByRequestByUserId(request, excludes);
        if (tag != null) {
            return tag;
        } else {
            Error error = new Error("Can not find tag documents by user & if from request params");
            throw new DataException("NotFound", error);
        }
    }

    /**
     * Method for update tag document
     * @param request Spark request object
     * @return updated tag document
     * @throws AccessException throw if has no access to update tag document
     * @throws DataException throw if can't get ids from params or find tag
     */
    public Tag updateTag(
            Request request,
            String right,
            String action
    ) throws AccessException, DataException {
        RuleDTO rule = getRule(request, right, action);
        boolean hasAccess = checkHasAccess(request, rule);
        if (hasAccess) {
            ObjectId userId = ParamsManager.getUserId(request);
            ObjectId tagId = ParamsManager.getTagId(request);
            Tag tag = updateTag(userId, tagId, request);
            String[] excludes = getExcludes(request, rule);
            return getTagByDocument(tag, excludes);
        } else {
            Error error = new Error("Has no access to update tag document");
            throw new AccessException("CanNotUpdate", error);
        }
    }

    /**
     * Method for delete tag document
     * @param request Spark reqeust object
     * @param tagsRepository repository for tags collection
     * @param rule rule data transfer object
     * @return deactivated tag document
     * @throws AccessException if has no access to delete tag document
     * @throws DataException if can't get ids from params or find tag
     */
    public Tag deleteTag(
            Request request,
            String right,
            String action
    ) throws AccessException, DataException {
        boolean hasAccess = checkHasGlobalAccess(request, right, action);
        if (hasAccess) {
            ObjectId userId = ParamsManager.getUserId(request);
            ObjectId tagId = ParamsManager.getTagId(request);
            return deleteTag(userId, tagId);
        } else {
            Error error = new Error("Has no access to delete tag document");
            throw new AccessException("CanNotDelete", error);
        }
    }
    
    @Override
    protected boolean checkExistHasAccess(RuleDTO rule, boolean isTrusted) {
    	return  (isTrusted) ? rule.isMyPrivate() : rule.isOtherPrivate();
    }
}
