package Utils.v1;

import DataTransferObjects.RuleDTO;
import Filters.UsersFilter;
import Models.User;
import Models.UserRight;
import Services.v1.UserService;
import Sources.UsersSource;
import spark.Request;

/**
 * @author entropy
 * Class for checks rights
 */
public class RightManager {

    /**
     * Method for get rule data transfer object by username of user
     * @param request Spark request object
     * @param source
     * @param rightName right name
     * @param ruleName rule name
     * @return rule data transfer object
     */
    public static RuleDTO getRuleByRequest_Username(Request request, UsersSource source, String rightName, String ruleName) {
        try {
            User user = UserService.getUserByUsername(request, source);
            return RightManager.getRuleObject(user, rightName, ruleName);
        } catch (Exception exception) {
            return null;
        }
    }

    /**
     * Method for get rule data transfer object by user token
     * @param request Spark request object
     * @param source
     * @param rightName right name
     * @param ruleName rule name
     * @return rule data transfer object
     */
    public static RuleDTO getRuleByRequest_Token(Request request, UsersSource source, String rightName, String ruleName) {
        try {
            UsersFilter filter = new UsersFilter();
            filter.setExcludes(UserService.ALL_ALLOWED);
            User user = UserService.getUserByToken(request, source, filter);
            return RightManager.getRuleObject(user, rightName, ruleName);
        } catch (Exception exception) {
            return null;
        }
    }

    /**
     * Method for get right from user document
     * @param name  name of collection
     * @param user user document
     * @return right document
     */
    private static UserRight getRight(String name, User user) {
        UserRight result = null;
        if (user != null) {
            for (UserRight right : user.getRights()) {
                if (right.getName().equals(name)) {
                    result = right;
                }
            }
        }
        return result;
    }

    /**
     * Method for get rule from user right by action name
     * @param right user right
     * @param action action name
     * @return rule string
     */
    private static String getRule(UserRight right, String action) {
        return switch (action) {
            case "create" -> right.getCreate();
            case "read" -> right.getRead();
            case "update" -> right.getUpdate();
            case "delete" -> right.getDelete();
            default -> "000000";
        };
    }

    /**
     * Method for create rule data transfer object for user by right name and rule name
     * @param user user document
     * @param rightName right name
     * @param ruleName rule name
     * @return rule data transfer object
     */
    public static RuleDTO getRuleObject(User user, String rightName, String ruleName) {
        UserRight right = RightManager.getRight(rightName, user);
        String rule = (right != null) ? RightManager.getRule(right, ruleName) : null;
        return (rule != null) ? new RuleDTO(rule) : null;
    }
}
