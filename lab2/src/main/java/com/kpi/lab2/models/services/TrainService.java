package com.kpi.lab2.models.services;

import com.kpi.lab2.models.daos.TrainDao;
import com.kpi.lab2.models.entities.Train;

import java.util.List;

public class TrainService extends EntityServiceBase<Train, TrainDao> {

    public TrainService(TrainDao entityDao) {
        super(entityDao);
    }

    public List<Train> findByNumber(int readNumber) {
        return entityDao.findByNumber(readNumber);
    }
}
