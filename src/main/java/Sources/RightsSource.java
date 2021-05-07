/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sources;

import Exceptions.DataException;
import Models.User;
import Models.UserRight;
import Sources.Abstract.AbstractChildUserSource;
import dev.morphia.Datastore;
import java.util.List;

/**
 * Class for user datasource
 * @author small-entory
 */
public class RightsSource extends AbstractChildUserSource {
    
    /**
     * Constructor for right datasource object
     * @param datastore Morphia datastore object
     */
    public RightsSource(Datastore datastore) {
        super(datastore);
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
}
