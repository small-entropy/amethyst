package Services.v1;

import DataTransferObjects.RuleDTO;
import Exceptions.AccessException;
import Exceptions.DataException;
import Models.Catalog;
import Services.core.CoreCatalogService;
import Sources.CatalogsSource;
import Sources.UsersSource;
import Utils.common.Comparator;
import Utils.constants.RequestParams;
import Utils.v1.RightManager;
import java.util.List;
import spark.Request;

/**
 * Class for work with catalog documents
 * @version 1
 * @author small-entropy
 */
public class CatalogService extends CoreCatalogService {
    private static final String[] PUBLIC_EXCLUDES = new String[]{ "owner", "status", "version"  };
    private static final String[] PRIVATE_EXCLUDES = new String[] { "status", "version" };
    
    /**
     * Method for get catalog by id from request
     * @param request Spark request object
     * @param soure datasource for catalogs collection
     * @param rule rule data transfer object
     * @return catalog document
     * @throws DataException
     */
    public static Catalog getCatalogById(Request request, CatalogsSource soure, RuleDTO rule) throws DataException {
        String[] excludes = RightManager.getExcludes(request, rule, PUBLIC_EXCLUDES, PRIVATE_EXCLUDES);
        var catalog = getCatalogByRequestByUserId(request, soure, excludes);
        if (catalog != null) {
            return catalog;
        } else {
            Error error = new Error("Can not find catalog focument by user id from request params");
            throw new DataException("NotFount", error);
        }
    }
    
    /**
     * Method for get list of user catalog by owner id from request params
     * @param request Spark request obejct 
     * @param source datasource for catalogs colletion
     * @param rule rule data transfer object
     * @return list of catalog documents
     * @throws DataException throw if can not find 
     */
    public static List<Catalog> getCatalogsByUser(Request request, CatalogsSource source, RuleDTO rule) throws DataException {
        String[] excludes = RightManager.getExcludes(request, rule, PUBLIC_EXCLUDES, PRIVATE_EXCLUDES);
        var catalogs = getCatalogsByRequestForUser(request, source, excludes);
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
     * @param source datasource for catalog collection
     * @param rule rule data transfer obejct
     * @return list of catalogs
     * @throws DataException throw when can get catalogs
     */
    public static List<Catalog> getCatalogs(Request request, CatalogsSource source, RuleDTO rule) throws DataException {
        String[] excludes = RightManager.getExcludes(request, rule, PUBLIC_EXCLUDES, PRIVATE_EXCLUDES);
        List<Catalog> catalogs = getList(request, source, excludes);
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
     * @param catalogsSource datasource for catalogs collection
     * @param usersSource datasource for users collection
     * @param rule rule data transfer object
     * @return created catalog document
     * @throws AccessException 
     */
    public static Catalog createCatalog(Request request, CatalogsSource catalogsSource, UsersSource usersSource, RuleDTO rule) throws AccessException {
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        boolean hasAccess = (isTrusted) ? rule.isMyGlobal(): rule.isOtherGlobal();
        if (hasAccess) {
            String idParam = request.params(RequestParams.USER_ID.getName());
            Catalog catalog = createCatalog(idParam, request, catalogsSource, usersSource);
            String[] excludes = RightManager.getExludesByRule(isTrusted, rule, PUBLIC_EXCLUDES, PRIVATE_EXCLUDES);
            return getCatalogByDocument(catalog, catalogsSource, excludes);
        } else {
            Error error = new Error("Has no access to create catalog");
            throw new AccessException("CanNotCreate", error);
        }
    }

    /**
     * Method for get update catalo by request
     * @param request Spark request object
     * @param source datasource for catalogs collection
     * @param rule
     * @return
     * @throws AccessException
     * @throws DataException 
     */
    public static Catalog updateCatalog(Request request, CatalogsSource source, RuleDTO rule) throws AccessException, DataException {
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        boolean hasAccess = (isTrusted) ? rule.isMyPrivate() : rule.isOtherPrivate();
        if (hasAccess) {
            String idParam = request.params(RequestParams.USER_ID.getName());
            String catalogIdParam = request.params(RequestParams.CATALOG_ID.getName());
            Catalog catalog = updateCatalog(idParam, catalogIdParam, request, source);
            String[] excludes = RightManager.getExludesByRule(isTrusted, rule, PUBLIC_EXCLUDES, PRIVATE_EXCLUDES);
            return getCatalogByDocument(catalog, source, excludes);
        } else {
            Error error = new Error("Has no access to update catalog document");
            throw new AccessException("CanNotUpdate", error);
        }
    }

    /**
     * Method for delete catalog
     * @param request Spark request object
     * @param source datastoure source for catalogs collection
     * @param rule rule data transfer object
     * @return 
     * @throws AccessException
     * @throws DataException 
     */
    public static Catalog deleteCatalog(
            Request request, 
            CatalogsSource source, 
            RuleDTO rule
    ) throws AccessException, DataException {
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        boolean hasAccess = (isTrusted) ? rule.isMyGlobal() : rule.isOtherGlobal();
        if (hasAccess) {
            String idParam = request.params(RequestParams.USER_ID.getName());
            String catalogIdParam = request.params(RequestParams.CATALOG_ID.getName());
            return deleteCatalog(idParam, catalogIdParam, source);
        } else {
            Error error = new Error("Has no access to delete catalog document");
            throw new AccessException("CanNotDelete", error);
        }
    }
}
