package com.ivan.controller;

import com.ivan.exception.NotValidArgumentException;
import com.ivan.model.entity.Player;
import com.ivan.service.SecurityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * The SecurityController class handles user registration and authorization actions.
 * It provides methods for registering a new player and authorizing an existing player.
 */
@Slf4j
@RequiredArgsConstructor
public class SecurityController {

    private final SecurityService securityService;

    /**
     * Registers a new player with the provided login and password.
     *
     * @param login The login of the player.
     * @param password The password of the player.
     * @return The registered player object.
     * @throws NotValidArgumentException If the login or password is empty, consists of only spaces,
     *         or the password length is not within the specified range (3-32 characters).
     */
    public Player register(String login, String password) {
        if (login == null || password == null || login.isEmpty() || login.isBlank() || password.isEmpty() || password.isBlank()) {
            throw new NotValidArgumentException("The password or login cannot be empty or consist of only spaces.");
        }

        if (password.length() < 3 || password.length() > 32) {
            throw new NotValidArgumentException("The password must be between 3 and 32 characters long.");
        }

        log.info("The player trying to register with login " + login + " and password " + password + ".");
        return securityService.registration(login, password);
    }

    /**
     * Authorizes an existing player with the provided login and password.
     *
     * @param login The login of the player.
     * @param password The password of the player.
     * @return The authorized player object.
     * @throws NotValidArgumentException If the login or password is empty, consists of only spaces.
     */
    public Player authorize(String login, String password) {
        if (login == null || password == null || login.isEmpty() || login.isBlank() || password.isEmpty() || password.isBlank()) {
            throw new NotValidArgumentException("The password or login cannot be empty or consist of only spaces.");
        }

        log.info("The player trying to log in with login " + login + " and password " + password + ".");
        return securityService.authorization(login, password);
    }
}