package Services.v1;

import DataTransferObjects.RuleDTO;
import Exceptions.AccessException;
import Exceptions.DataException;
import Models.Category;
import Services.core.CoreCategoryService;
import Sources.CatalogsSource;
import Sources.CategoriesSource;
import Sources.UsersSource;
import Utils.common.Comparator;
import Utils.constants.RequestParams;
import spark.Request;

/**
 * Class for work with categories documents
 * @version 1
 * @author small-entropy
 */
public class CategoryService extends CoreCategoryService {
    private static final String[] PUBLIC_EXCLUDES = new String[] {};
    private static final String[] PRIVATE_EXCLUDES = new String[] {};
    private static final String[] GLOBAL_EXLUDES = new String[] {};
    
    /**
     * Method fot get exlude fields by rule & request
     * @param request Spark request object
     * @param rule rule data transfer object
     * @return exclude fields
     */
    private static String[] getExudes(Request request, RuleDTO rule) {
        String[] exludes;
        if (rule != null) {
            boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        
            if (isTrusted) {
                if (rule.isMyGlobal()) {
                    exludes = GLOBAL_EXLUDES;
                } else if (rule.isMyPrivate()) {
                    exludes = PRIVATE_EXCLUDES;
                } else {
                    exludes = PUBLIC_EXCLUDES;
                }
            } else {
                if (rule.isOtherGlobal()) {
                    exludes = GLOBAL_EXLUDES;
                } else if (rule.isOtherPrivate()) {
                    exludes = PRIVATE_EXCLUDES;
                } else {
                    exludes = PUBLIC_EXCLUDES;
                }
            }
        } else {
            exludes = PUBLIC_EXCLUDES;
        }
        return exludes;
    }
    
    public static Category createCategory(
            Request request, 
            CategoriesSource categoriesSource, 
            CatalogsSource catalogsSource, 
            UsersSource usersSource,
            RuleDTO rule
    ) throws AccessException, DataException {
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        boolean hasAccess = (isTrusted) ? rule.isMyGlobal() : rule.isOtherGlobal();
        if (hasAccess) {
            String idParam = request.params(RequestParams.USER_ID.getName());
            String catalogIdParam = request.params(RequestParams.CATALOG_ID.getName());
            Category category = createCategory(idParam, catalogIdParam, request, categoriesSource, catalogsSource, usersSource);
            String[] exludes = getExudes(request, rule);
            return null;
        } else {
            Error error = new Error("Has no access to create catalog");
            throw new AccessException("CanNotCreate", error);
        }
    } 
}
