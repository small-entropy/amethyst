/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Interfaces;

import Filters.Filter;
import java.util.List;

/**
 * Interface for Source object
 * @author entropy
 * @param <S> type of datastore
 * @param <M> type of model
 * @param <I> type of entities ID
 * @param <F> type of filter
 * @param <D> type of data transfer object
 */
public interface SourceInterface<S, M, I, F extends Filter, D> {
    public Class<M> getModelClass();
    public S getStore();
    public List<M> findAll(F filter);
    public M findOneById(F filter);
    public M create(D dto);
    public void save(M data);
}
