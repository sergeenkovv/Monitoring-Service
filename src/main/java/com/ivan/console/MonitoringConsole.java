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

import java.time.YearMonth;
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
                    case 1 -> userService.getCurrentMeterReadings(getLoggedInUserName());
                    case 2 -> {
                        System.out.println("подача показаний");
                        String playerName = getLoggedInUserName();
                        actionScreenHandler.meterTypeMenu();
                        MeterType meterType = null;
                        int choice2 = actionScreenHandler.readChoice();
                        switch (choice2) {
                            case 1 -> meterType = MeterType.HOT_WATTER;
                            case 2 -> meterType = MeterType.COLD_WATTER;
                            case 3 -> meterType = MeterType.HEATING;
                            case 4 -> meterType = MeterType.CHOCOLATE;
                        }
                        System.out.println("Введите сколько");
                        Integer counter = scanner.nextInt();
                        userService.submitMeterReading(playerName, meterType, counter);
                    }
                    case 3 -> {
                        System.out.println("просмотра показаний за конкретный месяц");
                        System.out.print("Введите год (например, 2023): ");
                        int year = scanner.nextInt();
                        System.out.print("Введите месяц (например, 3 для марта): ");
                        int month = scanner.nextInt();
                        YearMonth date = YearMonth.of(year, month);
                        userService.getMeterReadingsByMonth(getLoggedInUserName(), date);
                    }
                    case 4 -> userService.getMeterReadingHistory(getLoggedInUserName());

                }
            }
        }
    }
}