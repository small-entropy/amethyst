package Services.base;

import DataTransferObjects.v1.RuleDTO;
import Filters.common.UsersFilter;
import Models.Standalones.User;
import Repositories.v1.UsersRepository;
import Utils.v1.RightManager;
import dev.morphia.Datastore;
import org.bson.types.ObjectId;
import spark.Request;

/**
 * 
 * @author small-entropey
 */
public abstract class BaseDocumentService <R> extends BaseService<R>{
   UsersRepository usersRepository;
   
   public BaseDocumentService(Datastore datastore, R repository) {
       super(repository);
       this.usersRepository = new UsersRepository(datastore);
   }
   public BaseDocumentService(
           Datastore datastore,
           R repository,
           String[] globalExcludes,
           String[] publicExcludes,
           String[] privateExcludes
   ) {
       super(repository, globalExcludes, publicExcludes, privateExcludes);
       this.usersRepository = new UsersRepository(datastore);
   }

    public UsersRepository getUsersRepository() {
        return usersRepository;
    }

    public void setUsersRepository(UsersRepository usersRepository) {
        this.usersRepository = usersRepository;
    }
    
    protected User getUserById(ObjectId id) {
        UsersFilter filter = new UsersFilter(id, new String[] {});
        return getUsersRepository().findOneById(filter);
    }
    
    protected RuleDTO getRule(
            Request request,
            String right,
            String action
    ) {
        return RightManager.getRuleByRequest_Token(
                request, 
                getUsersRepository(), 
                right, 
                action
        );
    }
}
