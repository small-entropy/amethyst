package Services.core;


import DataTransferObjects.v1.CatalogDTO;
import Exceptions.DataException;
import Filters.common.CatalogsFilter;
import Models.Standalones.Catalog;
import Models.Standalones.User;
import Repositories.v1.CatalogsRepository;
import Repositories.v1.UsersRepository;
import Utils.common.ParamsManager;
import Utils.common.QueryManager;
import java.util.List;
import org.bson.types.ObjectId;
import spark.Request;


/**
 * Class for base work with catalog documents
 * @author small-entropy
 */
public class CoreCatalogService extends CoreService {
    
    /**
     * Method for get catalog document by request (get id from requst params)
     * @param request Spark request object
     * @param catalogsRepository datasource for catalogs collection
     * @param excludes exludes fields
     * @return catalog document
     * @throws DataException
     */
    public static Catalog getCatalogByRequestByUserId(
            Request request, 
            CatalogsRepository catalogsRepository, 
            String[] excludes
    ) throws DataException {
        ObjectId catalogId = ParamsManager.getCatalogId(request);
        ObjectId userId = ParamsManager.getUserId(request);
        return getCatalogById(catalogId, userId, catalogsRepository, excludes);
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
        CatalogsFilter filter = new CatalogsFilter(
                catalogId, 
                ownerId, 
                excludes
        );
        return catalogsRepository.findOneByOwnerAndId(filter);
    }
    
    public static Catalog getCatalogById(
            ObjectId catalogId, 
            CatalogsRepository catalogsRepository
    ) {
        CatalogsFilter filter = new CatalogsFilter(catalogId);
        return catalogsRepository.findOneById(filter);
    }
    
    /**
     * Method for get catalog from requset by 
     * @param request Spark request object
     * @param catalogsRepository datasource for catalog collection
     * @param excludes array of exludes fields
     * @return list of catalog documents
     * @throws DataException
     */
    protected static List<Catalog> getCatalogsByRequestForUser(
            Request request, 
            CatalogsRepository catalogsRepository, 
            String[] excludes
    ) throws DataException {
        int skip = QueryManager.getSkip(request);
        int limit = QueryManager.getLimit(request);
        // User id
        ObjectId userId = ParamsManager.getUserId(request);
        
        CatalogsFilter filter = new CatalogsFilter(skip, limit, excludes);
        filter.setOwner(userId);
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
        int skip = QueryManager.getSkip(request);
        int limit = QueryManager.getLimit(request);
        CatalogsFilter filter = new CatalogsFilter(skip, limit, excludes);
        return catalogsRepository.findAll(filter);
    }
    
    /**
     * Method for create catalog document for owner (from params) 
     * by request body
     * @param userId owner id
     * @param request Spark request object
     * @param catalogsSource datasorce for catalogs collection
     * @param usersSource datasource for users collection
     * @return created catalog params
     */
    protected static Catalog createCatalog(
            ObjectId userId, 
            Request request, 
            CatalogsRepository catalogsSource, 
            UsersRepository usersSource
    ) {
        User user = CoreUserService.getUserById(userId, usersSource);
        CatalogDTO catalogDTO =  CatalogDTO.build(request, CatalogDTO.class);
        catalogDTO.setOwner(user);
        return catalogsSource.create(catalogDTO);
    }
    
    /**
     * Method for update catalog document
     * @param userId user id from request params
     * @param catalogId catalog id from request params
     * @param reqeust Spark request obect
     * @param catalogsSource datasource for catalog collection
     * @return updated catalog document
     * @throws DataException 
     */
    protected static Catalog updateCatalog(
            ObjectId userId, 
            ObjectId catalogId, 
            Request reqeust, 
            CatalogsRepository catalogsSource
    ) throws DataException {
        // Get catalog data transfer object
        CatalogDTO catalogDTO = CatalogDTO.build(reqeust, CatalogDTO.class); 
        // Create filter for serach document
        CatalogsFilter filter = new CatalogsFilter(catalogId, userId);
        // Return update result
        return catalogsSource.update(catalogDTO, filter);
    }
    
    /**
     * Method for deactivate catalog document
     * @param userId user id
     * @param catalogId catalog id
     * @param catalogsSource datasource for catalog collection
     * @return updated catalog document
     * @throws DataException throw if can not be found catalog
     */
    protected static Catalog deleteCatalog(
            ObjectId userId, 
            ObjectId catalogId, 
            CatalogsRepository catalogsSource
    ) throws DataException {
        CatalogsFilter filter = new CatalogsFilter(catalogId, userId);
        return catalogsSource.deactivate(filter);
    }
}
