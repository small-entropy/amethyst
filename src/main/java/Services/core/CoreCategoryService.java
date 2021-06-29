package Services.core;

import DataTransferObjects.v1.CategoryDTO;
import Exceptions.DataException;
import Filters.CategoriesFilter;
import Models.Standalones.Catalog;
import Models.Standalones.Category;
import Models.Standalones.User;
import Repositories.v1.CatalogsRepository;
import Repositories.v1.CategoriesRepository;
import Repositories.v1.UsersRepository;
import Utils.common.ParamsManager;
import Utils.common.QueryManager;
import java.util.List;
import org.bson.types.ObjectId;
import spark.Request;

/**
 *
 * @author small-entropy
 */
public class CoreCategoryService extends AbstractService {
    
    /**
     * Method for get list of categories by user id from request params
     * @param request Spark request object
     * @param categoriesSource datastore source for categories collection
     * @param excludes arrya of exludes fields
     * @return list of category documents
     * @throws DataException
     */
    protected static List<Category> getCategoriesByRequestForUser(
            Request request,
            CategoriesRepository categoriesSource,
            String[] excludes
    ) throws DataException {
        int skip = QueryManager.getSkip(request);
        // Set limit value from request query
        int limit = QueryManager.getLimit(request);
        // User id
        ObjectId userId = ParamsManager.getUserId(request);
        
        CategoriesFilter filter = new CategoriesFilter(skip, limit, excludes);
        filter.setOwner(userId);
        return categoriesSource.findAllByOwnerId(filter);
    }
    
    /**
     * Method for get categories list of documents
     * @param request Spark request object
     * @param categoriesSource datastore source for categories documents
     * @param excludes excludes fields
     * @return founded categories list
     */
    protected static List<Category> getList(
            Request request,
            CategoriesRepository categoriesSource,
            String[] excludes
    ) {
        int skip = QueryManager.getSkip(request);
        // Set limit value from request query
        int limit = QueryManager.getLimit(request);
        CategoriesFilter filter = new CategoriesFilter(skip, limit, excludes);
        return  categoriesSource.findAll(filter);
    }
    
    /**
     * Method for get category by id
     * @param categoryId catagory id as ObjectId
     * @param ownerId owner id as ObjectId
     * @param categoriesSource datastore source for categories collection
     * @param excludes exludes field
     * @return category document
     */
    protected static Category getCategoryById(
            ObjectId categoryId, 
            ObjectId ownerId, 
            CategoriesRepository categoriesSource, 
            String[] excludes
    ) {
        CategoriesFilter filter = new CategoriesFilter();
        filter.setId(categoryId);
        filter.setOwner(ownerId);
        filter.setExcludes(excludes);
        return categoriesSource.findOneByOwnerAndId(filter);
    }
    
    /**
     * Method for get category by document
     * @param category category document
     * @param categoriesSource datastore for categories collection
     * @param excludes exludes fields
     * @return category document
     */
    protected static Category getCategoryByDocument(
            Category category, 
            CategoriesRepository categoriesSource, 
            String[] excludes
    ) {
        ObjectId ownerId = category.getOwner().getId();
        ObjectId categoryId = category.getId();
        return getCategoryById(categoryId, ownerId, categoriesSource, excludes);
    }
    
    /**
     * Method for create category
     * @param userId user id
     * @param catalogId catalog id
     * @param request Spark request object
     * @param categoriesSource datastore source for categories collection
     * @param catalogsSource datastore source for catalogs collection
     * @param usersSource datastore source for users collection
     * @return created category document
     * @throws DataException throw is user or catalog can not be found
     */
    protected static Category createCategory(
            ObjectId userId,
            ObjectId catalogId, 
            Request request,
            CategoriesRepository categoriesSource,
            CatalogsRepository catalogsSource,
            UsersRepository usersSource
    ) throws DataException {
        User user = CoreUserService.getUserById(userId, usersSource);
        Catalog catalog = CoreCatalogService.getCatalogById(
                catalogId, 
                catalogsSource
        );
        if (user != null) {
            CategoryDTO categoryDTO = CategoryDTO.build(
                    request, 
                    CategoryDTO.class
            );
            categoryDTO.setCatalog(catalog);
            categoryDTO.setOwner(user);
            return categoriesSource.create(categoryDTO);
        } else {
            Error error = new Error("Can not find user");
            throw new DataException("NotFound", error);
        }
    }
    
    /**
     * Method fot get categories by catalog id fom request params
     * @param request Spark reqeuset object
     * @param catalogId catalog id from request params
     * @param categoriesSource datastoure source for categories collection
     * @param excludes array of exludes fields
     * @return list of categories documents
     */
    protected static List<Category> getCategoriesByCatalogId(
            Request request, 
            ObjectId catalogId,
            CategoriesRepository categoriesSource,
            String[] excludes
    ) {
        int skip = QueryManager.getSkip(request);
        // Set limit value from request query
        int limit = QueryManager.getLimit(request);
        
        CategoriesFilter filter = new CategoriesFilter(skip, limit, excludes);
        filter.setCatalog(catalogId);
        
        return categoriesSource.findAllByCatalogId(filter);
    }
    
    /**
     * Method for get category document by id from request params
     * @param categoryId catagory ID from request params
     * @param categoriesSource datastore source for categories collection
     * @param exludes array of excludes fields
     * @return category document
     */
    protected static Category getCategoryById(
            ObjectId categoryId, 
            CategoriesRepository categoriesSource,
            String[] exludes
    ) {
        CategoriesFilter filter = new CategoriesFilter(categoryId, exludes);
        return categoriesSource.findOneById(filter);
    }
    
    /**
     * Method for update category document
     * @param userId owner id from request params
     * @param categoryId category id from requeset params
     * @param request Spark request objecet
     * @param categoriesSource datastoure source for categories collection
     * @return category document
     * @throws DataException throw if can not find category document
     */
    protected static Category updateCategory(
            ObjectId userId,
            ObjectId categoryId,
            Request request,
            CategoriesRepository categoriesSource
    ) throws DataException {
        CategoryDTO categoryDTO = CategoryDTO.build(request, CategoryDTO.class);
        CategoriesFilter filter = new CategoriesFilter(new String[]{});
        filter.setOwner(userId);
        filter.setId(categoryId);
        return categoriesSource.update(categoryDTO, filter);
    }
    
    /**
     * Method for delete (deactivate) category document
     * @param userId owner id from request params
     * @param categoryId category id from requeset params
     * @param categoriesSource datastoure source for categories collection
     * @return category document
     * @throws DataException throw if can not found category document
     */
    protected static Category deleteCategory(
            ObjectId userId,
            ObjectId categoryId,
            CategoriesRepository categoriesSource
    ) throws DataException {
        CategoriesFilter filter = new CategoriesFilter(new String[] {});
        filter.setOwner(userId);
        filter.setId(categoryId);
        return categoriesSource.deactivate(filter);
    }
}
