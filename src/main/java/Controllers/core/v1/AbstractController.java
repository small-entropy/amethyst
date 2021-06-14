package Controllers.core.v1;

import Controllers.core.base.BaseController;
import DataTransferObjects.RuleDTO;
import Repositories.v1.UsersRepository;
import Utils.v1.RightManager;
import spark.Request;

/**
 * Core class for server controller
 * @author small-entropy
 * @version 1
 */
public class AbstractController extends BaseController {
    
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
            UsersRepository usersSource,
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
