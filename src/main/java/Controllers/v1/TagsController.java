package Controllers.v1;

import Controllers.base.BaseTagsController;
import Models.Standalones.Tag;
import Responses.SuccessResponse;
import Transformers.JsonTransformer;
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
        get("", (req, res) -> {
            List<Tag> tags = TagService.getTagsList();
            return new SuccessResponse<>(MSG_LIST, tags);
        }, transformer);
        get("/owner/:user_id", (req, res) -> {
            List<Tag> tags = TagService.getTagsByOwnerId();
            return new SuccessResponse<>(MSG_LIST, tags);
        }, transformer);
        post("/owner/:user_id", (req, res) -> {
            Tag tag = TagService.createTag();
            return new SuccessResponse<>(MSG_CREATED, tag);
        }, transformer);
        get("/owner/:user_id/tag/:tag_id", (req, res) -> {
            Tag tag = TagService.getTegById();
            return new SuccessResponse<>(MSG_ENTITY, tag);
        }, transformer);
        put("/owner/:user_id/tag/:tag_id", (req, res) -> {
            Tag tag = TagService.updateTag();
            return new SuccessResponse<>(MSG_UPDATED, tag);
        }, transformer);
        delete("/owner/:user_id/tag/:tag_id", (req, res) -> {
            Tag tag = TagService.deleteTag();
            return new SuccessResponse<>(MSG_DELETED, tag);
        }, transformer);
    }
}
