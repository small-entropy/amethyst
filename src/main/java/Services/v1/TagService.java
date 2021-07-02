package Services.v1;

import DataTransferObjects.v1.RuleDTO;
import Exceptions.AccessException;
import Exceptions.DataException;
import Models.Standalones.Tag;
import Repositories.v1.TagsRepository;
import Repositories.v1.UsersRepository;
import Services.core.CoreTagService;
import Utils.common.Comparator;
import Utils.common.ParamsManager;
import Utils.v1.RightManager;
import java.util.List;
import org.bson.types.ObjectId;
import spark.Request;

/**
 * Class service for work with tags collectoin
 * @author small-entropy
 * @version 1
 */
public class TagService extends CoreTagService {
    
    /** List of excludes fields for public read documents */
    private static final String[] PRIVATE_EXCLUDES = new String[] {
        "owner",
        "properties",
        "version"
    };
    
    /** List of exludes fields for public read documents */
    private static final String[] PUBLIC_EXCLUDES = new String[] {
        "version"
    };

    /**
     * Method fot get list of tags documents
     * @param request Spark request object
     * @param tagsRepository repository for tags collection
     * @param rule rule data transfer object
     * @return list of tags documents
     * @throws DataException throw if can not found tag documents
     */
    public static List<Tag> getTagsList(
            Request request,
            TagsRepository tagsRepository,
            RuleDTO rule,
            boolean byOwner
    ) throws DataException {
        String[] excludes = getExcludes(
                request, 
                rule, 
                PUBLIC_EXCLUDES, 
                PRIVATE_EXCLUDES
        );
        var userId = (byOwner)
                ? ParamsManager.getUserId(request)
                : null;
        List<Tag> tags = getList(
                request,
                tagsRepository,
                excludes,
                null
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
     * @param tagsRepository  repository for tags collection
     * @param usersRepository  repository for users collection
     * @param rule rule data transfer object
     * @return created tag document
     * @throws AccessException throw if user has no access to 
     *                         create tag documents
     * @throws DataException throw if can not found createad 
     *                       documents or creator user
     */
    public static Tag createTag(
            Request request,
            TagsRepository tagsRepository,
            UsersRepository usersRepository,
            RuleDTO rule
    ) throws AccessException, DataException {
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        boolean hasAccess = (isTrusted) 
                ? rule.isMyGlobal() 
                : rule.isOtherGlobal();
        if (hasAccess) {
            ObjectId userId = ParamsManager.getUserId(request);
            Tag tag = createTag(
                    userId,
                    request,
                    tagsRepository,
                    usersRepository
            );
            String[] excludes = getExcludes(
                    request, 
                    rule, 
                    PUBLIC_EXCLUDES, 
                    PRIVATE_EXCLUDES
            );
            return getTagByDocument(tag, tagsRepository, excludes);
        } else {
            Error error = new Error("Has no access to create tag");
            throw new AccessException("CanNotCreate", error);
        }
    }

    /**
     * Method get tag document 
     * @param request Spark request object
     * @param tagsRepository repository for tags collection
     * @param rule rule data transfer obejct
     * @return founded tag document
     * @throws DataException throw if can not found tag document
     */
    public static Tag getTagById(
            Request request,
            TagsRepository tagsRepository,
            RuleDTO rule
    ) throws DataException {
        String[] excludes = getExcludes(
                request, 
                rule, 
                PUBLIC_EXCLUDES, 
                PRIVATE_EXCLUDES
        );
        var tag = getTagByRequestByUserId(
                request, 
                tagsRepository, 
                excludes
        );
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
     * @param tagsRepository repository for tags collection
     * @param rule rule data transfer object
     * @return updated tag document
     * @throws AccessException throw if has no access to update tag document
     * @throws DataException throw if can't get ids from params or find tag
     */
    public static Tag updateTag(
            Request request,
            TagsRepository tagsRepository,
            RuleDTO rule
    ) throws AccessException, DataException {
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        boolean hasAccess = (isTrusted) 
                ? rule.isMyPrivate() 
                : rule.isOtherPrivate();
        if (hasAccess) {
            ObjectId userId = ParamsManager.getUserId(request);
            ObjectId tagId = ParamsManager.getTagId(request);
            Tag tag = updateTag(
                    userId,
                    tagId,
                    request,
                    tagsRepository
            );
            String[] excludes = getExcludes(
                    request, 
                    rule, 
                    PUBLIC_EXCLUDES, 
                    PRIVATE_EXCLUDES
            );
            return getTagByDocument(tag, tagsRepository, excludes);
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
    public static Tag deleteTag(
            Request request,
            TagsRepository tagsRepository,
            RuleDTO rule
    ) throws AccessException, DataException {
        boolean hasAccess = RightManager.chechAccess(request, rule);
        if (hasAccess) {
            ObjectId userId = ParamsManager.getUserId(request);
            ObjectId tagId = ParamsManager.getTagId(request);
            return deleteTag(userId, tagId, tagsRepository);
        } else {
            Error error = new Error("Has no access to delete tag document");
            throw new AccessException("CanNotDelete", error);
        }
    }
}
