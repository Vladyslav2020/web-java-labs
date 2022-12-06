package com.kpi.lab2.models.services;

import java.util.List;

public interface EntityService<T> {
    T findById(Long id);

    List<T> findAll();

    T create(T entity);

    void update(Long id, T entity);

    void delete(T entity);
}
