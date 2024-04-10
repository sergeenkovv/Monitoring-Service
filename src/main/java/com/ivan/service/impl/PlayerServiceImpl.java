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

/**
 * The PlayerServiceImpl class implements the PlayerService interface for handling player-related operations.
 * It provides functionality to retrieve current meter readings, submit new meter readings,
 * retrieve meter readings by month, and fetch meter reading history.
 */
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final MeterReadingDao meterReadingDao;

    /**
     * Retrieves the current meter readings for a player.
     *
     * @param playerId The ID of the player for whom to retrieve the current meter readings.
     * @return List of current meter readings for the specified player.
     */
    public List<MeterReading> getCurrentMeterReadings(Long playerId) {
        return getMeterReadingsByMonth(playerId, LocalDateTime.now().getYear(), LocalDateTime.now().getMonthValue());
    }

    /**
     * Submits a new meter reading for a player.
     *
     * @param playerId The ID of the player submitting the meter reading.
     * @param meterType The type of the meter reading.
     * @param counter The meter reading value.
     */
    public void submitMeterReading(Long playerId, String meterType, Integer counter) {
        if (!isValidMeterReadings(playerId, MeterType.valueOf(meterType))) {
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

    /**
     * Retrieves meter readings for a player based on the specified year and month.
     *
     * @param playerId The ID of the player for whom to retrieve meter readings.
     * @param year The year for which to retrieve meter readings.
     * @param month The month for which to retrieve meter readings.
     * @return List of meter readings for the specified player in the specified year and month.
     */
    public List<MeterReading> getMeterReadingsByMonth(Long playerId, Integer year, Integer month) {
        return meterReadingDao.findAllByPlayerIdAndYearMonth(playerId, year, month);
    }

    /**
     * Retrieves the meter reading history for a specific player.
     *
     * @param playerId The ID of the player for whom to retrieve the meter reading history.
     * @return List of meter reading history entries for the specified player.
     */
    public List<MeterReading> getMeterReadingHistory(Long playerId) {
        return meterReadingDao.findAllByPlayerId(playerId);
    }

    /**
     * Checks if the player has already submitted a meter reading of the same type in the current month.
     *
     * @param playerId The ID of the player to check.
     * @param meterType The meter reading type to check.
     * @return True if a reading of the same type already exists for the player in the current month, false otherwise.
     */
    private boolean isValidMeterReadings(Long playerId, MeterType meterType) {
        List<MeterReading> allByPlayerUsername = meterReadingDao.findAllByPlayerId(playerId);

        for (MeterReading reading : allByPlayerUsername) {
            if (reading.getMeterType() == meterType &&
                YearMonth.from(reading.getDate()).equals(YearMonth.now())) {
                return false;
            }
        }
        return true;
    }
}
//        return allByPlayerUsername.stream()
//                .anyMatch(reading -> reading.getMeterType() == meterType &&
//                YearMonth.from(reading.getDate()).equals(YearMonth.now()));
