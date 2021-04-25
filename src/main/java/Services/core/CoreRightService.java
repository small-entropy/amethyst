package Services.core;

import Models.User;
import Models.UserRight;
import Utils.constants.DefaultRights;
import Utils.constants.UsersParams;
import dev.morphia.Datastore;
import spark.Request;

import java.util.Arrays;
import java.util.List;

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
     * @param datastore Morphia datastore (connection) object
     * @return list of user rights
     */
    protected static List<UserRight> getUserRights(Request request, Datastore datastore) {
        User user = CoreUserService.getUserById(request.params(UsersParams.ID.getName()), datastore);
        return (user.getRights() != null) ? user.getRights() : null;
    }
}
