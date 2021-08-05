package synthwave.services.v1.catalogs;

import platform.dto.RuleDTO;
import platform.exceptions.AccessException;
import platform.exceptions.DataException;
import synthwave.models.mongodb.standalones.Catalog;
import synthwave.services.core.catalogs.CoreCatalogService;
import synthwave.utils.helpers.Comparator;
import platform.utils.helpers.ParamsManager;
import synthwave.utils.access.RightManager;
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
    
    @Override
    public Catalog getEntityByIdByOwner(
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
    
    @Override
    public List<Catalog> getEntitiesListByOwner(
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

    @Override
    public List<Catalog> getEntitiesList(
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

    @Override
    public Catalog createEntity(
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

    @Override
    public Catalog updateEntity(
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

    @Override
    public Catalog deleteEntity(
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
