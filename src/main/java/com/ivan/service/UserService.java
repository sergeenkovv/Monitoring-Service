package com.ivan.service;

import com.ivan.model.entity.MeterReading;
import com.ivan.model.entity.Player;
import com.ivan.model.types.MeterType;

import java.time.YearMonth;
import java.util.List;

public interface UserService {

    List<MeterReading> getCurrentMeterReadings(String username);
    void submitMeterReading(String username, MeterType meterType, Integer counter);
    List<MeterReading> getMeterReadingsByMonth(String username, YearMonth date);
    List<MeterReading> getMeterReadingHistory(String username);
}
