package synthwave.services.v1;

import synthwave.dto.v1.RuleDTO;
import platform.exceptions.AccessException;
import platform.exceptions.DataException;
import synthwave.models.mongodb.standalones.Category;
import synthwave.services.core.CoreCategoryService;
import platform.helpers.Comparator;
import platform.request.ParamsManager;
import dev.morphia.Datastore;
import java.util.List;
import org.bson.types.ObjectId;
import spark.Request;

/**
 * Class for work with categories documents
 * @version 1
 * @author small-entropy
 */
public class CategoryService extends CoreCategoryService {
    
    public CategoryService(Datastore datastore) {
        super(
                datastore,
                new String[] {},
                new String[] { "owner", "version", "status" },
                new String[] { "verstion", "status" }
        );
    }
    
    /**
     * Methodo for get user categories
     * @param request Spark request object
     * @return list of categories
     * @throws DataException throw if categories not founded
     */
    public List<Category> getCategoriesByUser(
            Request request, 
            String right,
            String action
    ) throws DataException {
        RuleDTO rule = getRule(request, right, action);
        String[] excludes = getExcludes(request, rule);
        var categories = getCategoriesByRequestForUser(request, excludes);
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
     * @param rule rule data transfer object
     * @return created category document
     * @throws AccessException throw if user has not access for create category document
     * @throws DataException  throw if can not find user or catelog document
     */
    public Category createCategory(
            Request request, 
            String right,
            String action
    ) throws AccessException, DataException {
        RuleDTO rule = getRule(request, right, action);
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        boolean hasAccess = (isTrusted) 
                ? rule.isMyGlobal() 
                : rule.isOtherGlobal();
        if (hasAccess) {
            ObjectId userId = ParamsManager.getUserId(request);
            ObjectId catalogId = ParamsManager.getCatalogId(request);
            Category category = createCategory(userId, catalogId, request);
            String[] exludes = getExcludes(isTrusted, rule);
            return getCategoryByDocument(category, exludes);
        } else {
            Error error = new Error("Has no access to create catalog");
            throw new AccessException("CanNotCreate", error);
        }
    } 

    /**
     * Method for get categories by catalog
     * @param request Spark request object
     * @param rule rule data transfer object
     * @return dounded categories
     * @throws DataException  throw if categories ca not be found
     */
    public List<Category> getCatalogCategories(
            Request request,  
            String right,
            String action
    ) throws DataException {
        RuleDTO rule = getRule(request, right, action);
        String[] excludes = getExcludes(request, rule);
        ObjectId catalogId = ParamsManager.getCatalogId(request);
        var categories = getCategoriesByCatalogId(request, catalogId, excludes);
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
     * @param rule rule data transfer object
     * @return categories list
     * @throws DataException throw if can not found categories documents
     */
    public List<Category> getCategoriesList(
            Request request, 
            String right,
            String action
    ) throws DataException {
        RuleDTO rule = getRule(request, right, action);
        String[] excludes = getExcludes(request, rule);
        var categories = getList(request, excludes);
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
     * @param rule rule data transfer object
     * @return category document
     * @throws DataException throw if category document not found
     */
    public Category getCategoryById(
            Request request, 
            String right,
            String action
    ) throws DataException {
        RuleDTO rule = getRule(request, right, action);
        String[] exludes = getExcludes(request, rule);
        ObjectId categoryId = ParamsManager.getCategoryId(request);
        var category = getCategoryById(categoryId, exludes);
        if (category != null) {
            return category;
        } else {
            Error error = new Error("Can not find categories in catalog");
            throw new DataException("NotFound", error);
        }
    }

    public Category updateCategory(
            Request request,  
            String right,
            String action
    ) throws AccessException, DataException {
        RuleDTO rule = getRule(request, right, action);
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        boolean hasAccess = (isTrusted) 
                ? rule.isMyPrivate() 
                : rule.isOtherPrivate();
        if (hasAccess) {
            ObjectId userId = ParamsManager.getUserId(request);
            ObjectId categoryId = ParamsManager.getCategoryId(request);
            Category category = updateCategory(userId, categoryId, request);
            String[] excludes = getExcludes(isTrusted, rule);
            return getCategoryByDocument(category, excludes);
        } else {
            Error error = new Error("Has no access to update cateogry document");
            throw new AccessException("CanNotUpdate", error);
        }
    }
    
    public Category deleteCategory(
            Request request,
            String right,
            String action
    ) throws AccessException, DataException {
        RuleDTO rule = getRule(request, right, action);
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        boolean hasAccess = (isTrusted) 
                ? rule.isMyGlobal() 
                : rule.isOtherGlobal();
        if (hasAccess) {
            ObjectId userId = ParamsManager.getUserId(request);
            ObjectId categoryId = ParamsManager.getCategoryId(request);
            return deleteCategory(userId, categoryId);
        } else {
            Error error = new Error("Has no access to delete category document");
            throw new AccessException("CanNotDelete", error);
        }
    }
}
