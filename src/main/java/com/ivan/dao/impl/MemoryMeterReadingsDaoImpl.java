package com.ivan.dao.impl;

import com.ivan.dao.MeterReadingDao;
import com.ivan.model.entity.MeterReading;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The MemoryMeterReadingsDaoImpl class is an implementation of the MeterReadingDao interface
 * that stores meter readings in memory using a HashMap.
 * It provides methods for retrieving meter readings by player ID, player ID with a specific year and month,
 * and saving new meter readings.
 * The MemoryMeterReadingsDaoImpl class simulates working with a database. But instead of a database, collections are used.
 */
public class MemoryMeterReadingsDaoImpl implements MeterReadingDao {

    private final Map<Long, MeterReading> readingMap = new HashMap<>();
    private Long id = 1L;

    /**
     * Retrieves all meter readings for a specific player based on the player ID.
     *
     * @param playerId The ID of the player for whom to retrieve meter readings.
     * @return List of meter readings belonging to the specified player.
     */
    @Override
    public List<MeterReading> findAllByPlayerId(Long playerId) {
        List<MeterReading> result = new ArrayList<>();

        for (MeterReading reading : readingMap.values()) {
            if (reading.getPlayerId().equals(playerId)) {
                result.add(reading);
            }
        }
        return result;
    }

    /**
     * Retrieves all meter readings for a specific player based on the player ID, year, and month.
     *
     * @param playerId The ID of the player for whom to retrieve meter readings.
     * @param year The year for which to retrieve meter readings.
     * @param month The month for which to retrieve meter readings.
     * @return List of meter readings belonging to the specified player in the specified year and month.
     */
    @Override
    public List<MeterReading> findAllByPlayerIdAndYearMonth(Long playerId, Integer year, Integer month) {
        List<MeterReading> result = new ArrayList<>();

        for (MeterReading reading : readingMap.values()) {
            if (reading.getPlayerId().equals(playerId)
                && reading.getDate().getYear() == year
                && reading.getDate().getMonthValue() == month) {
                result.add(reading);
            }
        }
        return result;
    }

    /**
     * Saves a new meter reading in memory.
     *
     * @param reading The meter reading to save.
     * @return The saved meter reading object.
     */
    @Override
    public MeterReading save(MeterReading reading) {
        reading.setId(getLastId());
        incrementId();
        readingMap.put(reading.getId(), reading);
        return readingMap.get(reading.getId());
    }

    private Long getLastId() {
        return id;
    }

    private void incrementId() {
        id++;
    }
}