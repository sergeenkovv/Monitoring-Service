package com.ivan.service;

import com.ivan.model.entity.MeterReading;
import com.ivan.model.types.MeterType;

import java.util.List;

public interface PlayerService {

    List<MeterReading> getCurrentMeterReadings(String username);

    void submitMeterReading(String username, MeterType meterType, Integer counter);

    List<MeterReading> getMeterReadingHistory(String username);

    List<MeterReading> getMeterReadingsByMonth(String username, Integer year, Integer month);
}