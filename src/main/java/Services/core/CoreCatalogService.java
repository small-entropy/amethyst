package Services.core;

import DataTransferObjects.v1.CatalogDTO;
import Exceptions.DataException;
import Filters.common.CatalogsFilter;
import Models.Standalones.Catalog;
import Models.Standalones.User;
import Repositories.v1.CatalogsRepository;
import Services.base.BaseDocumentService;
import Utils.common.ParamsManager;
import Utils.common.QueryManager;
import dev.morphia.Datastore;
import java.util.List;
import org.bson.types.ObjectId;
import spark.Request;


/**
 * Class for base work with catalog documents
 * @author small-entropy
 */
public abstract class CoreCatalogService
        extends BaseDocumentService<CatalogsRepository> {
    
    
    public CoreCatalogService(Datastore datastore) {
        super(datastore, new CatalogsRepository(datastore));
    }
    
    public CoreCatalogService(
            Datastore datastore,
            String[] globalExcludes,
            String[] publicExcludes,
            String[] privateExcludes
    ) {
        super(
                datastore,
                new CatalogsRepository(datastore), 
                globalExcludes, 
                publicExcludes, 
                privateExcludes
        );
    }
    
    /**
     * Method for get catalog document by request (get id from requst params)
     * @param request Spark request object
     * @param excludes exludes fields
     * @return catalog document
     * @throws DataException
     */
    public Catalog getCatalogByRequestByUserId(
            Request request, 
            String[] excludes
    ) throws DataException {
        ObjectId catalogId = ParamsManager.getCatalogId(request);
        ObjectId userId = ParamsManager.getUserId(request);
        return getCatalogById(catalogId, userId, excludes);
    }
    
    /**
     * Method for get catalog with exluded fields by catalog object/document
     * @param catalog catalog object/document
     * @param catalogsRepository datasource for catalog collection
     * @param excludes array of exluded fields
     * @return catalog document
     */
    protected Catalog getCatalogByDocument(
            Catalog catalog, 
            String[] excludes
    ) {
        ObjectId ownerId = catalog.getOwner().getId();
        ObjectId catalogId = catalog.getId();
        return getCatalogById(catalogId, ownerId, excludes);
    }
    
    /**
     * Method for get catalog document by id
     * @param catalogId catalog id from request params
     * @param ownerId owner id from request params
     * @param excludes array of exluded field
     * @return founded catalog document
     */
    protected Catalog getCatalogById(
            ObjectId catalogId, 
            ObjectId ownerId,
            String[] excludes
    ) {
        CatalogsFilter filter = new CatalogsFilter(
                catalogId, 
                ownerId, 
                excludes
        );
        return getRepository().findOneByOwnerAndId(filter);
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
    protected List<Catalog> getCatalogsByRequestForUser(
            Request request, 
            String[] excludes
    ) throws DataException {
        int skip = QueryManager.getSkip(request);
        int limit = QueryManager.getLimit(request);
        // User id
        ObjectId userId = ParamsManager.getUserId(request);
        
        CatalogsFilter filter = new CatalogsFilter(skip, limit, excludes);
        filter.setOwner(userId);
        return getRepository().findAllByOwnerId(filter);
    } 
    
    /**
     * MEthod for get catalogs list
     * @param request Spark reqeust object
     * @param excludes array of exludes fields
     * @return list of catalog documents
     */
    protected List<Catalog> getList(
            Request request, 
            String[] excludes
    ) {
        int skip = QueryManager.getSkip(request);
        int limit = QueryManager.getLimit(request);
        CatalogsFilter filter = new CatalogsFilter(skip, limit, excludes);
        return getRepository().findAll(filter);
    }
    
    /**
     * Method for create catalog document for owner (from params) 
     * by request body
     * @param userId owner id
     * @param request Spark request object
     * @return created catalog params
     */
    protected Catalog createCatalog( ObjectId userId, Request request) {
        User user = getUserById(userId);
        CatalogDTO catalogDTO =  CatalogDTO.build(request, CatalogDTO.class);
        catalogDTO.setOwner(user);
        return getRepository().create(catalogDTO);
    }
    
    /**
     * Method for update catalog document
     * @param userId user id from request params
     * @param catalogId catalog id from request params
     * @param reqeust Spark request obect
     * @return updated catalog document
     * @throws DataException 
     */
    protected Catalog updateCatalog(
            ObjectId userId, 
            ObjectId catalogId, 
            Request request
    ) throws DataException {
        // Get catalog data transfer object
        CatalogDTO catalogDTO = CatalogDTO.build(request, CatalogDTO.class); 
        // Create filter for serach document
        CatalogsFilter filter = new CatalogsFilter(catalogId, userId);
        // Return update result
        return getRepository().update(catalogDTO, filter);
    }
    
    /**
     * Method for deactivate catalog document
     * @param userId user id
     * @param catalogId catalog id
     * @param catalogsSource datasource for catalog collection
     * @return updated catalog document
     * @throws DataException throw if can not be found catalog
     */
    protected Catalog deleteCatalog(
            ObjectId userId, 
            ObjectId catalogId
    ) throws DataException {
        CatalogsFilter filter = new CatalogsFilter(catalogId, userId);
        return getRepository().deactivate(filter);
    }
}
