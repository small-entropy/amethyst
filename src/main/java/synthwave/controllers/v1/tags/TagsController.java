package synthwave.controllers.v1.tags;

import synthwave.controllers.base.RESTController;
import synthwave.controllers.messages.TagsMessages;
import synthwave.models.mongodb.standalones.Tag;
import synthwave.repositories.mongodb.v1.TagsRepository;
import synthwave.services.v1.tags.TagService;
import platform.constants.DefaultRights;
import platform.utils.transformers.JsonTransformer;
import dev.morphia.Datastore;

/**
 * Class for work with tags collection
 * @author small-entropy
 */
public class TagsController
	extends RESTController<Tag, TagsRepository, TagService> {
	
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
				DefaultRights.TAGS.getName(),
				null,
				"",
				"/owner/:user_id/tag/:tag_id",
				"/owner/:user_id",
				true,
				true,
				true,
				true,
				true,
				TagsMessages.CREATED.getMessage(),
				TagsMessages.LIST.getMessage(),
				TagsMessages.ENTITY.getMessage(), 
				TagsMessages.UPDATED.getMessage(), 
				TagsMessages.DELETED.getMessage()
		);
	}
}
