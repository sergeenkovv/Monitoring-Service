package com.ivan.service.impl;

import com.ivan.model.entity.MeterReading;
import com.ivan.model.entity.Player;
import com.ivan.model.types.MeterType;
import com.ivan.service.UserService;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {

    @Getter
    private final Map<String, Player> playerMap;
    @Getter
    private final Map<String, MeterReading> meterReadingMap;
    //    @Getter
//    private final Map<String, List<MeterReading>> userMeterReadings;
    @Getter
    private final Map<String, List<String>> userActions;

    public UserServiceImpl(SecurityServiceImpl securityService) {
        this.playerMap = securityService.getPlayerMap();
//        this.userMeterReadings = securityService.getUserMeterReadings();
        this.userActions = securityService.getUserActions();
        this.meterReadingMap = securityService.getMeterReadingMap();
    }

    //подачи показаний
    public void submitMeterReading(String username, MeterType meterType, Integer counter) {
        Player player = playerMap.get(username);

        MeterReading meterReading = MeterReading.builder()
                .username(username)
                .meterType(meterType)
                .counter(counter)
                .date(LocalDate.now())
                .build();

        player.getReadingsList().add(meterReading);
    }

    //показание за конкретный месяц
//    public List<MeterReading> getMeterReadingsByMonth(String username, int month) {
//        List<MeterReading> readings = new ArrayList<>();
//        if (userMeterReadings.containsKey(username)) {
//            for (MeterReading reading : userMeterReadings.get(username)) {
//                Calendar cal = Calendar.getInstance();
////                cal.setTime(reading.getDate());
//                if (cal.get(Calendar.MONTH) + 1 == month) {
//                    readings.add(reading);
//                }
//            }
//        }
//        return readings;
//    }

    // Метод для просмотра истории подачи показаний счетчиков для указанного пользователя
    public void getMeterReadingHistory(String username) {
        Player player = playerMap.get(username);
        List<MeterReading> readingsList = player.getReadingsList();

        for (MeterReading reading : readingsList) {
            System.out.println(reading); // предполагается, что у MeterReading переопределен метод toString()
        }
    }

    //я получения актуальных показаний счетчиков/этот метод я сделаю с помощью метода про получение через конкретный месяц. метод в методе будет
//    public List<MeterReading> getLatestMeterReadings(String username) {
//        Player player = playerMap.get(username);
//        if (player.containsKey(username)) {
//            return users.get(username).getReadingsList();
//        }
//        return new ArrayList<>();
//    }
}