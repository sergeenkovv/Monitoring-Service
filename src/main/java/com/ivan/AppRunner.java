package com.ivan;

import com.ivan.console.MonitoringConsole;

public class AppRunner {
    public static void main(String[] args) {
        MonitoringConsole monitoringConsole = MonitoringConsole.getINSTANCE();
        monitoringConsole.start();
    }
}