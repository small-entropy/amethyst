/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Services.core;

import DataTransferObjects.CategoryDTO;
import Exceptions.DataException;
import Filters.CategoriesFilter;
import Models.Standalones.Catalog;
import Models.Standalones.Category;
import Models.Standalones.User;
import Repositories.v1.CatalogsRepository;
import Repositories.v1.CategoriesRepository;
import Repositories.v1.UsersRepository;
import Utils.constants.ListConstants;
import Utils.constants.QueryParams;
import Utils.constants.RequestParams;
import com.google.gson.Gson;
import java.util.List;
import org.bson.types.ObjectId;
import spark.Request;

/**
 *
 * @author igrav
 */
public class CoreCategoryService extends AbstractService {
    
    /**
     * Method for get list of categories by user id from request params
     * @param request Spark request object
     * @param categoriesSource datastore source for categories collection
     * @param excludes arrya of exludes fields
     * @return list of category documents
     */
    protected static List<Category> getCategoriesByRequestForUser(
            Request request,
            CategoriesRepository categoriesSource,
            String[] excludes
    ) {
        String qSkip = request.queryMap().get(QueryParams.SKIP.getKey()).value();
        int skip = (qSkip == null) ? ListConstants.SKIP.getValue() : Integer.parseInt(qSkip);
        // Set limit value from request query
        String qLimit = request.queryMap().get(QueryParams.LIMIT.getKey()).value();
        int limit = (qLimit == null) ? ListConstants.LIMIT.getValue() : Integer.parseInt(qLimit);
        // User id
        String idParam = request.params(RequestParams.USER_ID.getName());
        ObjectId id = new ObjectId(idParam);
        
        CategoriesFilter filter = new CategoriesFilter(skip, limit, excludes);
        filter.setOwner(id);
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
        String qSkip = request.queryMap().get(QueryParams.SKIP.getKey()).value();
        int skip = (qSkip == null) ? ListConstants.SKIP.getValue() : Integer.parseInt(qSkip);
        // Set limit value from request query
        String qLimit = request.queryMap().get(QueryParams.LIMIT.getKey()).value();
        int limit = (qLimit == null) ? ListConstants.LIMIT.getValue() : Integer.parseInt(qLimit);
        
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
    protected static Category getCategoryById(ObjectId categoryId, ObjectId ownerId, CategoriesRepository categoriesSource, String[] excludes) {
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
    protected static Category getCategoryByDocument(Category category, CategoriesRepository categoriesSource, String[] excludes) {
        ObjectId ownerId = category.getOwner().getId();
        ObjectId categoryId = category.getId();
        return getCategoryById(categoryId, ownerId, categoriesSource, excludes);
    }
    
    /**
     * Method for create category
     * @param idParam user id as string
     * @param catalogIdParam catalog id as string
     * @param request Spark request object
     * @param categoriesSource datastore source for categories collection
     * @param catalogsSource datastore source for catalogs collection
     * @param usersSource datastore source for users collection
     * @return created category document
     * @throws DataException throw is user or catalog can not be found
     */
    protected static Category createCategory(
            String idParam,
            String catalogIdParam, 
            Request request,
            CategoriesRepository categoriesSource,
            CatalogsRepository catalogsSource,
            UsersRepository usersSource
    ) throws DataException {
        User user = CoreUserService.getUserById(idParam, usersSource);
        Catalog catalog = CoreCatalogService.getCatalogById(catalogIdParam, catalogsSource);
        if (user != null) {
            CategoryDTO categoryDTO = new Gson().fromJson(request.body(), CategoryDTO.class);
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
     * @param idCatalogParam catalog id from request params (as string)
     * @param categoriesSource datastoure source for categories collection
     * @param excludes array of exludes fields
     * @return list of categories documents
     */
    protected static List<Category> getCategoriesByCatalogId(
            Request request, 
            String idCatalogParam,
            CategoriesRepository categoriesSource,
            String[] excludes
    ) {
        String qSkip = request.queryMap().get(QueryParams.SKIP.getKey()).value();
        int skip = (qSkip == null) ? ListConstants.SKIP.getValue() : Integer.parseInt(qSkip);
        // Set limit value from request query
        String qLimit = request.queryMap().get(QueryParams.LIMIT.getKey()).value();
        int limit = (qLimit == null) ? ListConstants.LIMIT.getValue() : Integer.parseInt(qLimit);
        
        ObjectId idCatalog = new ObjectId(idCatalogParam);
        
        CategoriesFilter filter = new CategoriesFilter(skip, limit, excludes);
        filter.setCatalog(idCatalog);
        
        return categoriesSource.findAllByCatalogId(filter);
    }
    
    /**
     * Method for get category document by id from request params
     * @param categoryIdParam catagory ID from request params (as string)
     * @param categoriesSource datastore source for categories collection
     * @param exludes array of excludes fields
     * @return category document
     */
    protected static Category getCategoryById(
            String categoryIdParam, 
            CategoriesRepository categoriesSource,
            String[] exludes
    ) {
        ObjectId catalogId = new ObjectId(categoryIdParam);
        CategoriesFilter filter = new CategoriesFilter(catalogId, exludes);
        return categoriesSource.findOneById(filter);
    }
    
    /**
     * Method for update category document
     * @param ownerIdParam owner id from request params
     * @param categoryIdParam category id from requeset params
     * @param request Spark request objecet
     * @param categoriesSource datastoure source for categories collection
     * @return category document
     * @throws DataException throw if can not find category document
     */
    protected static Category updateCategory(
            String ownerIdParam,
            String categoryIdParam,
            Request request,
            CategoriesRepository categoriesSource
    ) throws DataException {
        ObjectId ownerId = new ObjectId(ownerIdParam);
        ObjectId categoryId = new ObjectId(categoryIdParam);
        CategoryDTO categoryDTO = new Gson().fromJson(request.body(), CategoryDTO.class);
        CategoriesFilter filter = new CategoriesFilter(new String[]{});
        filter.setOwner(ownerId);
        filter.setId(categoryId);
        return categoriesSource.update(categoryDTO, filter);
    }
    
    /**
     * Method for delete (deactivate) category document
     * @param ownerIdParam owner id from request params
     * @param categoryIdParam category id from requeset params
     * @param categoriesSource datastoure source for categories collection
     * @return category document
     * @throws DataException throw if can not found category document
     */
    protected static Category deleteCategory(
            String ownerIdParam,
            String categoryIdParam,
            CategoriesRepository categoriesSource
    ) throws DataException {
        ObjectId ownerId = new ObjectId(ownerIdParam);
        ObjectId categoryId = new ObjectId(categoryIdParam);
        CategoriesFilter filter = new CategoriesFilter(new String[] {});
        filter.setOwner(ownerId);
        filter.setId(categoryId);
        return categoriesSource.deactivated(filter);
    }
}
