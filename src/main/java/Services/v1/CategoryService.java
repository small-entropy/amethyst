package Services.v1;

import DataTransferObjects.v1.RuleDTO;
import Exceptions.AccessException;
import Exceptions.DataException;
import Models.Standalones.Category;
import Services.core.CoreCategoryService;
import Repositories.v1.CatalogsRepository;
import Repositories.v1.CategoriesRepository;
import Repositories.v1.UsersRepository;
import Utils.common.Comparator;
import Utils.common.ParamsManager;
import java.util.List;
import org.bson.types.ObjectId;
import spark.Request;

/**
 * Class for work with categories documents
 * @version 1
 * @author small-entropy
 */
public class CategoryService extends CoreCategoryService {
    
    /** Property with public excludes fields */
    private static final String[] PUBLIC_EXCLUDES = new String[] { 
        "owner", 
        "version",
        "status"
    };
    /** Property with private excludes fields */
    private static final String[] PRIVATE_EXCLUDES = new String[] { 
        "version",
        "status"
    };
    
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
            CategoriesRepository categoriesSource,
            RuleDTO rule
    ) throws DataException {
        String[] excludes = getExcludes(
                request, 
                rule,
                PUBLIC_EXCLUDES,
                PRIVATE_EXCLUDES
        );
        var categories = getCategoriesByRequestForUser(
                request, 
                categoriesSource, 
                excludes
        );
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
            CategoriesRepository categoriesSource, 
            CatalogsRepository catalogsSource, 
            UsersRepository usersSource,
            RuleDTO rule
    ) throws AccessException, DataException {
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        boolean hasAccess = (isTrusted) 
                ? rule.isMyGlobal() 
                : rule.isOtherGlobal();
        if (hasAccess) {
            ObjectId userId = ParamsManager.getUserId(request);
            ObjectId catalogId = ParamsManager.getCatalogId(request);
            Category category = createCategory(
                    userId, 
                    catalogId, 
                    request, 
                    categoriesSource, 
                    catalogsSource, usersSource
            );
            String[] exludes = getExcludes(
                    isTrusted, 
                    rule,
                    PUBLIC_EXCLUDES,
                    PRIVATE_EXCLUDES
            );
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
            CategoriesRepository categoriesSource, 
            RuleDTO rule
    ) throws DataException {
        String[] excludes = getExcludes(
                request, 
                rule,
                PUBLIC_EXCLUDES,
                PRIVATE_EXCLUDES
        );
        ObjectId catalogId = ParamsManager.getCatalogId(request);
        var categories = getCategoriesByCatalogId(
                request,
                catalogId,
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
            CategoriesRepository categoriesSource, 
            RuleDTO rule
    ) throws DataException {
        String[] excludes = getExcludes(
                request, 
                rule,
                PUBLIC_EXCLUDES,
                PRIVATE_EXCLUDES
        );
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
    public static Category getCategoryById(
            Request request, 
            CategoriesRepository categoriesSource, 
            RuleDTO rule
    ) throws DataException {
        String[] exludes = getExcludes(
                request, 
                rule,
                PUBLIC_EXCLUDES,
                PRIVATE_EXCLUDES
        );
        ObjectId categoryId = ParamsManager.getCategoryId(request);
        var category = getCategoryById(
                categoryId, 
                categoriesSource, 
                exludes
        );
        if (category != null) {
            return category;
        } else {
            Error error = new Error("Can not find categories in catalog");
            throw new DataException("NotFound", error);
        }
    }

    public static Category updateCategory(
            Request request, 
            CategoriesRepository categoriesSource, 
            RuleDTO rule
    ) throws AccessException, DataException {
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        boolean hasAccess = (isTrusted) ? rule.isMyPrivate() : rule.isOtherPrivate();
        if (hasAccess) {
            ObjectId userId = ParamsManager.getUserId(request);
            ObjectId categoryId = ParamsManager.getCategoryId(request);
            Category category = updateCategory(
                    userId, 
                    categoryId,
                    request,
                    categoriesSource
            );
            String[] excludes = getExcludes(
                    isTrusted, 
                    rule,
                    PUBLIC_EXCLUDES,
                    PRIVATE_EXCLUDES
            );
            return getCategoryByDocument(category, categoriesSource, excludes);
        } else {
            Error error = new Error("Has no access to update cateogry document");
            throw new AccessException("CanNotUpdate", error);
        }
    }
    
    public static Category deleteCategory(
            Request request,
            CategoriesRepository categoriesSource,
            RuleDTO rule
    ) throws AccessException, DataException {
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        boolean hasAccess = (isTrusted) ? rule.isMyGlobal() : rule.isOtherGlobal();
        if (hasAccess) {
            ObjectId userId = ParamsManager.getUserId(request);
            ObjectId categoryId = ParamsManager.getCategoryId(request);
            return deleteCategory(
                    userId, 
                    categoryId, 
                    categoriesSource
            );
        } else {
            Error error = new Error("Has no access to delete category document");
            throw new AccessException("CanNotDelete", error);
        }
    }
}
