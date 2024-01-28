package com.ivan.validator;

import com.ivan.dao.MeterReadingDao;
import com.ivan.model.entity.MeterReading;
import com.ivan.model.types.MeterType;

import java.time.YearMonth;
import java.util.List;

public class UserValidator {

    private final MeterReadingDao meterReadingDao;

    public UserValidator(MeterReadingDao meterReadingDao) {
        this.meterReadingDao = meterReadingDao;
    }

    public boolean isValid(String username, MeterType meterType) {
        List<MeterReading> allByPlayerUsername = meterReadingDao.findAllByPlayerUsername(username);

        for (MeterReading reading : allByPlayerUsername) {
            if (reading.getMeterType() == meterType && YearMonth.from(reading.getDate()).equals(YearMonth.now())) {
                return true;
            }
        }
        return false;
    }
}