package com.ivan;

import com.ivan.controller.PlayerController;
import com.ivan.controller.SecurityController;
import com.ivan.exception.AuthorizationException;
import com.ivan.exception.DuplicateReadingsException;
import com.ivan.exception.NotValidArgumentException;
import com.ivan.exception.RegistrationException;
import com.ivan.in.InputData;
import com.ivan.in.OutputData;
import com.ivan.model.entity.MeterReading;
import com.ivan.model.entity.Player;
import com.ivan.model.types.MeterType;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * The ApplicationRunner class represents the main application running logic.
 * It manages the application flow by handling different stages and processes.
 * This class interacts with the application context to access necessary components.
 * <p>
 * The main stages include security process, main menu process, and exiting the application.
 * Errors and exceptions are logged and handled while running the application.
 */
@Slf4j
public class ApplicationRunner {

    private static PlayerController playerController;
    private static SecurityController securityController;
    private static ProcessStage currentStage;

    /**
     * Runs the application by initializing the context, managing stages, and processing user input.
     * This method controls the flow of the application based on the current stage.
     */
    public static void run() {
        ApplicationContext.loadContext();
        InputData inputData = (InputData) ApplicationContext.getBean("input");
        OutputData outputData = (OutputData) ApplicationContext.getBean("output");
        playerController = (PlayerController) ApplicationContext.getBean("playerController");
        securityController = (SecurityController) ApplicationContext.getBean("securityController");
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
            } catch (AuthorizationException |
                     DuplicateReadingsException |
                     NotValidArgumentException |
                     RegistrationException e) {
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

    /**
     * Handles the security process stage where user registration and authorization are managed.
     * It guides the user through registration, login, or exiting the application based on input.
     *
     * @param inputData  The input data object for receiving user input.
     * @param outputData The output data object for displaying messages to the user.
     */
    private static void securityProcess(InputData inputData, OutputData outputData) {
        final String menu = """
                ╔═══════════════════════════════════════════════════════╗
                  Please register or log in to the application.
                  Enter one number without spaces or other characters:
                  1. Registration.
                  2. Login.
                  3. End the program.
                ╚═══════════════════════════════════════════════════════╝
                  """;

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

    /**
     * Handles the main menu process stage where user interactions with meter readings and account occur.
     * Users can view current readings, submit readings, view history, log out, or quit the application.
     * This method provides a menu-based interface for users to perform various actions.
     *
     * @param inputData  The input source for user commands.
     * @param outputData The output destination for displaying messages.
     */
    private static void menuProcess(InputData inputData, OutputData outputData) {
        final String menu = """
                ╔═════════════════════════════════════════════════╗
                  1. Get current meter readings.
                  2. Submit meter readings.
                  3. View readings for a specific month.
                  4. View the history of meter readings.
                  5. Log out of your account.
                  0. Quit the application.
                ╚═════════════════════════════════════════════════╝
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

    /**
     * Handles the exit process of the application by displaying a farewell message and cleaning up the authorized player context.
     *
     * @param outputData The output destination to display the goodbye message.
     */
    private static void exitProcess(OutputData outputData) {
        final String message = "Goodbye!";
        outputData.output(message);
        ApplicationContext.cleanAuthorizePlayer();
    }

    /**
     * The ProcessStage enum represents the different stages in the application flow.
     * It includes the security stage, main menu stage, and exit stage.
     */
    private enum ProcessStage {
        /**
         * Security process stage.
         */
        SECURITY,
        /**
         * MonitoringServiceApplication menu process stage.
         */
        MAIN_MENU,
        /**
         * Exit process stage.
         */
        EXIT
    }

    /**
     * The HandlerMenu class contains methods for handling various menu operations within the application.
     * These methods manage actions such as displaying meter reading history, viewing readings by month,
     * submitting meter readings, and showing current meter readings.
     * <p>
     * These methods interact with the player controller to retrieve or modify meter readings based on user input.
     */
    private static class HandlerMenu {

        /**
         * Displays the history of meter readings for the authorized player.
         * It shows a message if no history is available.
         *
         * @param outputData The output data object for displaying messages.
         */
        private static void handlerShowMeterReadingHistory(OutputData outputData) {
            List<MeterReading> history = playerController.showMeterReadingHistory(ApplicationContext.getAuthorizePlayer());
            if (history == null || history.isEmpty()) {
                outputData.output("You have not a history of sending meter readings.\n");
            } else {
                outputData.output("""
                        Your history of sending meter readings:
                        """);
                for (MeterReading reading : history) {
                    String formattedReading = String.format(
                            "ID: %-15d, Meter Type: %-15s, Counter: %-15d, Date: %-10s, Player ID: %-20d",
                            reading.getId(), reading.getMeterType(), reading.getCounter(), reading.getDate(), reading.getPlayerId()
                    );
                    outputData.output(formattedReading);
                }
            }
        }

        /**
         * Displays the meter readings for a specific month provided by the user.
         * If no readings are found, a corresponding message is displayed.
         *
         * @param inputData  The input source for user commands.
         * @param outputData The output data object for displaying messages.
         */
        private static void handlerShowMeterReadingsByMonth(InputData inputData, OutputData outputData) {
            final String yearMessage = "Enter year:";
            outputData.output(yearMessage);
            String yearOut = inputData.input().toString();
            final String monthMessage = "Enter month:";
            outputData.output(monthMessage);
            String monthOut = inputData.input().toString();
            List<MeterReading> history = playerController.showMeterReadingsByMonth(ApplicationContext.getAuthorizePlayer(), yearOut, monthOut);
            if (history == null || history.isEmpty()) {
                outputData.output("You haven't submitted your meter readings yet.\n");
            } else {
                outputData.output("Current meter readings: ");
                for (MeterReading reading : history) {
                    String formattedReading = String.format(
                            "ID: %-15d, Meter Type: %-15s, Counter: %-15d, Date: %-10s, Player ID: %-20d",
                            reading.getId(), reading.getMeterType(), reading.getCounter(), reading.getDate(), reading.getPlayerId()
                    );
                    outputData.output(formattedReading);
                }
            }
        }

        /**
         * Handles the submission of a new meter reading by the authorized player.
         * It prompts the user to select a meter type and enter a counter value for the reading.
         *
         * @param inputData  The input source for user commands.
         * @param outputData The output data object for displaying messages.
         */
        private static void handlerSubmitMeterReading(InputData inputData, OutputData outputData) {
            final String readingMsg = "Select which readings to send.";
            outputData.output(readingMsg);
            for (MeterType type : MeterType.values()) {
                outputData.output(type);
            }
            String meterType = inputData.input().toString();
            final String counterMess = "Enter how much. Negative numbers are not allowed!";
            outputData.output(counterMess);
            String countOutp = inputData.input().toString();
            playerController.submitMeterReading(ApplicationContext.getAuthorizePlayer(), meterType, countOutp);
        }

        /**
         * Displays the current meter readings for the authorized player.
         * Shows a message if no current readings are available.
         *
         * @param outputData The output data object for displaying messages.
         */
        private static void handlerShowCurrentMeterReadings(OutputData outputData) {
            List<MeterReading> history = playerController.showCurrentMeterReadings(ApplicationContext.getAuthorizePlayer());
            if (history == null || history.isEmpty()) {
                outputData.output("You haven't submitted your meter readings yet.\n");
            } else {
                outputData.output("Current meter readings: ");
                for (MeterReading reading : history) {
                    String formattedReading = String.format(
                            "ID: %-15d, Meter Type: %-15s, Counter: %-15d, Date: %-10s, Player ID: %-20d",
                            reading.getId(), reading.getMeterType(), reading.getCounter(), reading.getDate(), reading.getPlayerId()
                    );
                    outputData.output(formattedReading);
                }
            }
        }
    }

    /**
     * The HandlerSecurity class contains methods for handling security-related operations, such as user authorization and registration.
     * These methods interact with the security controller to manage user authentication and account creation.
     */
    public static class HandlerSecurity {

        /**
         * Handles the user authorization process by requesting login and password input.
         * Upon successful authorization, the authorized player is loaded into the application context.
         * The current stage is then updated to the main menu stage for further interactions.
         *
         * @param inputData  The input source for user commands.
         * @param outputData The output data object for displaying messages.
         */
        private static void handlerAuthorize(InputData inputData, OutputData outputData) {
            final String loginMsg = "Enter login:";
            outputData.output(loginMsg);
            String login = inputData.input().toString();
            final String passMsg = "Enter password:";
            outputData.output(passMsg);
            String password = inputData.input().toString();

            Player authorizedPlayer = securityController.authorize(login, password);
            ApplicationContext.loadAuthorizePlayer(authorizedPlayer);
            currentStage = ProcessStage.MAIN_MENU;
        }

        /**
         * Manages the registration process by guiding the user to create a new account.
         * This method prompts the user to enter a login and password for registration.
         *
         * @param inputData  The input source for user commands.
         * @param outputData The output data object for displaying messages.
         */
        private static void handlerRegister(InputData inputData, OutputData outputData) {
            final String loginMsg = "Enter login:";
            outputData.output(loginMsg);
            String login = inputData.input().toString();
            final String passMsg = "Enter password. The password cannot be empty and must be between 3 and 32 characters long:";
            outputData.output(passMsg);
            String password = inputData.input().toString();

            Player registeredPlayer = securityController.register(login, password);
            ApplicationContext.loadAuthorizePlayer(registeredPlayer);
            currentStage = ProcessStage.MAIN_MENU;
        }
    }
}