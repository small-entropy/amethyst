package synthwave.services.v1.tags;

import platform.dto.RuleDTO;
import platform.exceptions.AccessException;
import platform.exceptions.DataException;
import synthwave.models.mongodb.standalones.Tag;
import synthwave.services.core.tags.CoreTagService;
import platform.utils.helpers.ParamsManager;
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
                    "version",
                    "status"
                },
                new String[] {
                    "version",
                    "status"
                }
        );
    }
    
    private List<Tag> getEntitiesList(
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

    @Override
    public List<Tag> getEntitiesList(
        Request request,
        String right,
        String action
    ) throws DataException {
        return getEntitiesList(request, right, action, false);
    }

    @Override
    public List<Tag> getEntitiesListByOwner(
        Request request,
        String right,
        String action
    ) throws DataException {
        return getEntitiesList(request, right, action, true);
    }
    
    @Override
    public Tag createEntity(
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

    @Override
    public Tag getEntityById(
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

    @Override
    public Tag updateEntity(
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

    @Override
    public Tag deleteEntity(
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
