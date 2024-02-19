package com.ivan.service;

import com.ivan.model.entity.MeterReading;

import java.util.List;

/**
 * The PlayerService interface defines methods for handling player-related actions like retrieving current meter readings,
 * submitting new meter readings, fetching meter reading history, and getting meter readings by a specific month.
 */
public interface PlayerService {

    /**
     * Retrieves the current meter readings for a specific player.
     *
     * @param playerId The ID of the player for whom to retrieve the current meter readings.
     * @return List of current meter readings for the specified player.
     */
    List<MeterReading> getCurrentMeterReadings(Long playerId);

    /**
     * Submits a new meter reading for a player.
     *
     * @param playerId The ID of the player submitting the meter reading.
     * @param meterType The type of the meter reading.
     * @param counter The meter reading value.
     */
    void submitMeterReading(Long playerId, String meterType, Integer counter);

    /**
     * Retrieves the meter reading history for a specific player.
     *
     * @param playerId The ID of the player for whom to retrieve the meter reading history.
     * @return List of meter reading history entries for the specified player.
     */
    List<MeterReading> getMeterReadingHistory(Long playerId);

    /**
     * Retrieves meter readings for a player based on the specified year and month.
     *
     * @param playerId The ID of the player for whom to retrieve meter readings.
     * @param year The year for which to retrieve meter readings.
     * @param month The month for which to retrieve meter readings.
     * @return List of meter readings for the specified player in the specified year and month.
     */
    List<MeterReading> getMeterReadingsByMonth(Long playerId, Integer year, Integer month);
}