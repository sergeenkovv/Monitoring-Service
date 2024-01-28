package com.ivan.service.impl;

import com.ivan.dao.MeterReadingDao;
import com.ivan.model.entity.MeterReading;
import com.ivan.model.types.MeterType;
import com.ivan.service.PlayerService;
import com.ivan.validator.UserValidator;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

public class PlayerServiceImpl implements PlayerService {

    private final UserValidator userValidator;
    private final MeterReadingDao meterReadingDao;

    public PlayerServiceImpl(UserValidator userValidator, MeterReadingDao meterReadingDao) {
        this.userValidator = userValidator;
        this.meterReadingDao = meterReadingDao;
    }

    public List<MeterReading> getCurrentMeterReadings(String username) {
        return getMeterReadingsByMonth(username, LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue());
    }

    public void submitMeterReading(String username, MeterType meterType, Integer counter) {
        if (userValidator.isValid(username, meterType)) {
            System.out.println("вы уже отправляли показание в этом месяце");
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