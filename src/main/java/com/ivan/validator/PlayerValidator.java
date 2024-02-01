package com.ivan.validator;

import com.ivan.dao.MeterReadingDao;
import com.ivan.model.entity.MeterReading;
import com.ivan.model.types.MeterType;
import lombok.RequiredArgsConstructor;

import java.time.YearMonth;
import java.util.List;

@RequiredArgsConstructor
public class PlayerValidator {

    private final MeterReadingDao meterReadingDao;

    public boolean isValid(Long playerId, MeterType meterType) {
        List<MeterReading> allByPlayerUsername = meterReadingDao.findAllByPlayerId(playerId);

        for (MeterReading reading : allByPlayerUsername) {
            if (reading.getMeterType() == meterType && YearMonth.from(reading.getDate()).equals(YearMonth.now())) {
                return true;
            }
        }
        return false;
    }
}