package com.kpi.lab2.models.daos;

import java.util.List;

public interface EntityDao<T> {

    T findById(Long id);

    List<T> findAll();

    T save(T entity);

    void delete(T entity);
}
