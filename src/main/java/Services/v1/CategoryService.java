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
import Utils.v1.RightManager;
import java.util.List;
import spark.Request;

/**
 * Class for work with categories documents
 * @version 1
 * @author small-entropy
 */
public class CategoryService extends CoreCategoryService {
    
    private static final String[] PUBLIC_EXCLUDES = new String[] { "status", "owner", "version" };
    private static final String[] PRIVATE_EXCLUDES = new String[] { "status",  "version"};
    
    public static List<Category> getCategoriesByUser(
            Request request, 
            CategoriesSource categoriesSource,
            RuleDTO rule
    ) throws DataException {
        String[] excludes = RightManager.getExcludes(request, rule, PUBLIC_EXCLUDES, PRIVATE_EXCLUDES);
        var categories = getCategoriesByRequestForUser(request, categoriesSource, excludes);
        if (categories != null && !categories.isEmpty()) {
            return categories;
        } else {
            Error error = new Error("Can not find user categories by request params");
            throw new DataException("NotFound", error);
        }
    }
    
    /**
     * Method for create category document
     * @param request Spark request object
     * @param categoriesSource datastore source for categories collection
     * @param catalogsSource datastore source for catalogs collection
     * @param usersSource datastore source for users collection
     * @param rule rule data transfer object
     * @return created category document
     * @throws AccessException throw if user has not access for create category document
     * @throws DataException  throw if can not find user or catelog document
     */
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
            String[] exludes = RightManager.getExludesByRule(isTrusted, rule, PUBLIC_EXCLUDES, PRIVATE_EXCLUDES);
            return getCategoryByDocument(category, categoriesSource, exludes);
        } else {
            Error error = new Error("Has no access to create catalog");
            throw new AccessException("CanNotCreate", error);
        }
    } 
}
