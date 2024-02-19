package com.ivan.service.impl;

import com.ivan.dao.PlayerDao;
import com.ivan.exception.AuthorizationException;
import com.ivan.exception.RegistrationException;
import com.ivan.model.entity.Player;
import com.ivan.service.SecurityService;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

/**
 * The SecurityServiceImpl class implements the SecurityService interface for managing security-related operations.
 * It provides methods for user registration and authorization.
*/
@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private final PlayerDao playerDao;

    /**
     * Registers a new player with the provided username and password.
     *
     * @param username The username of the player to register.
     * @param password The password of the player to register.
     * @return The newly registered player object.
     * @throws RegistrationException If a player with the same username already exists.
     */
    @Override
    public Player registration(String username, String password) {
        Optional<Player> player = playerDao.findByUsername(username);

        if (player.isPresent()) {
            throw new RegistrationException("The player with this login already exists.");
        }

        Player newPlayer = Player.builder()
                .username(username)
                .password(password)
                .build();

        return playerDao.save(newPlayer);
    }

    /**
     * Authorizes an existing player with the given username and password.
     *
     * @param username The username of the player to authorize.
     * @param password The password of the player to authorize.
     * @return The authorized player object.
     * @throws AuthorizationException If no player is found with the given username or if the password is incorrect.
     */
    @Override
    public Player authorization(String username, String password) {
        Optional<Player> maybePlayer = playerDao.findByUsername(username);

        if (maybePlayer.isEmpty()) {
            throw new AuthorizationException("There is no player with this login in the database.");
        }

        Player player = maybePlayer.get();
        if (!player.getPassword().equals(password)) {
            throw new AuthorizationException("Incorrect password.");
        }
        return player;
    }
}