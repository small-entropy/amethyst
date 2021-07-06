package synthwave.services.core;

import synthwave.dto.v1.CategoryDTO;
import platform.exceptions.DataException;
import synthwave.filters.CategoriesFilter;
import synthwave.models.mongodb.standalones.Catalog;
import synthwave.models.mongodb.standalones.Category;
import synthwave.models.mongodb.standalones.User;
import synthwave.repositories.mongodb.v1.CatalogsRepository;
import synthwave.repositories.mongodb.v1.CategoriesRepository;
import platform.services.BaseDocumentService;
import platform.request.ParamsManager;
import platform.request.QueryManager;
import dev.morphia.Datastore;
import java.util.List;
import org.bson.types.ObjectId;
import spark.Request;

/**
 *
 * @author small-entropy
 */
public abstract class CoreCategoryService 
        extends BaseDocumentService<CategoriesRepository> {
    
    CatalogsRepository catalogsRepository;
    
    public CoreCategoryService(
            Datastore datastore,
            String[] globalExcludes,
            String[] publicExcludes,
            String[] privateExclides
    ) {
        super(
                datastore,
                new CategoriesRepository(datastore),
                globalExcludes,
                publicExcludes,
                privateExclides
        );
        this.catalogsRepository = new CatalogsRepository(datastore);
    }

    public CatalogsRepository getCatalogsRepository() {
        return catalogsRepository;
    }

    public void setCatalogsRepository(CatalogsRepository catalogsRepository) {
        this.catalogsRepository = catalogsRepository;
    }
    
    /**
     * Method for get list of categories by user id from request params
     * @param request Spark request object
     * @param excludes arrya of exludes fields
     * @return list of category documents
     * @throws DataException
     */
    protected List<Category> getCategoriesByRequestForUser(
            Request request,
            String[] excludes
    ) throws DataException {
        int skip = QueryManager.getSkip(request);
        // Set limit value from request query
        int limit = QueryManager.getLimit(request);
        // User id
        ObjectId userId = ParamsManager.getUserId(request);
        
        CategoriesFilter filter = new CategoriesFilter(skip, limit, excludes);
        filter.setOwner(userId);
        return getRepository().findAllByOwnerId(filter);
    }
    
    /**
     * Method for get categories list of documents
     * @param request Spark request object
     * @param excludes excludes fields
     * @return founded categories list
     */
    protected List<Category> getList(
            Request request,
            String[] excludes
    ) {
        int skip = QueryManager.getSkip(request);
        // Set limit value from request query
        int limit = QueryManager.getLimit(request);
        CategoriesFilter filter = new CategoriesFilter(skip, limit, excludes);
        return  getRepository().findAll(filter);
    }
    
    /**
     * Method for get category by id
     * @param categoryId catagory id as ObjectId
     * @param ownerId owner id as ObjectId
     * @param excludes exludes field
     * @return category document
     */
    protected Category getCategoryById(
            ObjectId categoryId, 
            ObjectId ownerId,
            String[] excludes
    ) {
        CategoriesFilter filter = new CategoriesFilter();
        filter.setId(categoryId);
        filter.setOwner(ownerId);
        filter.setExcludes(excludes);
        return getRepository().findOneByOwnerAndId(filter);
    }
    
    /**
     * Method for get category by document
     * @param category category document
     * @param excludes exludes fields
     * @return category document
     */
    protected Category getCategoryByDocument(
            Category category,
            String[] excludes
    ) {
        ObjectId ownerId = category.getOwner().getId();
        ObjectId categoryId = category.getId();
        return getCategoryById(categoryId, ownerId, excludes);
    }
    
    /**
     * Method for create category
     * @param userId user id
     * @param catalogId catalog id
     * @param request Spark request object
     * @return created category document
     * @throws DataException throw is user or catalog can not be found
     */
    protected Category createCategory(
            ObjectId userId,
            ObjectId catalogId, 
            Request request
    ) throws DataException {
        User user = getUserById(userId);
        Catalog catalog = CoreCatalogService.getCatalogById(
                catalogId, 
                getCatalogsRepository()
        );
        if (user != null) {
            CategoryDTO categoryDTO = CategoryDTO.build(
                    request, 
                    CategoryDTO.class
            );
            categoryDTO.setCatalog(catalog);
            categoryDTO.setOwner(user);
            return getRepository().create(categoryDTO);
        } else {
            Error error = new Error("Can not find user");
            throw new DataException("NotFound", error);
        }
    }
    
    /**
     * Method fot get categories by catalog id fom request params
     * @param request Spark reqeuset object
     * @param catalogId catalog id from request params
     * @param excludes array of exludes fields
     * @return list of categories documents
     */
    protected List<Category> getCategoriesByCatalogId(
            Request request, 
            ObjectId catalogId,
            String[] excludes
    ) {
        int skip = QueryManager.getSkip(request);
        // Set limit value from request query
        int limit = QueryManager.getLimit(request);
        
        CategoriesFilter filter = new CategoriesFilter(skip, limit, excludes);
        filter.setCatalog(catalogId);
        
        return getRepository().findAllByCatalogId(filter);
    }
    
    /**
     * Method for get category document by id from request params
     * @param categoryId catagory ID from request params
     * @param exludes array of excludes fields
     * @return category document
     */
    protected Category getCategoryById(
            ObjectId categoryId,
            String[] exludes
    ) {
        CategoriesFilter filter = new CategoriesFilter(categoryId, exludes);
        return getRepository().findOneById(filter);
    }
    
    /**
     * Method for update category document
     * @param userId owner id from request params
     * @param categoryId category id from requeset params
     * @param request Spark request objecet
     * @return category document
     * @throws DataException throw if can not find category document
     */
    protected Category updateCategory(
            ObjectId userId,
            ObjectId categoryId,
            Request request
    ) throws DataException {
        CategoryDTO categoryDTO = CategoryDTO.build(request, CategoryDTO.class);
        CategoriesFilter filter = new CategoriesFilter(
                this.getGlobalExcludes()
        );
        filter.setOwner(userId);
        filter.setId(categoryId);
        return getRepository().update(categoryDTO, filter);
    }
    
    /**
     * Method for delete (deactivate) category document
     * @param userId owner id from request params
     * @param categoryId category id from requeset params
     * @return category document
     * @throws DataException throw if can not found category document
     */
    protected Category deleteCategory(
            ObjectId userId,
            ObjectId categoryId
    ) throws DataException {
        CategoriesFilter filter = new CategoriesFilter(new String[] {});
        filter.setOwner(userId);
        filter.setId(categoryId);
        return getRepository().deactivate(filter);
    }
}
