package com.ivan.service;

import com.ivan.model.entity.MeterReading;
import com.ivan.model.types.MeterType;

import java.util.List;

public interface PlayerService {

    List<MeterReading> getCurrentMeterReadings(Long playerId);

    void submitMeterReading(Long playerId, String meterType, Integer counter);

    List<MeterReading> getMeterReadingHistory(Long playerId);

    List<MeterReading> getMeterReadingsByMonth(Long playerId, Integer year, Integer month);
}