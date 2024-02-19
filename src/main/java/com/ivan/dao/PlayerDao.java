package com.ivan.dao;

import com.ivan.model.entity.Player;

import java.util.Optional;

/**
 * The PlayerDao interface extends the GeneralDao interface for handling operations related to player data.
 * It provides a method for finding a player by username.
*/
public interface PlayerDao extends GeneralDao<Long, Player> {

    /**
     * Finds a player by their username.
     *
     * @param username The username of the player to find.
     * @return Optional<Player> The player object found by username,
     *         or an empty Optional if no player with that username is found.
     */
    Optional<Player> findByUsername(String username);
}