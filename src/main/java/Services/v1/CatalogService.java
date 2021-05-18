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
     * @return list of catalogs
     */
    public static List<Catalog> getCatalogs(Request request, CatalogsSource source, RuleDTO rule) {
        String[] exludes = getExudes(request, rule);
        return getList(request, source, exludes);
    }

    /**
     * Method for create catalog documents by request
     * @param request
     * @param catalogsSource
     * @param usersSource
     * @param rule
     * @return
     * @throws AccessException 
     */
    public static Catalog createCatalog(Request request, CatalogsSource catalogsSource, UsersSource usersSource, RuleDTO rule) throws AccessException {
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        boolean hasAccess = (isTrusted) ? rule.isMyGlobal(): rule.isOtherGlobal();
        if (hasAccess) {
            String idParam = request.params(RequestParams.USER_ID.getName());
            Catalog catalog = createCatalog(idParam, request, catalogsSource, usersSource);
            String[] excliudes = getExudes(request, rule);
            return getCatalogByDocument(catalog, catalogsSource, PRIVATE_EXCLUDES);
        } else {
            Error error = new Error("Has no access to create catalog");
            throw new AccessException("CanNotCreae", error);
        }
    }

    public static Catalog updateCatalog(Request request, CatalogsSource catalogsSource, UsersSource userSource, RuleDTO rule) throws AccessException, DataException {
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        boolean hasAccess = (isTrusted) ? rule.isMyPrivate() : rule.isOtherPrivate();
        if (hasAccess) {
            String idParam = request.params(RequestParams.USER_ID.getName());
            String catalogIdParam = request.params(RequestParams.CATALOG_ID.getName());
            Catalog catalog = updateCatalog(idParam, catalogIdParam, request, catalogsSource, userSource);
            String[] exludes = getExudes(request, rule);
            return getCatalogByDocument(catalog, catalogsSource, exludes);
        } else {
            Error error = new Error("Has no access to update catalog document");
            throw new AccessException("CanNotUpdate", error);
        }
    }
}
