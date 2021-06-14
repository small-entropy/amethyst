package Services.core;


import DataTransferObjects.v1.CatalogDTO;
import Exceptions.DataException;
import Filters.CatalogsFilter;
import Models.Standalones.Catalog;
import Models.Standalones.User;
import Repositories.v1.CatalogsRepository;
import Repositories.v1.UsersRepository;
import Utils.common.ParamsManager;
import Utils.common.QueryManager;
import com.google.gson.Gson;
import java.util.List;
import org.bson.types.ObjectId;
import spark.Request;


/**
 * Class for base work with catalog documents
 * @author small-entropy
 */
public class CoreCatalogService {
    
    /**
     * Method for get catalog document by request (get id from requst params)
     * @param request Spark request object
     * @param catalogsRepository datasource for catalogs collection
     * @param excludes exludes fields
     * @return catalog document
     */
    public static Catalog getCatalogByRequestByUserId(
            Request request, 
            CatalogsRepository catalogsRepository, 
            String[] excludes
    ) {
        String catalogIdParam = ParamsManager.getCatalogId(request);
        String ownerIdParam = ParamsManager.getUserId(request);
        
        ObjectId ownerId = new ObjectId(ownerIdParam);
        ObjectId catalogId = new ObjectId(catalogIdParam);
        
        return getCatalogById(catalogId, ownerId, catalogsRepository, excludes);
    }
    
    /**
     * Method for get catalog with exluded fields by catalog object/document
     * @param catalog catalog object/document
     * @param catalogsRepository datasource for catalog collection
     * @param excludes array of exluded fields
     * @return catalog document
     */
    protected static Catalog getCatalogByDocument(
            Catalog catalog, 
            CatalogsRepository catalogsRepository, 
            String[] excludes
    ) {
        ObjectId ownerId = catalog.getOwner().getId();
        ObjectId catalogId = catalog.getId();
        return getCatalogById(catalogId, ownerId, catalogsRepository, excludes);
    }
    
    /**
     * Method for get catalog document by id
     * @param catalogId catalog id from request params
     * @param ownerId owner id from request params
     * @param catalogsRepository datasource for catalog collection
     * @param excludes array of exluded field
     * @return founded catalog document
     */
    protected static Catalog getCatalogById(
            ObjectId catalogId, 
            ObjectId ownerId, 
            CatalogsRepository catalogsRepository, 
            String[] excludes
    ) {
        CatalogsFilter filter = new CatalogsFilter();
        filter.setId(catalogId);
        filter.setOwner(ownerId);
        filter.setExcludes(excludes);
        return catalogsRepository.findOneByOwnerAndId(filter);
    }
    
    public static Catalog getCatalogById(
            String catalogIdParam, 
            CatalogsRepository catalogsRepository
    ) {
        ObjectId id = new ObjectId(catalogIdParam);
        CatalogsFilter filter = new CatalogsFilter();
        filter.setId(id);
        filter.setExcludes(new String[] {});
        return catalogsRepository.findOneById(filter);
    }
    
    /**
     * Method for get catalog from requset by 
     * @param request Spark request object
     * @param catalogsRepository datasource for catalog collection
     * @param excludes array of exludes fields
     * @return list of catalog documents
     */
    protected static List<Catalog> getCatalogsByRequestForUser(
            Request request, 
            CatalogsRepository catalogsRepository, 
            String[] excludes
    ) {
        int skip = QueryManager.getSkip(request);
        int limit = QueryManager.getLimit(request);
        // User id
        String idParam = ParamsManager.getUserId(request);
        ObjectId id = new ObjectId(idParam);
        
        CatalogsFilter filter = new CatalogsFilter(skip, limit, new String[]{});
        filter.setOwner(id);
        filter.setExcludes(excludes);
        return catalogsRepository.findAllByOwnerId(filter);
    } 
    
    /**
     * MEthod for get catalogs list
     * @param request Spark reqeust object
     * @param catalogsRepository datasource for catalogs colltions
     * @param excludes array of exludes fields
     * @return list of catalog documents
     */
    protected static List<Catalog> getList(
            Request request, 
            CatalogsRepository catalogsRepository, 
            String[] excludes
    ) {
        CatalogsFilter filter = new CatalogsFilter();
        filter.setExcludes(excludes);
        return catalogsRepository.findAll(filter);
    }
    
    /**
     * Method for create catalog document for owner (from params) 
     * by request body
     * @param idParam owner id param as string
     * @param request Spark request object
     * @param catalogsSource datasorce for catalogs collection
     * @param usersSource datasource for users collection
     * @return created catalog params
     */
    protected static Catalog createCatalog(
            String idParam, 
            Request request, 
            CatalogsRepository catalogsSource, 
            UsersRepository usersSource
    ) {
        User user = CoreUserService.getUserById(idParam, usersSource);
        CatalogDTO catalogDTO = new Gson()
                .fromJson(request.body(), CatalogDTO.class);
        catalogDTO.setOwner(user);
        return catalogsSource.create(catalogDTO);
    }
    
    /**
     * Method for update catalog document
     * @param idParam user id from request params
     * @param catalogIdParam catalog id from request params
     * @param reqeust Spark request obect
     * @param catalogsSource datasource for catalog collection
     * @return updated catalog document
     * @throws DataException 
     */
    protected static Catalog updateCatalog(
            String idParam, 
            String catalogIdParam, 
            Request reqeust, 
            CatalogsRepository catalogsSource
    ) throws DataException {
        // Get catalog ObjectId by string
        ObjectId catalogId = new ObjectId(catalogIdParam);
        ObjectId ownerId = new ObjectId(idParam);
        // Get catalog data transfer object
        CatalogDTO catalogDTO = CatalogDTO.build(reqeust, CatalogDTO.class); 
        // Create filter for serach document
        CatalogsFilter filter = new CatalogsFilter(new String[]{});
        // Set catalog owner
        filter.setOwner(ownerId);
        // Set catalog id
        filter.setId(catalogId);
        // Return update result
        return catalogsSource.update(catalogDTO, filter);
    }
    
    /**
     * Method for deactivate catalog document
     * @param idParam user id from requst params
     * @param catalogIdParam catalog id from reqeuset params
     * @param catalogsSource datasource for catalog collection
     * @return updated catalog document
     * @throws DataException throw if can not be found catalog
     */
    protected static Catalog deleteCatalog(
            String idParam, 
            String catalogIdParam, 
            CatalogsRepository catalogsSource
    ) throws DataException {
        ObjectId catalogId = new ObjectId(catalogIdParam);
        ObjectId ownerId = new ObjectId(idParam);
        CatalogsFilter filter = new CatalogsFilter(new String[]{});
        filter.setOwner(ownerId);
        filter.setId(catalogId);
        return catalogsSource.deactivate(filter);
    }
}
