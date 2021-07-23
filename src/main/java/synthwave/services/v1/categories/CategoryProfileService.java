package synthwave.services.v1.categories;

import java.util.Arrays;
import java.util.List;

import dev.morphia.Datastore;
import spark.Request;

import platform.dto.RuleDTO;
import platform.exceptions.AccessException;
import platform.exceptions.DataException;
import platform.exceptions.TokenException;
import synthwave.models.mongodb.embeddeds.EmbeddedProperty;
import synthwave.services.core.categories.CoreCategoryProfileService;
import synthwave.utils.helpers.Comparator;

/**
 * Class for work with categories profile
 * @author small-entropy
 * @version 1
 */
public class CategoryProfileService extends CoreCategoryProfileService {

    /**
     * Default constructor for category profile service
     * @param datastore Morphia datastore object
     */
    public CategoryProfileService(Datastore datastore) {
        super(datastore, Arrays.asList("registered"));
    }

    /**
     * Method fir create category profile property
     * @param request Spark requst object
     * @return create category profile property
     * @throws TokenException throw if can't found some date
     * @throws DataException throw if send incorrect token
     */
    public EmbeddedProperty createCategoryProfile(
        Request request
    ) throws TokenException, DataException{
        boolean isTrusted = Comparator.id_fromParam_fromToken(request);
        if (isTrusted) {
            return createEmbeddedProperty(request);
        } else {
            Error error = new Error("Han not access to create profile property");
            throw new TokenException("NotEquals", error);
        }
    }

    /**
     * Method for update category profile property
     * @param request Spark request object
     * @param right name of right
     * @param action name of action
     * @return updated profile property
     * @throws AccessException throw if user hasn't access to update 
     * @throws DataException throw if can't be found some data
     */
    public EmbeddedProperty updateCategoryProperty(
        Request request,
        String right,
        String action
    ) throws AccessException, DataException {
        boolean hasAccess = checkHasAccess(request, right, action);
        if (hasAccess) {
            return updateEmbeddedProperty(request);
        } else {
            Error error = new Error("Has no access to update profile property");
            throw new AccessException("CanNotUpdate", error);
        }
    }

    /**
     * Method for delete category profile property
     * @param request Spark request object
     * @param right name of right
     * @param action name of action
     * @return updated profile list
     * @throws AccessException throw if hasn't access to delete property
     * @throws DataException throw if can't be found some data
     */
    public List<EmbeddedProperty> deleteCategoryProperty(
        Request request,
        String right,
        String action
    ) throws AccessException, DataException {
        boolean hasAccess = checkHasAccess(request, right, action);
        if (hasAccess) {
            return deletEmbeddedProperty(request);
        } else {
            Error error = new Error("Has no access to delete profile property");
            throw new AccessException("CanNotDelete", error);
        }
    }

    @Override
    protected boolean checkExistHasAccess(
        RuleDTO rule,
        boolean isTrusted
    ) {
        return (isTrusted) ? rule.isMyPublic() : rule.isMyPublic();
    }
}
