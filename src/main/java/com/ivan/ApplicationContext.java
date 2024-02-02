package com.ivan;

import com.ivan.controller.MainController;
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

public class ApplicationContext {
    private static final Map<String, Object> CONTEXT = new HashMap<>();

    public static void loadContext() {
        loadDaoLayer();
        loadServiceLayer();
        loadControllers();
        loadInputOutputLayer();
    }

    public static void loadAuthorizePlayer(Player player) {
        CONTEXT.put("authorize", player);
    }

    public static void cleanAuthorizePlayer() {
        CONTEXT.remove("authorize");
    }

    public static Player getAuthorizePlayer() {
        return (Player) CONTEXT.get("authorize");
    }

    public static Object getBean(String beanName) {
        return CONTEXT.get(beanName);
    }

    private static void loadDaoLayer() {
        CONTEXT.put("playerDao", new MemoryPlayerDaoImpl());
        CONTEXT.put("meterReadingDao", new MemoryMeterReadingsDaoImpl());
    }

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

    private static void loadControllers() {
        MainController controller = new MainController(
                (SecurityService) CONTEXT.get("securityService"),
                (PlayerService) CONTEXT.get("playerService")
        );
        CONTEXT.put("controller", controller);
    }

    private static void loadInputOutputLayer() {
        CONTEXT.put("input", new ConsoleInputData());
        CONTEXT.put("output", new ConsoleOutputData());
    }
}