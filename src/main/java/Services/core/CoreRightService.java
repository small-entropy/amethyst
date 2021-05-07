package Services.core;

import Exceptions.DataException;
import Models.UserRight;
import Sources.RightsSource;
import Utils.constants.DefaultRights;
import Utils.constants.UsersParams;
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
    protected static List<UserRight> getDefaultRightList() {
        UserRight usersRight= new UserRight(DefaultRights.USERS.getName());
        UserRight catalogsRight = new UserRight(DefaultRights.CATALOGS.getName());
        return Arrays.asList(usersRight, catalogsRight);
    }

    /**
     * Method for get user right list
     * @param request Spark request object
     * @param source source for work with users collection
     * @return list of user rights
     * @throws DataException throw if can not found user or rights
     */
    protected static List<UserRight> getUserRights(Request request, RightsSource source) throws DataException {
        String idPaString = request.params(UsersParams.ID.getName());
        return source.getList(idPaString);
    }
    
    /**
     * MEthod for return user rights by request from source
     * @param request Spark request object
     * @param source user rights source
     * @return user right document
     * @throws DataException throw if can not found user or right
     */
    protected static UserRight getUserRightById(Request request, RightsSource source) throws DataException {
       String rightIdParam = request.params(UsersParams.RIGHT_ID.getName());
       String idParam = request.params(UsersParams.ID.getName());
       return source.getRightByIdParam(rightIdParam, idParam);
    }
}
