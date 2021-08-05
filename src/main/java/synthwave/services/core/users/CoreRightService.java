package synthwave.services.core.users;

import synthwave.dto.UserRightDTO;
import platform.exceptions.DataException;
import synthwave.models.mongodb.embeddeds.EmbeddedRight;
import synthwave.models.mongodb.standalones.User;
import synthwave.repositories.mongodb.v1.RightsRepository;
import synthwave.services.base.BaseDocumentService;
import platform.utils.helpers.ParamsManager;
import platform.constants.DefaultRights;
import dev.morphia.Datastore;
import java.util.Arrays;
import java.util.List;
import org.bson.types.ObjectId;
import spark.Request;

/**
 * Abstract class with main methods for work with user rights
 * @author small-entropy
 */
public abstract class CoreRightService 
        extends BaseDocumentService<User, RightsRepository>{
    
    public CoreRightService(Datastore datastore, List<String> blackList) {
        super(
                datastore,
                new RightsRepository(datastore, blackList),
                new String[] {},
                new String[] {},
                new String[] {}
        );
    }

    /**
     * Method for get default list of rights
     * @return list of rights
     */
    protected static List<EmbeddedRight> getDefaultRightList() {
        EmbeddedRight usersRight= new EmbeddedRight(DefaultRights.USERS.getName());
        EmbeddedRight catalogsRight = new EmbeddedRight(DefaultRights.CATALOGS.getName());
        EmbeddedRight categoriesRight = new EmbeddedRight(DefaultRights.CATEGORIES.getName());
        EmbeddedRight productsRight = new EmbeddedRight(DefaultRights.PRODUCTS.getName());
        EmbeddedRight tagsRight = new EmbeddedRight(DefaultRights.TAGS.getName());
        EmbeddedRight companiesRight = new EmbeddedRight(DefaultRights.COMPANIES.getName());
        return Arrays.asList(
                usersRight, 
                catalogsRight, 
                categoriesRight, 
                productsRight,
                tagsRight,
                companiesRight
        );
    }

    /**
     * Method for get user right list
     * @param request Spark request object
     * @param rightsRepository source for work with users collection
     * @return list of user rights
     * @throws DataException throw if can not found user or rights
     */
    protected List<EmbeddedRight> getUserRights(
            Request request
    ) throws DataException {
        ObjectId userId = ParamsManager.getUserId(request);
        return getRepository().getList(userId);
    }
    
    /**
     * MEthod for return user rights by request from source
     * @param request Spark request object
     * @param rightsRepository user rights source
     * @return user right document
     * @throws DataException throw if can not found user or right
     */
    protected EmbeddedRight getUserRightById(Request request) 
            throws DataException {
       ObjectId rightId = ParamsManager.getRightId(request);
       ObjectId userId = ParamsManager.getUserId(request);
       return getRepository().getRightByIdParam(rightId, userId);
    }
    
    protected EmbeddedRight createUserRight(Request request) 
            throws DataException {
        ObjectId userId = ParamsManager.getUserId(request);
        UserRightDTO rightDTO = UserRightDTO.build(request, UserRightDTO.class);
        return getRepository().createUserRight(userId, rightDTO);
    }
    
    protected List<EmbeddedRight> deleteRights(Request request) 
            throws DataException {
        ObjectId userId = ParamsManager.getUserId(request);
        ObjectId rightId = ParamsManager.getRightId(request);
        return getRepository().removeRight(rightId, userId);
    }
    
    protected EmbeddedRight updateRight(Request request) 
            throws DataException {
        ObjectId userId = ParamsManager.getUserId(request);
        ObjectId rightId = ParamsManager.getRightId(request);
        UserRightDTO userRightDTO = UserRightDTO.build(
                request, 
                UserRightDTO.class
        ); 
        return getRepository().updateUserRight(
                rightId, 
                userId, 
                userRightDTO
        );
    }
}
