package Services.core;


import DataTransferObjects.CatalogDTO;
import Filters.CatalogsFilter;
import Models.Catalog;
import Models.User;
import Sources.CatalogsSource;
import Sources.UsersSource;
import Utils.constants.UsersParams;
import com.google.gson.Gson;
import java.util.List;
import org.bson.types.ObjectId;
import spark.Request;


/**
 * Class for base work with catalog documents
 * @author small-entropy
 */
public class CoreCatalogService {
    private static final String[] PUBLIC_EXLUDES = new String[]{ "status" };
    private static final String[] PRIVATE_EXCLUDES = new String[] { "status" };
    private static final String[] GLOBAL_EXCLUDES = new String[] {};
    
    public static List<Catalog> getCatalogsByUser(Request request, CatalogsSource source) {
        // Set default values for some params
        final int DEFAULT_SKIP = 0;
        final int DEFAULT_LIMIT = 10;
        // Keys in query params
        final String SKIP_FIELD = "skip";
        final String LIMIT_FIELD = "limit";
        final String USER_ID = "id";
        
        String qSkip = request.queryMap().get(SKIP_FIELD).value();
        int skip = (qSkip == null) ? DEFAULT_SKIP : Integer.parseInt(qSkip);
        // Set limit value from request query
        String qLimit = request.queryMap().get(LIMIT_FIELD).value();
        int limit = (qLimit == null) ? DEFAULT_LIMIT : Integer.parseInt(qLimit);
        // User id
        String idParam = request.params(UsersParams.ID.getName());
        ObjectId id = new ObjectId(idParam);
        CatalogsFilter filter = new CatalogsFilter(skip, limit, new String[]{});
        filter.setOwner(id);
        return source.findAllByOwnerId(filter);
    } 
    
    protected static List<Catalog> getList(Request request, CatalogsSource source) {
        CatalogsFilter filter = new CatalogsFilter(PUBLIC_EXLUDES);
        return source.findAll(filter);
    }
    
    protected static Catalog createCatalog(String idParam, Request request, CatalogsSource catalogsSource, UsersSource usersSource) {
        User user = CoreUserService.getUserById(idParam, usersSource);
        CatalogDTO catalogDTO = new Gson().fromJson(request.body(), CatalogDTO.class);
        catalogDTO.setOwner(user);
        return catalogsSource.create(catalogDTO);
    }
}
