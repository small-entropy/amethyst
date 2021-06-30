package Controllers.v1;

import Controllers.base.BaseTagsController;
import DataTransferObjects.v1.RuleDTO;
import Models.Standalones.Tag;
import Repositories.v1.TagsRepository;
import Repositories.v1.UsersRepository;
import Utils.responses.SuccessResponse;
import Utils.transformers.JsonTransformer;
import dev.morphia.Datastore;
import static spark.Spark.*;
import Services.v1.TagService;
import java.util.List;

/**
 * Class for work with tags collection
 * @author small-entropy
 */
public class TagsController extends BaseTagsController {
    public static void routes(Datastore store, JsonTransformer transformer) {
        TagsRepository tagsRepository = new TagsRepository(store);
        UsersRepository usersRepository = new UsersRepository(store);
        // Route for get tags list
        get("", (req, res) -> {
            RuleDTO rule = getRule(req, usersRepository, RIGHT, READ);
            List<Tag> tags = TagService.getTagsList(
                    req,
                    tagsRepository,
                    rule,
                    false
            );
            return new SuccessResponse<>(MSG_LIST, tags);
        }, transformer);
        // Route for get tags list by for user by id
        get("/owner/:user_id", (req, res) -> {
            RuleDTO rule = getRule(req, usersRepository, RIGHT, READ);
            List<Tag> tags = TagService.getTagsList(
                    req, 
                    tagsRepository, 
                    rule,
                    true
            );
            return new SuccessResponse<>(MSG_LIST, tags);
        }, transformer);
        // Route fot create tag document
        post("/owner/:user_id", (req, res) -> {
            RuleDTO rule = getRule(req, usersRepository, RIGHT, CREATE);
            Tag tag = TagService.createTag(
                    req,
                    tagsRepository,
                    usersRepository,
                    rule
            );
            return new SuccessResponse<>(MSG_CREATED, tag);
        }, transformer);
        // Route for get tag document by tag id & user id
        get("/owner/:user_id/tag/:tag_id", (req, res) -> {
            RuleDTO rule = getRule(req, usersRepository, RIGHT, READ);
            Tag tag = TagService.getTagById(
                    req,
                    tagsRepository,
                    rule
            );
            return new SuccessResponse<>(MSG_ENTITY, tag);
        }, transformer);
        // Route for update tag document
        put("/owner/:user_id/tag/:tag_id", (req, res) -> {
            RuleDTO rule = getRule(req, usersRepository, RIGHT, UPDATE);
            Tag tag = TagService.updateTag(
                    req,
                    tagsRepository,
                    rule
            );
            return new SuccessResponse<>(MSG_UPDATED, tag);
        }, transformer);
        delete("/owner/:user_id/tag/:tag_id", (req, res) -> {
            RuleDTO rule = getRule(req, usersRepository, RIGHT, DELETE);
            Tag tag = TagService.deleteTag(
                    req,
                    tagsRepository,
                    rule
            );
            return new SuccessResponse<>(MSG_DELETED, tag);
        }, transformer);
    }
}
