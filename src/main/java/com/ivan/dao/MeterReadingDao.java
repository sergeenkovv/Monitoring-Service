package com.ivan.dao;

import com.ivan.model.entity.MeterReading;

import java.util.List;

public interface MeterReadingDao extends GeneralDao<Long, MeterReading> {

    List<MeterReading> findAllByPlayerUsername(String playerName);

    List<MeterReading> findAllByPlayerUsernameAndYearMonth(String playerName, int year, int month);

}