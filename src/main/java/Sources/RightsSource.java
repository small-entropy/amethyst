/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sources;

import DataTransferObjects.UserRightDTO;
import Exceptions.DataException;
import Models.User;
import Models.UserRight;
import Sources.Abstract.AbstractChildUserSource;
import dev.morphia.Datastore;
import java.util.Iterator;
import java.util.List;

/**
 * Class for user datasource
 * @author small-entory
 */
public class RightsSource extends AbstractChildUserSource {
    
    private final List<String> blackList;
    
    /**
     * Constructor for right datasource object
     * @param datastore Morphia datastore object
     * @param blackList
     */
    public RightsSource(Datastore datastore, List<String> blackList) {
        super(datastore);
        this.blackList = blackList;
    }

    public List<String> getBlackList() {
        return blackList;
    }
    
    /**
     * Method for get user right for rights list by id
     * @param rightIdParam right id as string
     * @param rights rights list
     * @return founded right
     * @throws DataException throw if right not found
     */
    public UserRight getRightByIdParam(String rightIdParam, List<UserRight> rights) throws DataException {
        UserRight result = null;
        for (UserRight right : rights) {
            if (right.getId().toString().equals(rightIdParam)) {
                result = right;
                break;
            }
        }
        
        
        if (result != null) {
            return result;
        } else {
            Error error = new Error("Can not find user right by request params");
            throw new DataException("NotFound", error);
        }
    }
    
    /**
     * Method for get user right by id param from request
     * @param rightIdParam user right id as string
     * @param idParam user id as string
     * @return user right document
     * @throws DataException throw if not found user or right
     */
    public UserRight getRightByIdParam(String rightIdParam, String idParam) throws DataException {
        List<UserRight> rights = getList(idParam);
        return getRightByIdParam(rightIdParam, rights);
    }
    
    /**
     * Method for get user right by user id
     * @param idParam user id as string
     * @return list of user rights
     * @throws DataException throw if not founded user or rights
     */
    public List<UserRight> getList(String idParam) throws DataException {
        User user = getUserDocument(idParam);
        List<UserRight> rights = user.getRights();
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
        List<UserRight> rights = user.getRights();
        for (UserRight right : rights) {
            if (right.getName().equals(name)) {
                hasRight = true;
                break;
            }
        }
        return hasRight;
    }

    /**
     * Method for create user right by user right data transfer object
     * @param idParam user id from request params
     * @param userRightDTO user right data transfer object
     * @return created right document
     * @throws DataException throw if user document can be found
     */
    public UserRight createUserRight(String idParam, UserRightDTO userRightDTO) throws DataException {
        User user = getUserDocument(idParam);
        boolean hasRight = hasRight(idParam, user);
        if (!hasRight) {
            UserRight userRight = new UserRight(
                    userRightDTO.getName(),
                    userRightDTO.getCreate(),
                    userRightDTO.getRead(),
                    userRightDTO.getUpdate(),
                    userRightDTO.getDelete()
            );
            List<UserRight> rights = user.getRights();
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
     * @param rightIdParam right id from request
     * @param idParam user id from request
     * @return actual right list
     * @throws DataException throw if can user document can not be found
     */
    public List<UserRight> removeRight(String rightIdParam, String idParam) throws DataException {
        User user = getUserDocument(idParam);
        List<UserRight> rights = user.getRights();
        Iterator iterator = rights.iterator();
        while(iterator.hasNext()) {
            UserRight right = (UserRight) iterator.next();
            if (right.getId().toString().equals(rightIdParam) &&
                    !getBlackList().contains(right.getName())) {
                iterator.remove();
            }
        }
        save(user);
        return user.getRights();
    }
    
    /**
     * MEthod for update user right document
     * @param rightIdParam right id from request
     * @param idParam user id from request
     * @param userRightDTO user right data transfer obejct
     * @return updated right document
     * @throws DataException throw if user document con not be found
     */
    public UserRight updateUserRight(String rightIdParam, String idParam, UserRightDTO userRightDTO) throws DataException {
        User user = getUserDocument(idParam);
        List<UserRight> rights = user.getRights();
        UserRight right = getRightByIdParam(rightIdParam, rights);
        
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
}
