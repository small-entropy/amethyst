package synthwave.controllers.v1;

import synthwave.controllers.messages.TagsMessages;
import synthwave.models.mongodb.standalones.Tag;
import synthwave.services.v1.tags.TagService;
import platform.constants.DefaultRights;
import platform.controllers.BaseController;
import platform.utils.responses.SuccessResponse;
import platform.utils.transformers.JsonTransformer;
import dev.morphia.Datastore;
import static spark.Spark.*;

import java.util.List;

/**
 * Class for work with tags collection
 * @author small-entropy
 */
public class TagsController
	extends BaseController<TagService, JsonTransformer> {
	
	/**
	 * Default tag controller constructor. Create instance
	 * by datastore & transformer
	 * @param datastore Morphia datastore object
	 * @param transformer response transformer
	 */
	public TagsController(
			Datastore datastore,
			JsonTransformer transformer
	) {
		super(
				new TagService(datastore),
				transformer,
				DefaultRights.TAGS.getName()
		);
	}
	
	/**
	 * Message for get tags list
	 */
	protected void getTagsListRoute() {
		get("", (request, response) -> {
            List<Tag> tags = getService().getTagsList(
            		request, 
            		getRight(), 
            		getReadActionName(), 
            		false
            );
            return new SuccessResponse<>(
            		TagsMessages.LIST.getMessage(), 
            		tags
            );
        }, getTransformer());
	}
	
	/**
	 * Method for get tags list by owner id
	 */
	protected void getTagListByOwnerIdRoute() {
		get("/owner/:user_id", (request, response) -> {
            List<Tag> tags = getService().getTagsList(
            		request, 
            		getRight(), 
            		getReadActionName(), 
            		true
            );
            return new SuccessResponse<>(
            		TagsMessages.LIST.getMessage(), 
            		tags
            );
        }, getTransformer());
	}
	
	/**
	 * Method for create tag entity
	 */
	protected void createTagRoute() {
		post("/owner/:user_id", (request, response) -> {
            Tag tag = getService().createTag(
            		request, 
            		getRight(), 
            		getCreateActionName()
            );
            return new SuccessResponse<>(
            		TagsMessages.CREATED.getMessage(), 
            		tag
            );
        }, getTransformer());
	}
	
	/**
	 * Get tag entity by owner id & tag id
	 */
	protected void getTagByOwnerIdAndIdRoute() {
        get("/owner/:user_id/tag/:tag_id", (request, response) -> {
            Tag tag = getService().getTagById(
            		request, 
            		getRight(), 
            		getReadActionName()
            );
            return new SuccessResponse<>(
            		TagsMessages.ENTITY.getMessage(), 
            		tag
            );
        }, getTransformer());
	}
	
	/**
	 * Method for update tag entity
	 */
	protected void updateTagRoute() {
		put("/owner/:user_id/tag/:tag_id", (request, response) -> {
            Tag tag = getService().updateTag(
            		request, 
            		getRight(), 
            		getUpdateActionName()
            );
            return new SuccessResponse<>(
            		TagsMessages.UPDATED.getMessage(), 
            		tag
            );
        }, getTransformer());
	}
	
	/**
	 * Method for deactivate tag entity
	 */
	protected void deleteTagRoute() {
        delete("/owner/:user_id/tag/:tag_id", (request, response) -> {
            Tag tag = getService().deleteTag(
            		request, 
            		getRight(), 
            		getDeleteActionName()
            );
            return new SuccessResponse<>(
            		TagsMessages.DELETED.getMessage(), 
            		tag
            );
        }, getTransformer());
	}
	
	/**
	 * Method for register routes for tags data
	 */
	@Override
    public void register() {
		createTagRoute();
		getTagsListRoute();
		getTagListByOwnerIdRoute();
		getTagByOwnerIdAndIdRoute();
		updateTagRoute();
    }
}
