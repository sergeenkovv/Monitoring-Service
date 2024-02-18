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

@Slf4j
@RequiredArgsConstructor
public class PlayerController {

    private final PlayerService playerService;

    public List<MeterReading> showCurrentMeterReadings(Player player) {
        log.info("The player" + player.getUsername() + "trying to get meter readings for the current month.");
        return playerService.getCurrentMeterReadings(player.getId());
    }

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

    public List<MeterReading> showMeterReadingsByMonth(Player player, String year, String month) {
        if (!isValidNum(year, month)) {
            throw new NotValidArgumentException("You entered an unknown value. Make sure the number you enter is digits only.");
        }

        log.info("The player" + player.getUsername() + "trying to get meter readings for the " + year + month + ".");
        return playerService.getMeterReadingsByMonth(player.getId(), Integer.parseInt(year), Integer.parseInt(month));
    }

    public List<MeterReading> showMeterReadingHistory(Player player) {
        log.info("The player" + player.getUsername() + "trying to get get the history of sending meter readings.");
        return playerService.getMeterReadingHistory(player.getId());
    }

    private boolean isValidMeterType(String meterType) {
        return Arrays.stream(MeterType.values())
                .anyMatch(type -> meterType.equals(type.name()));
    }

    private boolean isValidNum(String... inputs) {
        return Arrays.stream(inputs)
                .allMatch(input -> input.matches("\\d+"));
    }
}