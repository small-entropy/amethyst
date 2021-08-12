package synthwave.services.v1.categories;

import platform.dto.RuleDTO;
import platform.exceptions.DataException;
import synthwave.models.mongodb.standalones.Category;
import synthwave.services.core.categories.CoreCategoryService;
import synthwave.utils.helpers.Comparator;
import platform.utils.helpers.ParamsManager;
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
    
    @Override
    public List<Category> getEntitiesListByOwner(
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
    
    @Override
    public Category createEntity(
            Request request, 
            String right,
            String action
    ) throws DataException {
        RuleDTO rule = getRule(request, right, action);
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        ObjectId userId = ParamsManager.getUserId(request);
        ObjectId catalogId = ParamsManager.getCatalogId(request);
        Category category = createCategory(userId, catalogId, request);
        String[] exludes = getExcludes(isTrusted, rule);
        return getCategoryByDocument(category, exludes);
    } 

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
    
    @Override
    public List<Category> getEntitiesList(
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
    
    @Override
    public Category getEntityByIdByOwner(
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

    @Override
    public Category updateEntity(
            Request request,  
            String right,
            String action
    ) throws DataException {
        RuleDTO rule = getRule(request, right, action);
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        ObjectId userId = ParamsManager.getUserId(request);
        ObjectId categoryId = ParamsManager.getCategoryId(request);
        Category category = updateCategory(userId, categoryId, request);
        String[] excludes = getExcludes(isTrusted, rule);
        return getCategoryByDocument(category, excludes);
    }
    
    @Override
    public Category deleteEntity(
            Request request,
            String right,
            String action
    ) throws DataException {
        ObjectId userId = ParamsManager.getUserId(request);
        ObjectId categoryId = ParamsManager.getCategoryId(request);
        return deleteCategory(userId, categoryId);
    }
}
