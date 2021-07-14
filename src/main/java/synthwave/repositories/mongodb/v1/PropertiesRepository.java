/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package synthwave.repositories.mongodb.v1;

import synthwave.repositories.mongodb.base.BaseUserPropertyRepository;
import dev.morphia.Datastore;
import java.util.List;

/**
 * Class of source for user properties documents
 * @author small-entropy
 */
public class PropertiesRepository extends BaseUserPropertyRepository {
    
    /**
     * Constructor for source
     * @param datastore Morphia datastore object
     * @param blaclList blaclist for profile documents
     */
    public PropertiesRepository(Datastore datastore, List<String> blaclList) {
        super(datastore, "properties", blaclList);
    }
}   