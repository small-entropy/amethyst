package Controllers.core.base;

import Utils.common.HeadersUtils;
import Utils.constants.DefaultActions;
import spark.Response;

/**
 * Base class for create controllers
 * @author small-entopy
 */
public class BaseController {
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
}
