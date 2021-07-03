package Services.v1;

import DataTransferObjects.v1.RuleDTO;
import Exceptions.AccessException;
import Exceptions.DataException;
import Models.Standalones.Catalog;
import Services.core.CoreCatalogService;
import Utils.common.Comparator;
import Utils.common.ParamsManager;
import Utils.v1.RightManager;
import dev.morphia.Datastore;
import java.util.List;
import org.bson.types.ObjectId;
import spark.Request;

/**
 * Class for work with catalog documents
 * @version 1
 * @author small-entropy
 */
public class CatalogService extends CoreCatalogService {
    
    public CatalogService(Datastore datastore) {
        super(
                datastore,
                new String[] {},
                new String[] {  "owner", "version", "status" },
                new String[] { "version", "status" }
        );
    }
    
    /**
     * Method for get catalog by id from request
     * @param request Spark request object
     * @param rule rule data transfer object
     * @return catalog document
     * @throws DataException
     */
    public Catalog getCatalogById(
            Request request,
            String right,
            String action
    ) throws DataException {
        RuleDTO rule = getRule(request, right, action);
        String[] excludes = getExcludes(request, rule);
        var catalog = getCatalogByRequestByUserId(request,excludes);
        if (catalog != null) {
            return catalog;
        } else {
            Error error = new Error("Can not find catalog document by user id from request params");
            throw new DataException("NotFount", error);
        }
    }
    
    /**
     * Method for get list of user catalog by owner id from request params
     * @param request Spark request obejct 
     * @param catalogsRepository datasource for catalogs colletion
     * @param rule rule data transfer object
     * @return list of catalog documents
     * @throws DataException throw if can not find 
     */
    public List<Catalog> getCatalogsByUser(
            Request request, 
            String right,
            String action
    ) throws DataException {
        RuleDTO rule = getRule(request, right, action);
        String[] excludes = getExcludes(request, rule);
        var catalogs = getCatalogsByRequestForUser(request,excludes);
        if (catalogs != null && !catalogs.isEmpty()) {
            return catalogs;
        } else {
            Error error = new Error("Can not find user catalogs by request params");
            throw new DataException("NotFound", error);
        }
    }

    /**
     * Method for get list of catalogs documents
     * @param request Spark reqeust object
     * @return list of catalogs
     * @throws DataException throw when can get catalogs
     */
    public List<Catalog> getCatalogs(
            Request request, 
            String right,
            String action
    ) throws DataException {
        RuleDTO rule = getRule(request, right, action);
        String[] excludes = getExcludes(request, rule);
        List<Catalog> catalogs = getList(request, excludes);
        if (!catalogs.isEmpty()) {
            return catalogs;
        } else {
            Error error = new Error("Can not find catalog documents");
            throw new DataException("NotFound", error);
        }
    }

    /**
     * Method for create catalog documents by request
     * @param request Spark request object
     * @param rule rule data transfer object
     * @return created catalog document
     * @throws AccessException 
     * @throws Exceptions.DataException 
     */
    public Catalog createCatalog(
            Request request,
            String right,
            String action
    ) throws AccessException, DataException {
        RuleDTO rule = getRule(request, right, action);
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        boolean hasAccess = (isTrusted) 
                ? rule.isMyGlobal()
                : rule.isOtherGlobal();
        if (hasAccess) {
            ObjectId userId = ParamsManager.getUserId(request);
            Catalog catalog = createCatalog(userId, request);
            String[] excludes = getExcludes(isTrusted, rule);
            return getCatalogByDocument(catalog, excludes);
        } else {
            Error error = new Error("Has no access to create catalog");
            throw new AccessException("CanNotCreate", error);
        }
    }

    /**
     * Method for get update catalo by request
     * @param request Spark request object
     * @param rule rule data transfer object 
     * @return updated document
     * @throws AccessException throw if no access to update document
     * @throws DataException throw if can not find document
     */
    public Catalog updateCatalog(
            Request request,
            String right,
            String action
    ) throws AccessException, DataException {
        RuleDTO rule = getRule(request, right, action);
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        boolean hasAccess = (isTrusted) ? rule.isMyPrivate() : rule.isOtherPrivate();
        if (hasAccess) {
            ObjectId userId = ParamsManager.getUserId(request);
            ObjectId catalogId = ParamsManager.getCatalogId(request);
            Catalog catalog = updateCatalog(userId, catalogId, request);
            String[] excludes = getExcludes(isTrusted, rule);
            return getCatalogByDocument(catalog, excludes);
        } else {
            Error error = new Error("Has no access to update catalog document");
            throw new AccessException("CanNotUpdate", error);
        }
    }

    /**
     * Method for delete catalog
     * @param request Spark request object
     * @param rule rule data transfer object
     * @return 
     * @throws AccessException
     * @throws DataException 
     */
    public Catalog deleteCatalog(
            Request request,
            String right,
            String action
    ) throws AccessException, DataException {
        RuleDTO rule = getRule(request, right, action);
        boolean hasAccess = RightManager.chechAccess(request, rule);
        if (hasAccess) {
            ObjectId userId = ParamsManager.getUserId(request);
            ObjectId catalogId = ParamsManager.getCatalogId(request);
            return deleteCatalog(userId, catalogId);
        } else {
            Error error = new Error("Has no access to delete catalog document");
            throw new AccessException("CanNotDelete", error);
        }
    }
}
