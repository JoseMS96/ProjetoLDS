package br.fai.lds.client.service;

import java.util.List;

//generics
public interface BaseService<T> {

    int create(T entity);

    List<T> find();

    T findById(int id);

    boolean update(int id, T entity);

    boolean deleteById(int id);

}
