package com.kpi.lab2.models.services;

import com.kpi.lab2.models.daos.EntityDao;
import com.kpi.lab2.models.entities.Entity;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class EntityServiceBase<E extends Entity, D extends EntityDao<E>> implements EntityService<E> {
    protected D entityDao;

    @Override
    public E findById(Long id) {
        return entityDao.findById(id);
    }

    @Override
    public List<E> findAll() {
        return entityDao.findAll();
    }

    @Override
    public E create(E entity) {
        return entityDao.save(entity);
    }

    @Override
    public void update(Long id, E entity) {
        if (id == null || !id.equals(entity.getId())) {
            throw new IllegalArgumentException("Invalid ID of the entity");
        }
        entityDao.save(entity);
    }

    @Override
    public void delete(E entity) {
        entityDao.delete(entity);
    }
}
