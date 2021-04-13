package Utils;

import DTO.RuleDTO;
import Models.User;
import Models.UserRight;


/**
 * @author entropy
 * Class for checks rights
 */
public class RightManager {
    /**
     * Method for get right from user document
     * @param name  name of collection
     * @param user user document
     * @return right document
     */
    private static UserRight getRight(String name, User user) {
        UserRight result = null;
        for (UserRight right : user.getRights()) {
            if (right.getCollection().equals(name)) {
                result = right;
            }
        }
        return result;
    }

    private static String getRule(UserRight right, String action) {
        return switch (action) {
            case "create" -> right.getCreate();
            case "read" -> right.getRead();
            case "update" -> right.getUpdate();
            case "delete" -> right.getDelete();
            default -> "000000";
        };
    }


    public static RuleDTO getRuleObject(User user, String rightName, String ruleName) {
        UserRight right = RightManager.getRight(rightName, user);
        String rule = RightManager.getRule(right, ruleName);
        return new RuleDTO(rule);
    }
}
