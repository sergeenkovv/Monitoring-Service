package com.ivan;

import com.ivan.controller.PlayerController;
import com.ivan.controller.SecurityController;
import com.ivan.dao.MeterReadingDao;
import com.ivan.dao.PlayerDao;
import com.ivan.dao.impl.MemoryMeterReadingsDaoImpl;
import com.ivan.dao.impl.MemoryPlayerDaoImpl;
import com.ivan.in.ConsoleInputData;
import com.ivan.in.ConsoleOutputData;
import com.ivan.model.entity.Player;
import com.ivan.service.PlayerService;
import com.ivan.service.SecurityService;
import com.ivan.service.impl.PlayerServiceImpl;
import com.ivan.service.impl.SecurityServiceImpl;

import java.util.HashMap;
import java.util.Map;

/**
 * The ApplicationContext class serves as the application context for managing beans and dependencies.
 * It loads and stores various components such as controllers, services, DAOs, and input/output layers.
 * It provides methods for loading context, managing authorized players, and retrieving beans by name.
 *
 * In this context, it initializes DAO implementations, service implementations, controllers, and input/output components.
 */
public class ApplicationContext {
    private static final Map<String, Object> CONTEXT = new HashMap<>();

    /**
     * Loads the application context by initializing the DAO layer, service layer, controllers, and input/output layer.
     */
    public static void loadContext() {
        loadDaoLayer();
        loadServiceLayer();
        loadControllers();
        loadInputOutputLayer();
    }

    /**
     * Loads an authorized player into the context.
     *
     * @param player The player to authorize.
     */
    public static void loadAuthorizePlayer(Player player) {
        CONTEXT.put("authorize", player);
    }

    /**
     * Removes the authorized player from the context.
     */
    public static void cleanAuthorizePlayer() {
        CONTEXT.remove("authorize");
    }

    /**
     * Retrieves the authorized player from the context.
     *
     * @return The authorized player object.
     */
    public static Player getAuthorizePlayer() {
        return (Player) CONTEXT.get("authorize");
    }

    /**
     * Retrieves a bean by its name from the context.
     *
     * @param beanName The name of the bean to retrieve.
     * @return The bean object associated with the given name.
     */
    public static Object getBean(String beanName) {
        return CONTEXT.get(beanName);
    }

    /**
     * Initializes and loads the DAO layer components into the application context.
     * It creates instances of MemoryPlayerDaoImpl and MemoryMeterReadingsDaoImpl, and stores them in the context.
     */
    private static void loadDaoLayer() {
        CONTEXT.put("playerDao", new MemoryPlayerDaoImpl());
        CONTEXT.put("meterReadingDao", new MemoryMeterReadingsDaoImpl());
    }

    /**
     * Initializes and loads the service layer components into the application context.
     * It creates instances of PlayerServiceImpl and SecurityServiceImpl, using DAO objects obtained from the context.
     * The created service objects are then stored in the context.
     */
    private static void loadServiceLayer() {
        PlayerService playerService = new PlayerServiceImpl(
                (MeterReadingDao) CONTEXT.get("meterReadingDao")
        );
        CONTEXT.put("playerService", playerService);

        SecurityService securityService = new SecurityServiceImpl(
                (PlayerDao) CONTEXT.get("playerDao")
        );
        CONTEXT.put("securityService", securityService);
    }

    /**
     * Initializes and loads the controller layer components into the application context.
     * It creates instances of PlayerController and SecurityController, using service objects obtained from the context.
     * The created controller objects are then stored in the context.
     */
    private static void loadControllers() {
        PlayerController playerController = new PlayerController(
                (PlayerService) CONTEXT.get("playerService")
        );
        CONTEXT.put("playerController", playerController);

        SecurityController securityController = new SecurityController(
                (SecurityService) CONTEXT.get("securityService")
        );
        CONTEXT.put("securityController", securityController);
    }

    /**
     * Initializes and loads the input/output layer components into the application context.
     * It creates instances of ConsoleInputData and ConsoleOutputData, and stores them in the context.
     */
    private static void loadInputOutputLayer() {
        CONTEXT.put("input", new ConsoleInputData());
        CONTEXT.put("output", new ConsoleOutputData());
    }
}