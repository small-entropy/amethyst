package Services;

import Exceptions.DataException;
import Exceptions.TokenException;
import Models.User;
import Models.UserRight;
import dev.morphia.Datastore;
import spark.Request;

import java.util.Arrays;
import java.util.List;

/**
 * Class service for work with user right document
 */
public class UserRightService {

    /**
     * Method for get default list of rights
     * @return list of rights
     */
    public static List<UserRight> getDefaultRightList() {
        UserRight usersRight = new UserRight("users_right");
        UserRight catalogsRight = new UserRight("catalogs_right");
        return Arrays.asList(usersRight, catalogsRight);
    }

    public static String updateRight(Request request, Datastore datastore) {
        return "Update user right";
    }

    public static String deleteRight(Request request, Datastore datastore) {
        return "Delete user right";
    }

    public static String createRight(Request request, Datastore datastore) {
        return "Create user right";
    }

    /**
     * Method for get user right list
     * @param request Spark request object
     * @param datastore Morphia datastore (connection) object
     * @return list of user rights
     * @throws DataException exception for errors with data
     * @throws TokenException exception for errors of token
     */
    public static List<UserRight> getUserRights(Request request, Datastore datastore) throws DataException, TokenException {
        User user = UserService.getUserWithTrust(request, datastore);
        return (user.getRights() != null) ? user.getRights() : null;
    }

    /**
     * Method for get user right by id
     * @param request Spark request object
     * @param datastore Morphia datastore (connection) object
     * @return user right document
     * @throws DataException exception for errors with data
     * @throws TokenException exception for errors of token
     */
    public static UserRight getUserRightById(Request request, Datastore datastore) throws DataException, TokenException {
        List<UserRight> rights = UserRightService.getUserRights(request, datastore);
        String rightId = request.params("right_id");
        UserRight result = null;
        if (rights != null && rightId != null) {
            for (UserRight right : rights) {
                if (right.getId().toString().equals(rightId)) {
                    result = right;
                }
            }
        }
        return result;
    }
}
