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
        List<UserRight> rights = Arrays.asList();
        rights.add(new UserRight("users"));
        rights.add(new UserRight("catalog"));
        return rights;
    }
}
