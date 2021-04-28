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
 */
public interface DatasourceInterface<D, M, I> {
    public D getDatastore();
    public List<M> findAll(int skip, int limit, String[] excludes);
    public M findOne(I id, String[] excludes);
}
