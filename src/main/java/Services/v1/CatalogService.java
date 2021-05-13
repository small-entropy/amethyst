package Services.v1;

import DataTransferObjects.RuleDTO;
import Exceptions.AccessException;
import Models.Catalog;
import Services.core.CoreCatalogService;
import Sources.CatalogsSource;
import Sources.UsersSource;
import Utils.constants.UsersParams;
import java.util.List;
import spark.Request;

/**
 * Class for work with catalog documents
 * @version 1
 * @author small-entropy
 */
public class CatalogService extends CoreCatalogService {

    public static List<Catalog> getCatalogs(Request request, CatalogsSource source) {
        return getList(request, source);
    }

    public static Catalog createCatalog(Request request, CatalogsSource catalogsSource, UsersSource usersSource, RuleDTO rule) throws AccessException {
        boolean hasAccess = rule.isMyGlobal();
        if (hasAccess) {
            String idParam = request.params(UsersParams.ID.getName());
            return createCatalog(idParam, request, catalogsSource, usersSource);
        } else {
            Error error = new Error("Has no access to create catalog");
            throw new AccessException("CanNotCreae", error);
        }
    }
}
