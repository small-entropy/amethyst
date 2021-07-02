package Services.v1;

import DataTransferObjects.v1.RuleDTO;
import Exceptions.AccessException;
import Exceptions.DataException;
import Models.Standalones.Catalog;
import Services.core.CoreCatalogService;
import Repositories.v1.CatalogsRepository;
import Repositories.v1.UsersRepository;
import Utils.common.Comparator;
import Utils.common.ParamsManager;
import Utils.v1.RightManager;
import java.util.List;
import org.bson.types.ObjectId;
import spark.Request;

/**
 * Class for work with catalog documents
 * @version 1
 * @author small-entropy
 */
public class CatalogService extends CoreCatalogService {
    private static final String[] PUBLIC_EXCLUDES = new String[]{ 
        "owner", 
        "version",
        "status"
    };
    
    private static final String[] PRIVATE_EXCLUDES = new String[] { 
        "version",
        "status"
    };
    
    /**
     * Method for get catalog by id from request
     * @param request Spark request object
     * @param catalogsRepository datasource for catalogs collection
     * @param rule rule data transfer object
     * @return catalog document
     * @throws DataException
     */
    public static Catalog getCatalogById(
            Request request, 
            CatalogsRepository catalogsRepository, 
            RuleDTO rule
    ) throws DataException {
        String[] excludes = getExcludes(
                request, 
                rule, 
                PUBLIC_EXCLUDES, 
                PRIVATE_EXCLUDES
        );
        var catalog = getCatalogByRequestByUserId(
                request, 
                catalogsRepository, 
                excludes
        );
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
    public static List<Catalog> getCatalogsByUser(
            Request request, 
            CatalogsRepository catalogsRepository, 
            RuleDTO rule
    ) throws DataException {
        String[] excludes = getExcludes(
                request, 
                rule, 
                PUBLIC_EXCLUDES, 
                PRIVATE_EXCLUDES
        );
        var catalogs = getCatalogsByRequestForUser(
                request, 
                catalogsRepository, 
                excludes
        );
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
     * @param catalogsRepository datasource for catalog collection
     * @param rule rule data transfer obejct
     * @return list of catalogs
     * @throws DataException throw when can get catalogs
     */
    public static List<Catalog> getCatalogs(
            Request request, 
            CatalogsRepository catalogsRepository, 
            RuleDTO rule
    ) throws DataException {
        String[] excludes = getExcludes(
                request, 
                rule, 
                PUBLIC_EXCLUDES, 
                PRIVATE_EXCLUDES
        );
        List<Catalog> catalogs = getList(
                request, 
                catalogsRepository, 
                excludes
        );
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
     * @param catalogsRepository datasource for catalogs collection
     * @param usersRepository datasource for users collection
     * @param rule rule data transfer object
     * @return created catalog document
     * @throws AccessException 
     * @throws Exceptions.DataException 
     */
    public static Catalog createCatalog(
            Request request, 
            CatalogsRepository catalogsRepository, 
            UsersRepository usersRepository, 
            RuleDTO rule
    ) throws AccessException, DataException {
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        boolean hasAccess = (isTrusted) 
                ? rule.isMyGlobal()
                : rule.isOtherGlobal();
        if (hasAccess) {
            ObjectId userId = ParamsManager.getUserId(request);
            Catalog catalog = createCatalog(
                    userId, 
                    request, 
                    catalogsRepository, 
                    usersRepository
            );
            String[] excludes = getExcludes(
                    isTrusted, 
                    rule, 
                    PUBLIC_EXCLUDES, 
                    PRIVATE_EXCLUDES
            );
            return getCatalogByDocument(catalog, catalogsRepository, excludes);
        } else {
            Error error = new Error("Has no access to create catalog");
            throw new AccessException("CanNotCreate", error);
        }
    }

    /**
     * Method for get update catalo by request
     * @param request Spark request object
     * @param catalogsRepository datasource for catalogs collection
     * @param rule rule data transfer object 
     * @return updated document
     * @throws AccessException throw if no access to update document
     * @throws DataException throw if can not find document
     */
    public static Catalog updateCatalog(
            Request request, 
            CatalogsRepository catalogsRepository, 
            RuleDTO rule
    ) throws AccessException, DataException {
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        boolean hasAccess = (isTrusted) ? rule.isMyPrivate() : rule.isOtherPrivate();
        if (hasAccess) {
            ObjectId userId = ParamsManager.getUserId(request);
            ObjectId catalogId = ParamsManager.getCatalogId(request);
            Catalog catalog = updateCatalog(
                    userId, 
                    catalogId, 
                    request, 
                    catalogsRepository
            );
            String[] excludes = getExcludes(
                    isTrusted, 
                    rule, 
                    PUBLIC_EXCLUDES, 
                    PRIVATE_EXCLUDES
            );
            return getCatalogByDocument(catalog, catalogsRepository, excludes);
        } else {
            Error error = new Error("Has no access to update catalog document");
            throw new AccessException("CanNotUpdate", error);
        }
    }

    /**
     * Method for delete catalog
     * @param request Spark request object
     * @param catalogsRepository datastoure source for catalogs collection
     * @param rule rule data transfer object
     * @return 
     * @throws AccessException
     * @throws DataException 
     */
    public static Catalog deleteCatalog(
            Request request, 
            CatalogsRepository catalogsRepository, 
            RuleDTO rule
    ) throws AccessException, DataException {
        boolean hasAccess = RightManager.chechAccess(request, rule);
        if (hasAccess) {
            ObjectId userId = ParamsManager.getUserId(request);
            ObjectId catalogId = ParamsManager.getCatalogId(request);
            return deleteCatalog(userId, catalogId, catalogsRepository);
        } else {
            Error error = new Error("Has no access to delete catalog document");
            throw new AccessException("CanNotDelete", error);
        }
    }
}
