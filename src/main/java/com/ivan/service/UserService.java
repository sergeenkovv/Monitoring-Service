package com.ivan.service;

import com.ivan.model.entity.MeterReading;
import com.ivan.model.entity.Player;
import com.ivan.model.types.MeterType;

public interface UserService {
    void submitMeterReading(String username, MeterType meterType, Integer counter);
}
