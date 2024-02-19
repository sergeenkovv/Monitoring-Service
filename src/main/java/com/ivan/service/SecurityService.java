package com.ivan.service;

import com.ivan.model.entity.Player;

/**
 * The SecurityService interface defines methods for user registration and authorization.
 * These methods handle actions related to registering new players and authorizing existing players.
 */
public interface SecurityService {

    /**
     * Registers a new player with the provided login and password.
     *
     * @param login The login of the player to register.
     * @param password The password of the player to register.
     * @return The newly registered player object.
     */
    Player registration(String login, String password);

    /**
     * Authorizes an existing player with the given login and password.
     *
     * @param login The login of the player to authorize.
     * @param password The password of the player to authorize.
     * @return The authorized player object.
     */
    Player authorization(String login, String password);
}