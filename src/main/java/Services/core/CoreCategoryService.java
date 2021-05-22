/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Services.core;

import DataTransferObjects.CategoryDTO;
import Exceptions.DataException;
import Models.Catalog;
import Models.Category;
import Models.User;
import Sources.CatalogsSource;
import Sources.CategoriesSource;
import Sources.UsersSource;
import com.google.gson.Gson;
import spark.Request;

/**
 *
 * @author igrav
 */
public class CoreCategoryService {
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
