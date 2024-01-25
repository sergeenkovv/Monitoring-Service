package com.ivan.console;

import com.ivan.console.handler.ActionScreenHandler;
import com.ivan.console.handler.WelcomeScreenHandler;
import com.ivan.model.types.MeterType;
import com.ivan.service.impl.SecurityServiceImpl;
import com.ivan.service.impl.UserServiceImpl;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Scanner;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MonitoringConsole {

    @Getter
    private static final MonitoringConsole INSTANCE = new MonitoringConsole();

    SecurityServiceImpl securityService = new SecurityServiceImpl();
    UserServiceImpl userService = new UserServiceImpl(securityService);

    @Getter
    @Setter
    String loggedInUserName = null;

    Scanner scanner;
    String password;


    @Getter
    @Setter
    boolean logIn = false;

    public void start() {
        WelcomeScreenHandler welcomeScreenHandler = new WelcomeScreenHandler();
        ActionScreenHandler actionScreenHandler = new ActionScreenHandler();

        while (true) {
            if (!logIn || loggedInUserName == null) {
                welcomeScreenHandler.welcomeMenu();
                int choice = actionScreenHandler.readChoice();

                switch (choice) {
                    case 1 -> {
                        scanner = new Scanner(System.in);
                        System.out.println("Введите имя игрока: ");
                        String username = scanner.nextLine();
                        System.out.println("Введите пароль игрока: ");
                        password = scanner.nextLine();
                        securityService.registration(username, password);
                    }
                    case 2 -> {
                        scanner = new Scanner(System.in);
                        System.out.println("Введите имя игрока: ");
                        String username = scanner.nextLine();
                        System.out.println("Введите пароль игрока: ");
                        password = scanner.nextLine();
                        String authorized = securityService.authorization(username, password);
                        if (authorized != null) {
                            setLogIn(true);
                            setLoggedInUserName(username);
                        } else {
                            System.out.println("Игрок не авторизован.");
                            setLogIn(false);
                        }
                    }
                    case 3 -> {
                        System.out.println("Выход из приложения");
                        System.exit(0);
                    }
                    default -> System.out.println("Некорректный выбор команды. Попробуйте еще раз.");
                }
            }
            if (loggedInUserName != null) {
                actionScreenHandler.actionMenu();
                int choice = actionScreenHandler.readChoice();

                switch (choice) {
//                    case 1 -> userService.getLatestMeterReadings(getLoggedInUserName());
                    case 2 -> {
                        System.out.println("подача показаний");
                        String playerName = getLoggedInUserName();
                        actionScreenHandler.meterTypeMenu();
                        System.out.println("Введите сколько");
                        Integer counterHOT_WATTER = scanner.nextInt();
                        userService.submitMeterReading(playerName, MeterType.HOT_WATTER, counterHOT_WATTER);
                        System.out.println("Введите сколько");
                        Integer counterCOLD_WATTER = scanner.nextInt();
                        userService.submitMeterReading(playerName, MeterType.COLD_WATTER, counterCOLD_WATTER);
                        System.out.println("Введите сколько");
                        Integer counterHEATING = scanner.nextInt();
                        userService.submitMeterReading(playerName, MeterType.HEATING, counterHEATING);
                        System.out.println("Введите сколько");
                        Integer counterCHOCOLATE = scanner.nextInt();
                        userService.submitMeterReading(playerName, MeterType.CHOCOLATE, counterCHOCOLATE);
                    }
                    case 3 -> {
                        System.out.println("просмотра показаний за конкретный месяц");
                    }
                    case 4 -> {
                        userService.getMeterReadingHistory(getLoggedInUserName());
                    }
                }
            }
        }
    }
}