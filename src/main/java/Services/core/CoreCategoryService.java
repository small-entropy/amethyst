/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Services.core;

import DataTransferObjects.CategoryDTO;
import Exceptions.DataException;
import Filters.CategoriesFilter;
import Models.Catalog;
import Models.Category;
import Models.User;
import Sources.CatalogsSource;
import Sources.CategoriesSource;
import Sources.UsersSource;
import com.google.gson.Gson;
import org.bson.types.ObjectId;
import spark.Request;

/**
 *
 * @author igrav
 */
public class CoreCategoryService {
    
    /**
     * Method for get category by id
     * @param categoryId catagory id as ObjectId
     * @param ownerId owner id as ObjectId
     * @param categoriesSource datastore source for categories collection
     * @param excludes exludes field
     * @return category document
     */
    protected static Category getCategoryById(ObjectId categoryId, ObjectId ownerId, CategoriesSource categoriesSource, String[] excludes) {
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
    protected static Category getCategoryByDocument(Category category, CategoriesSource categoriesSource, String[] excludes) {
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
            CategoriesSource categoriesSource,
            CatalogsSource catalogsSource,
            UsersSource usersSource
    ) throws DataException {
        User user = CoreUserService.getUserById(idParam, usersSource);
        Catalog catelog = CoreCatalogService.getCatalogById(catalogIdParam, catalogsSource);
        if (user != null) {
            CategoryDTO categoryDTO = new Gson().fromJson(request.body(), CategoryDTO.class);
            categoryDTO.setCatalog(catelog);
            categoryDTO.setOwner(user);
            return categoriesSource.create(categoryDTO);
        } else {
            Error error = new Error("Can not find user");
            throw new DataException("NotFound", error);
        }
    }
}
