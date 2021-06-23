package Services.core;

import DataTransferObjects.v1.UserRightDTO;
import Exceptions.DataException;
import Models.Embeddeds.EmbeddedRight;
import Repositories.v1.RightsRepository;
import Utils.common.ParamsManager;
import Utils.constants.DefaultRights;
import java.util.Arrays;
import java.util.List;
import org.bson.types.ObjectId;
import spark.Request;

/**
 * Abstract class with main methods for work with user rights
 * @author small-entropy
 */
public abstract class CoreRightService {

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
    protected static List<EmbeddedRight> getUserRights(
            Request request, 
            RightsRepository rightsRepository
    ) throws DataException {
        ObjectId userId = ParamsManager.getUserId(request);
        return rightsRepository.getList(userId);
    }
    
    /**
     * MEthod for return user rights by request from source
     * @param request Spark request object
     * @param rightsRepository user rights source
     * @return user right document
     * @throws DataException throw if can not found user or right
     */
    protected static EmbeddedRight getUserRightById(
            Request request, 
            RightsRepository rightsRepository
    ) throws DataException {
       ObjectId rightId = ParamsManager.getRightId(request);
       ObjectId userId = ParamsManager.getUserId(request);
       return rightsRepository.getRightByIdParam(rightId, userId);
    }
    
    protected static EmbeddedRight createUserRight(
            Request request, 
            RightsRepository rightsRepository
    ) throws DataException {
        ObjectId userId = ParamsManager.getUserId(request);
        UserRightDTO rightDTO = UserRightDTO.build(request, UserRightDTO.class);
        return rightsRepository.createUserRight(userId, rightDTO);
    }
    
    protected static List<EmbeddedRight> deleteRights(
            Request request, 
            RightsRepository rightsRepository
    ) throws DataException {
        ObjectId userId = ParamsManager.getUserId(request);
        ObjectId rightId = ParamsManager.getRightId(request);
        return rightsRepository.removeRight(rightId, userId);
    }
    
    protected static EmbeddedRight updateRight(
            Request request, 
            RightsRepository rightsRepository
    ) throws DataException {
        ObjectId userId = ParamsManager.getUserId(request);
        ObjectId rightId = ParamsManager.getRightId(request);
        UserRightDTO userRightDTO = UserRightDTO.build(
                request, 
                UserRightDTO.class
        ); 
        return rightsRepository.updateUserRight(
                rightId, 
                userId, 
                userRightDTO
        );
    }
}
