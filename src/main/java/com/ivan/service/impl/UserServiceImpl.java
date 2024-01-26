package com.ivan.service.impl;

import com.ivan.model.entity.MeterReading;
import com.ivan.model.entity.Player;
import com.ivan.model.types.MeterType;
import com.ivan.service.UserService;
import com.ivan.validator.UserValidator;
import lombok.Getter;

import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {

    @Getter
    private final Map<String, Player> playerMap;

    @Getter
    private final Map<String, List<String>> userActions;

    UserValidator userValidator;

    public UserServiceImpl(SecurityServiceImpl securityService) {
        this.userValidator = new UserValidator(securityService);
        this.playerMap = securityService.getPlayerMap();
        this.userActions = securityService.getUserActions();
    }

    //я получения актуальных показаний счетчиков/этот метод я сделаю с помощью метода про получение через конкретный месяц. метод в методе будет
    public List<MeterReading> getCurrentMeterReadings(String username) {
        Player player = playerMap.get(username);
        return getMeterReadingsByMonth(player.getUsername(), YearMonth.now());
    }

    //подачи показаний
    public void submitMeterReading(String username, MeterType meterType, Integer counter) {
        Player player = playerMap.get(username);

        if (userValidator.isValid(player.getUsername(), meterType)) {
            System.out.println("вы уже отправляли показание в этом месяце");
        }

        MeterReading meterReading = MeterReading.builder()
                .username(username)
                .meterType(meterType)
                .counter(counter)
                .date(YearMonth.now())
                .build();

        player.getReadingsList().add(meterReading);
    }

    //показание за конкретный месяц
    public List<MeterReading> getMeterReadingsByMonth(String username, YearMonth date) {
        List<MeterReading> readingsList = playerMap.get(username).getReadingsList();
        List<MeterReading> filteredReadings = new ArrayList<>();
        for (MeterReading reading : readingsList) {
            if (YearMonth.from(reading.getDate()).equals(date)) {
                filteredReadings.add(reading);
                System.out.println(reading);
            }
        }
        return filteredReadings;
    }


    // Метод для просмотра истории подачи показаний счетчиков для указанного пользователя
    public List<MeterReading> getMeterReadingHistory(String username) {
        Player player = playerMap.get(username);
        List<MeterReading> readingsList = player.getReadingsList();

        for (MeterReading reading : readingsList) {
            System.out.println(reading);
        }
        return readingsList;
    }
}