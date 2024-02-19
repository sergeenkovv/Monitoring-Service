package com.ivan.controller;

import com.ivan.exception.NotValidArgumentException;
import com.ivan.model.entity.MeterReading;
import com.ivan.model.entity.Player;
import com.ivan.model.types.MeterType;
import com.ivan.service.PlayerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Arrays;
import java.util.List;

/**
 * The PlayerController class contains endpoints for handling player actions related to meter readings.
 * It provides functionality to retrieve meter readings, submit new meter readings, and view reading history.
 */
@Slf4j
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    /**
     * Retrieves the current meter readings for a specific player.
     *
     * @param player The player for whom to retrieve the current meter readings.
     * @return List of current meter readings.
     */
    public List<MeterReading> showCurrentMeterReadings(Player player) {
        log.info("The player" + player.getUsername() + "trying to get meter readings for the current month.");
        return playerService.getCurrentMeterReadings(player.getId());
    }

    /**
     * Submits a new meter reading for a specific player.
     *
     * @param player    The player submitting the meter reading.
     * @param meterType The type of meter reading.
     * @param counter   The meter reading value.
     * @throws NotValidArgumentException If the meter reading type or counter is not valid.
     */
    public void submitMeterReading(Player player, String meterType, String counter) {
        if (!isValidMeterType(meterType)) {
            throw new NotValidArgumentException("You have entered an incorrect meter reading type. Check the correct spelling of the meter reading type.");
        }

        if (!isValidNum(counter)) {
            throw new NotValidArgumentException("You entered an unknown value. Make sure the number you enter contains only digits. The number must be greater than or equal to 0.");
        }

        log.info("The player " + player + " is trying to send meter readings.");
        playerService.submitMeterReading(player.getId(), meterType, Integer.parseInt(counter));
    }

    /**
     * Retrieves meter readings for a player based on the specified year and month.
     *
     * @param player The player for whom to retrieve meter readings.
     * @param year   The year for which to retrieve meter readings.
     * @param month  The month for which to retrieve meter readings.
     * @return List of meter readings for the specified year and month.
     * @throws NotValidArgumentException If the year or month is not valid.
     */
    public List<MeterReading> showMeterReadingsByMonth(Player player, String year, String month) {
        if (!isValidNum(year, month)) {
            throw new NotValidArgumentException("You entered an unknown value. Make sure the number you enter is digits only.");
        }

        log.info("The player" + player.getUsername() + "trying to get meter readings for the " + year + month + ".");
        return playerService.getMeterReadingsByMonth(player.getId(), Integer.parseInt(year), Integer.parseInt(month));
    }

    /**
     * Retrieves the meter reading history for a specific player.
     *
     * @param player The player for whom to retrieve the meter reading history.
     * @return List of meter reading history entries.
     */
    public List<MeterReading> showMeterReadingHistory(Player player) {
        log.info("The player" + player.getUsername() + "trying to get get the history of sending meter readings.");
        return playerService.getMeterReadingHistory(player.getId());
    }

    /**
     * Validates the meter type against the values defined in the MeterType enum.
     *
     * @param meterType The meter type to validate.
     * @return True if the meter type is valid, false otherwise.
     */
    private boolean isValidMeterType(String meterType) {
        return Arrays.stream(MeterType.values())
                .anyMatch(type -> meterType.equals(type.name()));
    }

    /**
     * Validates if the input strings contain only digits.
     *
     * @param inputs The input strings to validate.
     * @return True if all input strings contain only digits greater than or equal to 0, false otherwise.
     */
    private boolean isValidNum(String... inputs) {
        return Arrays.stream(inputs)
                .allMatch(input -> input.matches("\\d+"));
    }
}