package Controllers.v1;

import Controllers.base.BaseTagsController;
import Models.Standalones.Tag;
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
        TagService service = new TagService(store);
        // Route for get tags list
        get("", (req, res) -> {
            List<Tag> tags = service.getTagsList(req, RIGHT, READ, false);
            return new SuccessResponse<>(MSG_LIST, tags);
        }, transformer);
        // Route for get tags list by for user by id
        get("/owner/:user_id", (req, res) -> {
            List<Tag> tags = service.getTagsList(req, RIGHT, READ, true);
            return new SuccessResponse<>(MSG_LIST, tags);
        }, transformer);
        // Route fot create tag document
        post("/owner/:user_id", (req, res) -> {
            Tag tag = service.createTag(req, RIGHT, CREATE);
            return new SuccessResponse<>(MSG_CREATED, tag);
        }, transformer);
        // Route for get tag document by tag id & user id
        get("/owner/:user_id/tag/:tag_id", (req, res) -> {
            Tag tag = service.getTagById(req, RIGHT, READ);
            return new SuccessResponse<>(MSG_ENTITY, tag);
        }, transformer);
        // Route for update tag document
        put("/owner/:user_id/tag/:tag_id", (req, res) -> {
            Tag tag = service.updateTag(req, RIGHT, UPDATE);
            return new SuccessResponse<>(MSG_UPDATED, tag);
        }, transformer);
        delete("/owner/:user_id/tag/:tag_id", (req, res) -> {
            Tag tag = service.deleteTag(req, RIGHT, DELETE);
            return new SuccessResponse<>(MSG_DELETED, tag);
        }, transformer);
    }
}
