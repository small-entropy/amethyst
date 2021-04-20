package Utils.v1;

import DTO.RuleDTO;
import Models.User;
import Models.UserRight;
import Services.v1.UserService;
import dev.morphia.Datastore;
import spark.Request;

/**
 * @author entropy
 * Class for checks rights
 */
public class RightManager {

    /**
     * Method for get rule data transfer object by username of user
     * @param request Spark request object
     * @param datastore Morphia datastore object
     * @param rightName right name
     * @param ruleName rule name
     * @return rule data transfer object
     */
    public static RuleDTO getRuleByRequest_Username(Request request, Datastore datastore, String rightName, String ruleName) {
        try {
            User user = UserService.getUserByUsername(request, datastore);
            return RightManager.getRuleObject(user, rightName, ruleName);
        } catch (Exception exception) {
            return null;
        }
    }

    /**
     * Method for get rule data transfer object by user token
     * @param request Spark request object
     * @param datastore Morphia datastore
     * @param rightName right name
     * @param ruleName rule name
     * @return rule data transfer object
     */
    public static RuleDTO getRuleByRequest_Token(Request request, Datastore datastore, String rightName, String ruleName) {
        try {
            User user = UserService.getUserByToken(request, datastore);
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
                if (right.getCollection().equals(name)) {
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
