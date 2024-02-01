package com.ivan.controller;

import com.ivan.exception.NotValidArgumentException;
import com.ivan.model.entity.MeterReading;
import com.ivan.model.entity.Player;
import com.ivan.model.types.MeterType;
import com.ivan.service.PlayerService;
import com.ivan.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class MainController {

    private final SecurityService securityService;
    private final PlayerService playerService;

    public Player register(String login, String password) {
        log.info("The player trying to register with login " + login + " and password " + password);
        if (login == null || password == null || login.isEmpty() || login.isBlank() || password.isEmpty() || password.isBlank()) {
            throw new NotValidArgumentException("The password or login cannot be empty or consist of only spaces");
        }

        if (password.length() < 3 || password.length() > 32) {
            throw new NotValidArgumentException("The password must be between 3 and 32 characters long.");
        }

        return securityService.registration(login, password);
    }

    public Player authorize(String login, String password) {
        log.info("The player trying to log in with login " + login + " and password " + password);
        return securityService.authorization(login, password);
    }

    public List<MeterReading> showCurrentMeterReadings(Player player) {
        log.info("The player" + player.getUsername() + "trying to get meter readings for the current month");
        return playerService.getCurrentMeterReadings(player.getId());
    }

    public void submitMeterReading(Player player, MeterType meterType, Integer counter) {
        log.info("The player " + player + " is trying to send meter readings");
        playerService.submitMeterReading(player.getId(), meterType, counter);
    }

    public List<MeterReading> showMeterReadingsByMonth(Player player, Integer year, Integer month) {
        log.info("The player" + player.getUsername() + "trying to get meter readings for the " + year + month);
        return playerService.getMeterReadingsByMonth(player.getId(), year, month);
    }

    public List<MeterReading> showMeterReadingHistory(Player player) {
        log.info("The player" + player.getUsername() + "trying to get get the history of sending meter readings");
        return playerService.getMeterReadingHistory(player.getId());
    }
}