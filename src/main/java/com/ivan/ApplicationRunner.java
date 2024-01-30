package com.ivan;

import com.ivan.controller.MainController;
import com.ivan.exception.AuthorizeException;
import com.ivan.exception.DuplicateReadingsException;
import com.ivan.exception.NotValidArgumentException;
import com.ivan.exception.RegisterException;
import com.ivan.in.InputData;
import com.ivan.model.entity.MeterReading;
import com.ivan.model.entity.Player;
import com.ivan.model.types.MeterType;
import com.ivan.out.OutputData;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class ApplicationRunner {

    private static MainController controller;
    private static ProcessStage currentStage;

    public static void run() {
        ApplicationContext.loadContext();
        InputData inputData = (InputData) ApplicationContext.getBean("input");
        OutputData outputData = (OutputData) ApplicationContext.getBean("output");
        controller = (MainController) ApplicationContext.getBean("controller");
        currentStage = ProcessStage.SECURITY;
        outputData.output("Welcome!\n");

        boolean processIsRun = true;
        while (processIsRun) {
            try {
                switch (currentStage) {
                    case SECURITY -> securityProcess(inputData, outputData);
                    case MAIN_MENU -> menuProcess(inputData, outputData);
                    case EXIT -> {
                        exitProcess(outputData);
                        processIsRun = false;
                    }
                }
            } catch (AuthorizeException |
                     DuplicateReadingsException |
                     NotValidArgumentException |
                     RegisterException e) {
                log.warn(e.getMessage());
                outputData.errOutput(e.getMessage());
            } catch (RuntimeException e) {
                log.error(e.getMessage());
                outputData.errOutput("Unknown error. More details " + e.getMessage());
                processIsRun = false;
            }
        }
        inputData.closeInput();
    }

    private static void securityProcess(InputData inputData, OutputData outputData) {
        final String firstMessage = "Please register or log in to the application.";
        final String menu = """
                Enter one number without spaces or other characters:
                1. Registration.
                2. Login.
                3. End the program.
                """;

        outputData.output(firstMessage);
        while (true) {
            outputData.output(menu);
            Object input = inputData.input();
            if (input.toString().equals("1")) {
                HandlerSecurity.handlerRegister(inputData, outputData);
                break;
            } else if (input.toString().equals("2")) {
                HandlerSecurity.handlerAuthorize(inputData, outputData);
                break;
            } else if (input.toString().equals("3")) {
                currentStage = ProcessStage.EXIT;
                break;
            } else {
                outputData.output("Unknown command, try again.");
            }
        }
    }

    private static void menuProcess(InputData inputData, OutputData outputData) {
        final String menu = """
                1. Get current meter readings.
                2. Submit meter readings.
                3. View readings for a specific month.
                4. View the history of meter readings.
                5. Log out of your account.
                0. Quit the application.
                """;
        while (true) {
            outputData.output(menu);
            String input = inputData.input().toString();
            if (input.equals("1")) {
                HandlerMenu.handlerShowCurrentMeterReadings(outputData);
            } else if (input.equals("2")) {
                HandlerMenu.handlerSubmitMeterReading(inputData, outputData);
            } else if (input.equals("3")) {
                HandlerMenu.handlerShowMeterReadingsByMonth(inputData, outputData);
            } else if (input.equals("4")) {
                HandlerMenu.handlerShowMeterReadingHistory(outputData);
            } else if (input.equals("5")) {
                ApplicationContext.cleanAuthorizePlayer();
                currentStage = ProcessStage.SECURITY;
                break;
            } else if (input.equals("0")) {
                ApplicationContext.cleanAuthorizePlayer();
                currentStage = ProcessStage.EXIT;
                break;
            } else {
                outputData.output("Unknown command, try again.\n");
            }
        }
    }


    private static void exitProcess(OutputData outputData) {
        final String message = "Goodbye!";
        outputData.output(message);
        ApplicationContext.cleanAuthorizePlayer();
    }

    private enum ProcessStage {
        /**
         * Security process stage.
         */
        SECURITY,
        /**
         * Main menu process stage.
         */
        MAIN_MENU,
        /**
         * Exit process stage.
         */
        EXIT
    }

    private static class HandlerMenu {
        private static void handlerShowMeterReadingHistory(OutputData outputData) {
            List<MeterReading> history = controller.showMeterReadingHistory(ApplicationContext.getAuthorizePlayer());
            if (history == null || history.isEmpty()) {
                outputData.output("You have not a history of sending meter readings.\n");
            } else {
                outputData.output("Your history of sending meter readings:");
                for (MeterReading reading : history) {
                    outputData.output(reading);
                }
            }
        }

        private static void handlerShowMeterReadingsByMonth(InputData inputData, OutputData outputData) {
            final String yearMessage = "Enter year:";
            outputData.output(yearMessage);
            String yearOut = inputData.input().toString();
            final String monthMessage = "Enter month:";
            outputData.output(monthMessage);
            String monthOut = inputData.input().toString();
            List<MeterReading> history = controller.showMeterReadingsByMonth(ApplicationContext.getAuthorizePlayer(), Integer.valueOf(yearOut), Integer.valueOf(monthOut));
            if (history == null || history.isEmpty()) {
                outputData.output("You haven't submitted your meter readings yet.\n");
            } else {
                outputData.output("Current meter readings: ");
                for (MeterReading reading : history) {
                    outputData.output(reading);
                }
            }
        }

        private static void handlerSubmitMeterReading(InputData inputData, OutputData outputData) {
            final String readingMsg = "Select which readings to send.";
            outputData.output(readingMsg);
            for (MeterType type : MeterType.values()) {
                System.out.println(type);
            }
            String meterType = inputData.input().toString();
            final String counterMess = "Enter how much.";
            outputData.output(counterMess);
            String countOutp = inputData.input().toString();
            controller.submitMeterReading(ApplicationContext.getAuthorizePlayer(), MeterType.valueOf(meterType), Integer.valueOf(countOutp));
        }

        private static void handlerShowCurrentMeterReadings(OutputData outputData) {
            List<MeterReading> history = controller.showCurrentMeterReadings(ApplicationContext.getAuthorizePlayer());
            if (history == null || history.isEmpty()) {
                outputData.output("You haven't submitted your meter readings yet.\n");
            } else {
                outputData.output("Current meter readings: ");
                for (MeterReading reading : history) {
                    outputData.output(reading);
                }
            }
        }
    }

    public static class HandlerSecurity {
        private static void handlerAuthorize(InputData inputData, OutputData outputData) {
            final String loginMsg = "Enter login:";
            outputData.output(loginMsg);
            String login = inputData.input().toString();
            final String passMsg = "Enter password:";
            outputData.output(passMsg);
            String password = inputData.input().toString();

            Player authorizedPlayer = controller.authorize(login, password);
            ApplicationContext.loadAuthorizePlayer(authorizedPlayer);
            currentStage = ProcessStage.MAIN_MENU;
        }

        private static void handlerRegister(InputData inputData, OutputData outputData) {
            final String loginMsg = "Enter login:";
            outputData.output(loginMsg);
            String login = inputData.input().toString();
            final String passMsg = "Enter password. The password cannot be empty and must be between 3 and 32 characters long:";
            outputData.output(passMsg);
            String password = inputData.input().toString();

            Player registeredPlayer = controller.register(login, password);
            ApplicationContext.loadAuthorizePlayer(registeredPlayer);
            currentStage = ProcessStage.MAIN_MENU;
        }
    }
}