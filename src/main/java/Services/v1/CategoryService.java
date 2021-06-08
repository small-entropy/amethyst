package Services.v1;

import DataTransferObjects.RuleDTO;
import Exceptions.AccessException;
import Exceptions.DataException;
import Models.Standalones.Category;
import Services.core.CoreCategoryService;
import Sources.CatalogsSource;
import Sources.CategoriesSource;
import Sources.UsersSource;
import Utils.common.Comparator;
import Utils.v1.RightManager;
import java.util.List;
import spark.Request;

/**
 * Class for work with categories documents
 * @version 1
 * @author small-entropy
 */
public class CategoryService extends CoreCategoryService {
    
    /** Property with public excludes fields */
    private static final String[] PUBLIC_EXCLUDES = new String[] { "status", "owner", "version" };
    /** Property with private excludes fields */
    private static final String[] PRIVATE_EXCLUDES = new String[] { "status",  "version"};
    
    /**
     * Method for get exludes fields by rule * request
     * @param request Spark request object
     * @param rule rule data transfer object
     * @return excludes fields
     */
    private static String[] getExcludes(Request request, RuleDTO rule) {
        return RightManager.getExcludes(
                request, 
                rule, 
                PUBLIC_EXCLUDES, 
                PRIVATE_EXCLUDES
        );
    }
    
    /**
     * Method fot get excludes fields by rule
     * @param isTrusted trusted param
     * @param rule rule data transfer obejct
     * @return excludes fields
     */
    private static String[] getExludesByRule(boolean isTrusted, RuleDTO rule) {
        return RightManager.getExludesByRule(
                isTrusted, 
                rule, 
                PUBLIC_EXCLUDES, 
                PRIVATE_EXCLUDES
        );
    }
    
    /**
     * Methodo for get user categories
     * @param request Spark request object
     * @param categoriesSource datastore source for categories collections
     * @param rule rule data transfer object
     * @return list of categories
     * @throws DataException throw if categories not founded
     */
    public static List<Category> getCategoriesByUser(
            Request request, 
            CategoriesSource categoriesSource,
            RuleDTO rule
    ) throws DataException {
        String[] excludes = getExcludes(request, rule);
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
            String idParam = request.params(PARAM_USER_ID);
            String catalogIdParam = request.params(PARAM_CATEGORY_ID);
            Category category = createCategory(idParam, catalogIdParam, request, categoriesSource, catalogsSource, usersSource);
            String[] exludes = getExludesByRule(isTrusted, rule);
            return getCategoryByDocument(category, categoriesSource, exludes);
        } else {
            Error error = new Error("Has no access to create catalog");
            throw new AccessException("CanNotCreate", error);
        }
    } 

    /**
     * Method for get categories by catalog
     * @param request Spark request object
     * @param categoriesSource datastore source for categories collection
     * @param rule rule data transfer object
     * @return dounded categories
     * @throws DataException  throw if categories ca not be found
     */
    public static List<Category> getCatalogCategories(
            Request request, 
            CategoriesSource categoriesSource, 
            RuleDTO rule
    ) throws DataException {
        String[] excludes = getExcludes(request, rule);
        String catalogIdParam = request.params(PARAM_CATEGORY_ID);
        var categories = getCategoriesByCatalogId(
                request,
                catalogIdParam,
                categoriesSource, 
                excludes
        );
        if (categories != null && !categories.isEmpty()) {
            return categories;
        } else {
            Error error = new Error("Can not find categories in catalog");
            throw new DataException("NotFound", error);
        }
    }
    
    /**
     * Method for get categories documents list
     * @param request Spark request object
     * @param categoriesSource datastore source for categories collection
     * @param rule rule data transfer object
     * @return categories list
     * @throws DataException throw if can not found categories documents
     */
    public static List<Category> getCategoriesList(
            Request request, 
            CategoriesSource categoriesSource, 
            RuleDTO rule
    ) throws DataException {
        String[] excludes = getExcludes(request, rule);
        var categories = getList(
                request,
                categoriesSource,
                excludes
        );
        if (categories != null && !categories.isEmpty()) {
            return categories;
        } else {
            Error error = new Error("Can not find categories in catalog");
            throw new DataException("NotFound", error);
        }
    }
    
    /**
     * Mehtod for get category by requst params (ID)
     * @param request Spark request object
     * @param categoriesSource datastore source for categories collection
     * @param rule rule data transfer object
     * @return category document
     * @throws DataException throw if category document not found
     */
    public static Category getCategoryById(Request request, CategoriesSource categoriesSource, RuleDTO rule) throws DataException {
        String[] exludes = getExcludes(request, rule);
        String categoryIdParam = request.params(PARAM_CATEGORY_ID);
        var category = getCategoryById(categoryIdParam, categoriesSource, exludes);
        if (category != null) {
            return category;
        } else {
            Error error = new Error("Can not find categories in catalog");
            throw new DataException("NotFound", error);
        }
    }

    public static Category updateCategory(
            Request request, 
            CategoriesSource categoriesSource, 
            RuleDTO rule
    ) throws AccessException, DataException {
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        boolean hasAccess = (isTrusted) ? rule.isMyPrivate() : rule.isOtherPrivate();
        if (hasAccess) {
            String ownerIdParam = request.params(PARAM_USER_ID);
            String categoryIdParam = request.params(PARAM_CATEGORY_ID);
            Category category = updateCategory(
                    ownerIdParam, 
                    categoryIdParam,
                    request,
                    categoriesSource
            );
            String[] excludes = getExludesByRule(isTrusted, rule);
            return getCategoryByDocument(category, categoriesSource, excludes);
        } else {
            Error error = new Error("Has no access to update cateogry document");
            throw new AccessException("CanNotUpdate", error);
        }
    }
    
    public static Category deleteCategory(
            Request request,
            CategoriesSource categoriesSource,
            RuleDTO rule
    ) throws AccessException, DataException {
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        boolean hasAccess = (isTrusted) ? rule.isMyGlobal() : rule.isOtherGlobal();
        if (hasAccess) {
            String ownerIdParam = request.params(PARAM_USER_ID);
            String categoryIdParam = request.params(PARAM_CATEGORY_ID);
            return deleteCategory(ownerIdParam, categoryIdParam, categoriesSource);
        } else {
            Error error = new Error("Has no access to delete category document");
            throw new AccessException("CanNotDelete", error);
        }
    }
}
