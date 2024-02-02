package com.ivan.service.impl;

import com.ivan.dao.MeterReadingDao;
import com.ivan.exception.DuplicateReadingsException;
import com.ivan.model.entity.MeterReading;
import com.ivan.model.types.MeterType;
import com.ivan.service.PlayerService;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;

@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final MeterReadingDao meterReadingDao;

    public List<MeterReading> getCurrentMeterReadings(Long playerId) {
        return getMeterReadingsByMonth(playerId, LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue());
    }

    public void submitMeterReading(Long playerId, String meterType, Integer counter) {
        if (isValidMeterReadings(playerId, MeterType.valueOf(meterType))) {
            throw new DuplicateReadingsException("You have already submitted a reading this month");
        }

        MeterReading meterReading = MeterReading.builder()
                .meterType(MeterType.valueOf(meterType))
                .counter(counter)
                .date(YearMonth.now())
                .playerId(playerId)
                .build();

        meterReadingDao.save(meterReading);
    }

    public List<MeterReading> getMeterReadingsByMonth(Long playerId, Integer year, Integer month) {
        return meterReadingDao.findAllByPlayerIdAndYearMonth(playerId, year, month);
    }

    public List<MeterReading> getMeterReadingHistory(Long playerId) {
        return meterReadingDao.findAllByPlayerId(playerId);
    }

    private boolean isValidMeterReadings(Long playerId, MeterType meterType) {
        List<MeterReading> allByPlayerUsername = meterReadingDao.findAllByPlayerId(playerId);

        return allByPlayerUsername.stream()
                .anyMatch(reading -> reading.getMeterType() == meterType &&
                                     YearMonth.from(reading.getDate()).equals(YearMonth.now()));
    }
}