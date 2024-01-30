package com.ivan.service.impl;

import com.ivan.dao.MeterReadingDao;
import com.ivan.exception.DuplicateReadingsException;
import com.ivan.model.entity.MeterReading;
import com.ivan.model.types.MeterType;
import com.ivan.service.PlayerService;
import com.ivan.validator.PlayerValidator;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

public class PlayerServiceImpl implements PlayerService {

    private final PlayerValidator playerValidator;
    private final MeterReadingDao meterReadingDao;

    public PlayerServiceImpl(PlayerValidator playerValidator, MeterReadingDao meterReadingDao) {
        this.playerValidator = playerValidator;
        this.meterReadingDao = meterReadingDao;
    }

    public List<MeterReading> getCurrentMeterReadings(String username) {
        return getMeterReadingsByMonth(username, LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue());
    }

    public void submitMeterReading(String username, MeterType meterType, Integer counter) {
        if (playerValidator.isValid(username, meterType)) {
            throw new DuplicateReadingsException("You have already submitted a reading this month");
        }

        MeterReading meterReading = MeterReading.builder()
                .username(username)
                .meterType(meterType)
                .counter(counter)
                .date(YearMonth.now())
                .build();

        meterReadingDao.save(meterReading);
    }

    public List<MeterReading> getMeterReadingsByMonth(String username, Integer year, Integer month) {
        return meterReadingDao.findAllByPlayerUsernameAndYearMonth(username, year, month);
    }

    public List<MeterReading> getMeterReadingHistory(String username) {
        return meterReadingDao.findAllByPlayerUsername(username);
    }
}