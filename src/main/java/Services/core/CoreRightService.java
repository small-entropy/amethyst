package Services.core;

import Models.User;
import Models.UserRight;
import Sources.UsersSource;
import Utils.constants.DefaultRights;
import Utils.constants.UsersParams;
import java.util.Arrays;
import java.util.List;
import spark.Request;

/**
 * 
 * @author entropy
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
     */
    protected static List<UserRight> getUserRights(Request request, UsersSource source) {
        User user = CoreUserService.getUserById(request.params(UsersParams.ID.getName()), source);
        return (user.getRights() != null) ? user.getRights() : null;
    }
}
