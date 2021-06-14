package Services.core;

import DataTransferObjects.UserRightDTO;
import Exceptions.DataException;
import Models.Embeddeds.EmbeddedRight;
import Repositories.v1.RightsRepository;
import Utils.constants.DefaultRights;
import Utils.constants.RequestParams;
import com.google.gson.Gson;
import java.util.Arrays;
import java.util.List;
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
        return Arrays.asList(usersRight, catalogsRight, categoriesRight, productsRight);
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
        String idPaString = request.params(RequestParams.USER_ID.getName());
        return rightsRepository.getList(idPaString);
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
       String rightIdParam = request.params(RequestParams.RIGHT_ID.getName());
       String idParam = request.params(RequestParams.USER_ID.getName());
       return rightsRepository.getRightByIdParam(rightIdParam, idParam);
    }
    
    protected static EmbeddedRight createUserRight(
            Request request, 
            RightsRepository rightsRepository
    ) throws DataException {
        String idParam = request.params(RequestParams.USER_ID.getName());
        UserRightDTO rightDTO = new Gson()
                .fromJson(request.body(), UserRightDTO.class);
        return rightsRepository.createUserRight(idParam, rightDTO);
    }
    
    protected static List<EmbeddedRight> deleteRights(
            Request request, 
            RightsRepository rightsRepository
    ) throws DataException {
        String idParam = request.params(RequestParams.USER_ID.getName());
        String rightIdParam = request.params(RequestParams.RIGHT_ID.getName());
        return rightsRepository.removeRight(rightIdParam, idParam);
    }
    
    protected static EmbeddedRight updateRight(
            Request request, 
            RightsRepository rightsRepository
    ) throws DataException {
        String idParam = request.params(RequestParams.USER_ID.getName());
        String rightIdParam = request.params(RequestParams.RIGHT_ID.getName());
        UserRightDTO userRightDTO = new Gson()
                .fromJson(request.body(), UserRightDTO.class);
        return rightsRepository.updateUserRight(
                rightIdParam, 
                idParam, 
                userRightDTO
        );
    }
}
