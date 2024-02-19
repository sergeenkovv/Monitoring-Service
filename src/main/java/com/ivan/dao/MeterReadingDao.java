package com.ivan.dao;

import com.ivan.model.entity.MeterReading;

import java.util.List;

/**
 * The MeterReadingDao interface extends the GeneralDao interface for handling operations related to meter readings.
 * It provides methods for finding meter readings by player ID and by player ID, year, and month.
 */
public interface MeterReadingDao extends GeneralDao<Long, MeterReading> {

    /**
     * Finds all meter readings for a specific player based on the player ID.
     *
     * @param playerId The ID of the player for whom to retrieve meter readings.
     * @return List of meter readings belonging to the specified player.
     */
    List<MeterReading> findAllByPlayerId(Long playerId);

    /**
     * Finds all meter readings for a specific player based on the player ID, year, and month.
     *
     * @param playerId The ID of the player for whom to retrieve meter readings.
     * @param year The year for which to retrieve meter readings.
     * @param month The month for which to retrieve meter readings.
     * @return List of meter readings belonging to the specified player in the specified year and month.
     */
    List<MeterReading> findAllByPlayerIdAndYearMonth(Long playerId, Integer year, Integer month);
}