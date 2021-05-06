/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Sources;

import Sources.Abstract.AbstractPropertySource;
import dev.morphia.Datastore;
import java.util.List;

/**
 * Class of source for profile documents
 * @author small-entropy
 */
public class ProfileSource extends AbstractPropertySource {
    
    /**
     * Constructor for source
     * @param datastore Morphia datastore object
     * @param blaclList blaclist for profile documents
     */
    public ProfileSource(Datastore datastore, List<String> blaclList) {
        super(datastore, "profile", blaclList);
    }
}
