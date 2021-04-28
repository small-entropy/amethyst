/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Datasources;

import java.util.List;

/**
 *
 * @author igrav
 * @param <D>
 */
public abstract class AbstractDatasource<D, M, I> implements DatasourceInterface<D, M, I>{
    // datastore property
    private final D datastore;
   
    AbstractDatasource(D datastore) {
        this.datastore = datastore;
    }
    
    @Override
    public final D getDatastore() {
        return datastore;
    }
}
