package Services.core;


import DataTransferObjects.CatalogDTO;
import Exceptions.DataException;
import Filters.CatalogsFilter;
import Models.Catalog;
import Models.User;
import Sources.CatalogsSource;
import Sources.UsersSource;
import Utils.constants.ListConstants;
import Utils.constants.QueryParams;
import Utils.constants.RequestParams;
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
     * @param source datasource for catalogs collection
     * @param excludes exludes fields
     * @return catalog document
     */
    public static Catalog getCatalogByRequestByUserId(Request request, CatalogsSource source, String[] excludes) {
        String catalogIdParam = request.params(RequestParams.CATALOG_ID.getName());
        String ownerIdParam = request.params(RequestParams.USER_ID.getName());
        
        ObjectId ownerId = new ObjectId(ownerIdParam);
        ObjectId catalogId = new ObjectId(catalogIdParam);
        
        return getCatalogById(catalogId, ownerId, source, excludes);
    }
    
    /**
     * Method for get catalog with exluded fields by catalog object/document
     * @param catalog catalog object/document
     * @param source datasource for catalog collection
     * @param excludes array of exluded fields
     * @return catalog document
     */
    protected static Catalog getCatalogByDocument(Catalog catalog, CatalogsSource source, String[] excludes) {
        ObjectId ownerId = catalog.getOwner().getId();
        ObjectId catalogId = catalog.getPureId();
        return getCatalogById(catalogId, ownerId, source, excludes);
    }
    
    /**
     * Method for get catalog document by id
     * @param catalogId catalog id from request params
     * @param ownerId owner id from request params
     * @param source datasource for catalog collection
     * @param excludes array of exluded field
     * @return founded catalog document
     */
    protected static Catalog getCatalogById(ObjectId catalogId, ObjectId ownerId, CatalogsSource source, String[] excludes) {
        CatalogsFilter filter = new CatalogsFilter();
        filter.setId(catalogId);
        filter.setOwner(ownerId);
        filter.setExcludes(excludes);
        return source.findOneByOwnerAndId(filter);
    }
    
    /**
     * Method for get catalog from requset by 
     * @param request Spark request object
     * @param source datasource for catalog collection
     * @param excludes array of exludes fields
     * @return list of catalog documents
     */
    protected static List<Catalog> getCatalogsByRequestForUser(Request request, CatalogsSource source, String[] excludes) {
        String qSkip = request.queryMap().get(QueryParams.SKIP.getKey()).value();
        int skip = (qSkip == null) ? ListConstants.SKIP.getValue() : Integer.parseInt(qSkip);
        // Set limit value from request query
        String qLimit = request.queryMap().get(QueryParams.LIMIT.getKey()).value();
        int limit = (qLimit == null) ? ListConstants.LIMIT.getValue() : Integer.parseInt(qLimit);
        // User id
        String idParam = request.params(RequestParams.USER_ID.getName());
        ObjectId id = new ObjectId(idParam);
        
        CatalogsFilter filter = new CatalogsFilter(skip, limit, new String[]{});
        filter.setOwner(id);
        filter.setExcludes(excludes);
        return source.findAllByOwnerId(filter);
    } 
    
    /**
     * MEthod for get catalogs list
     * @param request Spark reqeust object
     * @param source datasource for catalogs colltions
     * @param excludes array of exludes fields
     * @return list of catalog documents
     */
    protected static List<Catalog> getList(Request request, CatalogsSource source, String[] excludes) {
        CatalogsFilter filter = new CatalogsFilter();
        filter.setExcludes(excludes);
        return source.findAll(filter);
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
    protected static Catalog createCatalog(String idParam, Request request, CatalogsSource catalogsSource, UsersSource usersSource) {
        User user = CoreUserService.getUserById(idParam, usersSource);
        CatalogDTO catalogDTO = new Gson().fromJson(request.body(), CatalogDTO.class);
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
    protected static Catalog updateCatalog(String idParam, String catalogIdParam, Request reqeust, CatalogsSource catalogsSource) throws DataException {
        // Get catalog ObjectId by string
        ObjectId catalogId = new ObjectId(catalogIdParam);
        ObjectId ownerId = new ObjectId(idParam);
        // Get catalog data transfer object
        CatalogDTO catalogDTO = new Gson().fromJson(reqeust.body(), CatalogDTO.class);
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
    protected static Catalog deleteCatalog(String idParam, String catalogIdParam, CatalogsSource catalogsSource) throws DataException {
        ObjectId catalogId = new ObjectId(catalogIdParam);
        ObjectId ownerId = new ObjectId(idParam);
        CatalogsFilter filter = new CatalogsFilter(new String[]{});
        filter.setOwner(ownerId);
        filter.setId(catalogId);
        return catalogsSource.deactivate(filter);
    }
}
