package com.ivan.console.handler;

import java.util.Scanner;

public class ActionScreenHandler {

    Scanner scanner = new Scanner(System.in);

    public int readChoice() {
        int choice = 0;
        try {
            choice = scanner.nextInt();
            scanner.nextLine();
        } catch (Exception e) {
            scanner.nextLine();
        }
        return choice;
    }

    public void actionMenu() {
        System.out.println("Выберите действие:");
        System.out.println("1. Получить актуальные показания счетчиков:");
        System.out.println("2. Подать показания:");
        System.out.println("3. Посмотреть показания за конкретный месяц:");
        System.out.println("4. История подачи показаний:");
    }

    public void meterTypeMenu() {
        System.out.println("За что платим?");
        System.out.println("1. HOT_WATTER");
        System.out.println("2. COLD_WATTER");
        System.out.println("3. HEATING");
        System.out.println("4. CHOCOLATE");
    }
}
