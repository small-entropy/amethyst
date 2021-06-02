package Controllers.core;

import DataTransferObjects.RuleDTO;
import Sources.UsersSource;
import Utils.constants.DefaultActions;
import Utils.v1.RightManager;
import spark.Request;

/**
 * Core class for server controller
 * @author small-entropy
 */
public class AbstractController {
    
    /** Property with name of create action */
    public static final String CREATE = DefaultActions.CREATE.getName();
    /** Property with name of read action */
    public static final String READ = DefaultActions.READ.getName();
    /** Property with name of update action */
    public static final String UPDATE = DefaultActions.UPDATE.getName();
    /** Property with name of delete action */
    public static final String DELETE = DefaultActions.DELETE.getName();
    
    /**
     * Method for get rule data transfer object
     * @param request Spark reqeust object
     * @param usersSource users datastore source
     * @param right right name
     * @param action action name
     * @return 
     */
    protected static RuleDTO getRule(
            Request request,
            UsersSource usersSource,
            String right,
            String action
    ) {
        return RightManager.getRuleByRequest_Token(
                request,
                usersSource,
                right,
                action
        );
    }
}
