package synthwave.repositories.mongodb.v1;

import synthwave.dto.UserRightDTO;
import synthwave.filters.UsersFilter;
import platform.exceptions.DataException;
import synthwave.models.mongodb.standalones.User;
import synthwave.models.mongodb.embeddeds.EmbeddedRight;
import synthwave.utils.helpers.Searcher;
import dev.morphia.Datastore;
import java.util.Iterator;
import java.util.List;
import org.bson.types.ObjectId;

/**
 * Class for user datasource
 * @author small-entory
 */
public class RightsRepository extends UsersRepository {
    
    private final List<String> blackList;
    
    /**
     * Constructor for right datasource object
     * @param datastore Morphia datastore object
     * @param blackList
     */
    public RightsRepository(Datastore datastore, List<String> blackList) {
        super(datastore);
        this.blackList = blackList;
    }

    public List<String> getBlackList() {
        return blackList;
    }
    
    /**
     * Method for get user right by id param from request
     * @param rightId user right id as string
     * @param userId user id as string
     * @return user right document
     * @throws DataException throw if not found user or right
     */
    public EmbeddedRight getRightByIdParam(
            ObjectId rightId,
            ObjectId userId
    ) throws DataException {
        List<EmbeddedRight> rights = getList(userId);
        return Searcher.getUserRightByIdFromList(rightId, rights);
    }
    
    /**
     * Method for get user right by user id
     * @param userId user id as string
     * @return list of user rights
     * @throws DataException throw if not founded user or rights
     */
    public List<EmbeddedRight> getList(ObjectId userId) throws DataException {
        User user = getUserDocument(userId);
        List<EmbeddedRight> rights = user.getRights();
        if (rights != null && !rights.isEmpty()) {
            return rights;
        } else {
            Error error = new Error("Can not found user rights");
            throw new DataException("NotFound", error);
        }
    }
    
    /**
     * Method for check exist right in rights list
     * @param name name of right
     * @param user user document
     * @return check result
     */
    private boolean hasRight(String name, User user) {
        boolean hasRight = false;
        List<EmbeddedRight> rights = user.getRights();
        for (EmbeddedRight right : rights) {
            if (right.getName().equals(name)) {
                hasRight = true;
                break;
            }
        }
        return hasRight;
    }

    /**
     * Method for create user right by user right data transfer object
     * @param userId user id from request params
     * @param userRightDTO user right data transfer object
     * @return created right document
     * @throws DataException throw if user document can be found
     */
    public EmbeddedRight createUserRight(
            ObjectId userId,
            UserRightDTO userRightDTO
    ) throws DataException {
        User user = getUserDocument(userId);
        boolean hasRight = hasRight(userRightDTO.getName(), user);
        if (!hasRight) {
            EmbeddedRight userRight = new EmbeddedRight(
                    userRightDTO.getName(),
                    userRightDTO.getCreate(),
                    userRightDTO.getRead(),
                    userRightDTO.getUpdate(),
                    userRightDTO.getDelete()
            );
            List<EmbeddedRight> rights = user.getRights();
            rights.add(userRight);
            save(user);
            return userRight;
        } else {
            Error error = new Error("Can not create user right");
            throw new DataException("CanNotCreate", error);
        }
    }
   
    /**
     * Method for remove user right from rights list in user document
     * @param rightId right id from request
     * @param userId user id from request
     * @return actual right list
     * @throws DataException throw if can user document can not be found
     */
    public List<EmbeddedRight> removeRight(
            ObjectId rightId,
            ObjectId userId
    ) throws DataException {
        User user = getUserDocument(userId);
        List<EmbeddedRight> rights = user.getRights();
        Iterator iterator = rights.iterator();
        String toCheck = rightId.toString();
        while(iterator.hasNext()) {
            EmbeddedRight right = (EmbeddedRight) iterator.next();
            if (right.getId().equals(toCheck) &&
                    !getBlackList().contains(right.getName())) {
                iterator.remove();
            }
        }
        save(user);
        return user.getRights();
    }
    
    /**
     * MEthod for update user right document
     * @param rightId right id from request
     * @param userId user id from request
     * @param userRightDTO user right data transfer obejct
     * @return updated right document
     * @throws DataException throw if user document con not be found
     */
    public EmbeddedRight updateUserRight(
            ObjectId rightId, 
            ObjectId userId,
            UserRightDTO userRightDTO
    ) throws DataException {
        User user = getUserDocument(userId);
        List<EmbeddedRight> rights = user.getRights();
        EmbeddedRight right = Searcher
                .getUserRightByIdFromList(rightId, rights);
        
        if (userRightDTO.getCreate() != null && 
                !right.getCreate().equals(userRightDTO.getCreate())) {
            right.setCreate(userRightDTO.getCreate());
        }
        
        if (userRightDTO.getRead() != null && 
                !right.getRead().equals(userRightDTO.getRead())) {
            right.setRead(userRightDTO.getRead());
        }
        
        if (userRightDTO.getUpdate() != null &&
                !right.getUpdate().equals(userRightDTO.getUpdate())) {
            right.setUpdate(userRightDTO.getUpdate());
        }
        
        if (userRightDTO.getDelete() != null &&
                !right.getDelete().equals(userRightDTO.getDelete())) {
            right.setDelete(userRightDTO.getCreate());
        }
        
        save(user);
        return right;
    }
    
    /**
     * Method for get full user document
     * @param userId user id from request params
     * @return user document
     * @throws DataException throw if can not find list of user properties
     */
    public User getUserDocument(ObjectId userId) throws DataException {
        String[] excludes = new String[]{};
        UsersFilter filter = new UsersFilter(userId, excludes);
        User user = findOneById(filter);
        if (user != null) {
            return user;
        } else {
            Error error = new Error("Can not find user by id from request");
            throw new DataException("NotFound", error);
        }
    }
}
