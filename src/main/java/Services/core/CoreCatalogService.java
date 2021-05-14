package Services.core;


import DataTransferObjects.CatalogDTO;
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
    private static final String[] PUBLIC_EXLUDES = new String[]{ "owner", "status" };
    private static final String[] PRIVATE_EXCLUDES = new String[] { "status" };
    private static final String[] GLOBAL_EXCLUDES = new String[] {};
    
    public static Catalog getCatalogById(Request request, CatalogsSource source) {
        String catalogIdParam = request.params(RequestParams.CATALOG_ID.getName());
        String ownerIdParam = request.params(RequestParams.USER_ID.getName());
        
        ObjectId ownerId = new ObjectId(ownerIdParam);
        ObjectId catalogId = new ObjectId(catalogIdParam);
        
        CatalogsFilter filter = new CatalogsFilter();
        filter.setId(catalogId);
        filter.setOwner(ownerId);
        filter.setExcludes(PUBLIC_EXLUDES);
        return source.findOneByOwnerAndId(filter);
    }
    
    public static List<Catalog> getCatalogsByUser(Request request, CatalogsSource source) {
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
        
        return source.findAllByOwnerId(filter);
    } 
    
    /**
     * MEthod for get catalogs list
     * @param request Spark reqeust object
     * @param source
     * @return 
     */
    protected static List<Catalog> getList(Request request, CatalogsSource source) {
        CatalogsFilter filter = new CatalogsFilter(PUBLIC_EXLUDES);
        return source.findAll(filter);
    }
    
    /**
     * Method for create catalog document for owner (from params) by request
     * body
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
}
