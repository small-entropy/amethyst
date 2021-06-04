package Sources;

import Sources.Core.MorphiaSource;
import DataTransferObjects.UserDTO;
import Filters.UsersFilter;
import Models.Standalones.User;
import Utils.common.JsonWebToken;
import dev.morphia.Datastore;
import dev.morphia.query.FindOptions;
import static dev.morphia.query.experimental.filters.Filters.and;
import static dev.morphia.query.experimental.filters.Filters.eq;
import java.util.Arrays;
import java.util.List;
import org.bson.types.ObjectId;

/**
 * Source for work with user collection
 * @author small-entropy
 */
public class UsersSource extends MorphiaSource<User, UsersFilter, UserDTO> {
    
    /**
     * Constuctor users collection sources
     * @param datastore Morphia datastore object
     */
    public UsersSource(Datastore datastore) {
        super(datastore, User.class);
    }
    
    /**
     * Method for find user by username
     * @param filter filter object
     * @return founded user document
     */
    public User findOneByUsername(UsersFilter filter) {
        // Create find optoions for Morphia
        FindOptions findOptions = new FindOptions()
                .projection()
                .exclude(filter.getExcludes());
        // Find in database by status & username
        return getStore()
                .find(getModelClass())
                .filter(and(
                        eq("status", filter.getStatus()),
                        eq("username", filter.getUsername())
                ))
                .first(findOptions);
    }
    
    /**
     * Method for craete (register) user in databse
     * @param dto user data transfer object
     * @return user document
     */
    @Override
    public User create(UserDTO dto) {
        // Create user document
        User user = new User(
                new ObjectId(),
                dto.getUsername(),
                dto.getPassword(),
                dto.getProperties(),
                dto.getProfile(),
                dto.getRights()
        );
        // Generate JWT token
        String token = JsonWebToken.encode(user);
        // Get tokens list
        List<String> tokens = Arrays.asList(token);
        // Set issued tokens list
        user.setIssuedTokens(tokens);
        // Save user document in database
        save(user);
        // Return user document
        return user;
    }
}
