package Services.core;

import Models.User;
import Models.UserRight;
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
        UserRight usersRight = new UserRight("users_right");
        UserRight catalogsRight = new UserRight("catalogs_right");
        return Arrays.asList(usersRight, catalogsRight);
    }

    /**
     * Method for get user right list
     * @param request Spark request object
     * @param datastore Morphia datastore (connection) object
     * @return list of user rights
     */
    protected static List<UserRight> getUserRights(Request request, Datastore datastore) {
        User user = CoreUserService.getUserById(request.params("id"), datastore);
        return (user.getRights() != null) ? user.getRights() : null;
    }
}
