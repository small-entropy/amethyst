package platform.controllers;

import platform.constants.DefaultActions;
import platform.request.HeadersUtils;
import synthwave.dto.v1.RuleDTO;
import synthwave.repositories.mongodb.v1.UsersRepository;
import platform.utils.access.v1.RightManager;
import spark.Request;
import spark.Response;

/**
 * Core class for server controller
 * @author small-entropy
 */
public class BaseController  {
    /** Property with name of create action */
    public static final String CREATE = DefaultActions.CREATE.getName();
    /** Property with name of read action */
    public static final String READ = DefaultActions.READ.getName();
    /** Property with name of update action */
    public static final String UPDATE = DefaultActions.UPDATE.getName();
    /** Property with name of delete action */
    public static final String DELETE = DefaultActions.DELETE.getName();
    
    /**
     * Method for set authorization headers in response
     * @param response Spark response object
     * @param token user json web token
     */
    protected static void setAuthHeaders(Response response, String token) {
        response.header(
                HeadersUtils.getAuthHeaderField(),
                HeadersUtils.getAuthHeaderValue(token)
        );
    }
        
    /**
     * Method for get rule data transfer object
     * @param request Spark reqeust object
     * @param usersRepository users datastore source
     * @param right right name
     * @param action action name
     * @return 
     */
    protected static RuleDTO getRule(
            Request request,
            UsersRepository usersRepository,
            String right,
            String action
    ) {
        return RightManager.getRuleByRequest_Token(
                request,
                usersRepository,
                right,
                action
        );
    }
}
