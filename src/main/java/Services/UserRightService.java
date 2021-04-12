package Services;

import Models.UserRight;

import java.util.Arrays;
import java.util.List;

public class UserRightService {

    /**
     * Method for get default list of rights
     * @return list of rights
     */
    public static List<UserRight> getDefaultRightList() {
        UserRight usersRight = new UserRight("users");
        UserRight catalogsRight = new UserRight("catalog");
        return Arrays.asList(usersRight, catalogsRight);
    }
}
