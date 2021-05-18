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
import java.util.List;
import spark.Request;

/**
 * Class for work with catalog documents
 * @version 1
 * @author small-entropy
 */
public class CatalogService extends CoreCatalogService {
    private static final String[] PUBLIC_EXLUDES = new String[]{ "owner", "status", "version"  };
    private static final String[] PRIVATE_EXCLUDES = new String[] { "status", "version" };
    private static final String[] GLOBAL_EXLUDES = new String[] {};
    
    /**
     * Method fot get exlude fields by rule & request
     * @param request Spark request object
     * @param rule rule data transfer object
     * @return exclude fields
     */
    private static String[] getExudes(Request request, RuleDTO rule) {
        String[] exludes;
        if (rule != null) {
            boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        
            if (isTrusted) {
                if (rule.isMyGlobal()) {
                    exludes = GLOBAL_EXLUDES;
                } else if (rule.isMyPrivate()) {
                    exludes = PRIVATE_EXCLUDES;
                } else {
                    exludes = PUBLIC_EXLUDES;
                }
            } else {
                if (rule.isOtherGlobal()) {
                    exludes = GLOBAL_EXLUDES;
                } else if (rule.isOtherPrivate()) {
                    exludes = PRIVATE_EXCLUDES;
                } else {
                    exludes = PUBLIC_EXLUDES;
                }
            }
        } else {
            exludes = PUBLIC_EXLUDES;
        }
        return exludes;
    }
    
    /**
     * Method for get catalog by id from request
     * @param request Spark request object
     * @param soure datasource for catalogs collection
     * @param rule rule data transfer object
     * @return catalog document
     */
    public static Catalog getCatalogById(Request request, CatalogsSource soure, RuleDTO rule) {
        String[] excludes = getExudes(request, rule);
        return getCatalogById(request, soure, excludes);
    }
    
    public static List<Catalog> getCatalogsByUser(Request request, CatalogsSource source, RuleDTO ruleDTO) {
        String[] exludes = getExudes(request, ruleDTO);
        return getCatalogsByUser(request, source, exludes);
    }

    /**
     * Method for get list of catalogs documents
     * @param request Spark reqeust object
     * @param source datasource for catalog collection
     * @param rule rule data transfer obejct
     * @return list of catalogs
     */
    public static List<Catalog> getCatalogs(Request request, CatalogsSource source, RuleDTO rule) {
        String[] exludes = getExudes(request, rule);
        return getList(request, source, exludes);
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
            String[] excludes = getExudes(request, rule);
            return getCatalogByDocument(catalog, catalogsSource, excludes);
        } else {
            Error error = new Error("Has no access to create catalog");
            throw new AccessException("CanNotCreae", error);
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
            String[] exludes = getExudes(request, rule);
            return getCatalogByDocument(catalog, source, exludes);
        } else {
            Error error = new Error("Has no access to update catalog document");
            throw new AccessException("CanNotUpdate", error);
        }
    }
}
