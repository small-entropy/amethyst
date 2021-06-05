package Utils.v1;

import DataTransferObjects.RuleDTO;
import Filters.UsersFilter;
import Models.Standalones.User;
import Models.Embeddeds.EmbeddedRight;
import Services.core.CoreUserService;
import Services.v1.UserService;
import Sources.UsersSource;
import Utils.common.Comparator;
import spark.Request;

/**
 * @author entropy
 * Class for checks rights
 */
public class RightManager {
    // Global excludes for get documents for all fields
    public static final String[] GLOBAL_EXCLUDES = new String[] {};
    
    /**
     * Method fo get excludes with default global excludes array
     * @param request Spark request object
     * @param rule rule data transfer object
     * @param publicExcludes public excludes fields
     * @param privateExcludes private excludes fields
     * @return exudes fields
     */
    public static String[] getExcludes(
            Request request,
            RuleDTO rule,
            String[] publicExcludes,
            String[] privateExcludes
    ) {
        return getExcludes(request, rule, GLOBAL_EXCLUDES, publicExcludes, privateExcludes);
    }
   
    /**
     * Method for get exludes fields withour default global exludes fields
     * @param request Spark requeset object
     * @param rule rule data transfer object
     * @param globalExcludes global exclude fields array
     * @param publicExludes public exclude fields array
     * @param privateExcludes private exclude fields array
     * @return exclude fields
     */
    public static String[] getExcludes(
            Request request, 
            RuleDTO rule,
            String[] globalExcludes,
            String[] publicExludes,
            String[] privateExcludes) {
        return getExludesByRule(
                Comparator.id_fromParam_fromToken(request),
                rule,
                globalExcludes,
                publicExludes,
                privateExcludes
        );
    }
    
    public static String[] getExludesByRule(
            boolean isTrusted,
            RuleDTO rule,
            String[] publicExludes,
            String[] privateExcludes
    ) { 
        return getExludesByRule(isTrusted, rule, GLOBAL_EXCLUDES, publicExludes, privateExcludes);
    }
    
    public static String[] getExludesByRule(
            boolean isTrusted,
            RuleDTO rule,
            String[] globalExcludes,
            String[] publicExludes,
            String[] privateExcludes
    ) {
        String[] excludes;
        if (rule != null) {
            if (isTrusted) {
                if (rule.isMyGlobal()) {
                    excludes = globalExcludes;
                } else if (rule.isMyPrivate()) {
                    excludes = privateExcludes;
                } else {
                    excludes = publicExludes;
                }
            } else {
                if (rule.isOtherGlobal()) {
                    excludes = globalExcludes;
                } else if (rule.isOtherPrivate()) {
                    excludes = privateExcludes;
                } else {
                    excludes = publicExludes;
                }
            }
        } else {
            return publicExludes;
        }
        return excludes;
    }

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
            User user = CoreUserService.getUserByToken(request, source, filter);
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
    private static EmbeddedRight getRight(String name, User user) {
        EmbeddedRight result = null;
        if (user != null) {
            for (EmbeddedRight right : user.getRights()) {
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
    private static String getRule(EmbeddedRight right, String action) {
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
        EmbeddedRight right = RightManager.getRight(rightName, user);
        String rule = (right != null) ? RightManager.getRule(right, ruleName) : null;
        return (rule != null) ? new RuleDTO(rule) : null;
    }
}
