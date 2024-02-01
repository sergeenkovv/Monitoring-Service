package com.ivan.dao;

import com.ivan.model.entity.MeterReading;

import java.util.List;

public interface MeterReadingDao extends GeneralDao<Long, MeterReading> {

    List<MeterReading> findAllByPlayerId(Long playerId);

    List<MeterReading> findAllByPlayerIdAndYearMonth(Long playerId, int year, int month);
}