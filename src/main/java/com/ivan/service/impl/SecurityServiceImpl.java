package com.ivan.service.impl;

import com.ivan.model.entity.MeterReading;
import com.ivan.model.entity.Player;
import com.ivan.model.types.Role;
import com.ivan.service.SecurityService;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SecurityServiceImpl implements SecurityService {

    @Getter
    private final Map<String, Player> playerMap;
    @Getter
    private final Map<String, MeterReading> meterReadingMap;
    @Getter
    private final Map<String, List<MeterReading>> userMeterReadings;
    @Getter
    private final Map<String, List<String>> userActions;

    public SecurityServiceImpl() {
        this.playerMap = new HashMap<>();
        this.userMeterReadings = new HashMap<>();
        this.userActions = new HashMap<>();
        playerMap.put("admin", Player.builder()
                .username("admin")
                .password("123")
                .role(Role.ADMIN)
                .readingsList(new ArrayList<>())
                .build());
        this.meterReadingMap = new HashMap<>();
    }

    @Override
    public void registration(String username, String password) {
        if (playerMap.containsKey(username)) {
            System.out.println("игрок уже существует");
            return;
        }

        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("вы ввели пустое значение");
            return;
        }

        Player newPlayer = Player.builder()
                .username(username)
                .password(password)
                .role(Role.PLAYER)
                .readingsList(new ArrayList<>())
                .build();
        playerMap.put(username, newPlayer);
        System.out.println("успех");
    }


    @Override
    public String authorization(String username, String password) {
        Player player = playerMap.get(username);

        if (player == null || player.getPassword().isEmpty()) {
            System.out.println("Пизда");
            return null;
        } else {
            System.out.println("успех");
            return username;
        }
    }
}