package com.ivan.dao.impl;

import com.ivan.dao.PlayerDao;
import com.ivan.model.entity.Player;

import java.util.*;

/**
 * The MemoryPlayerDaoImpl class is an implementation of the PlayerDao interface
 * that stores player data in memory using a HashMap.
 * It provides methods for finding a player by username and saving player data.
 * The MemoryPlayerDaoImpl class simulates working with a database. But instead of a database, collections are used.
 */
public class MemoryPlayerDaoImpl implements PlayerDao {

    private final Map<Long, Player> playerMap = new HashMap<>();
    private Long id = 1L;

    /**
     * Finds a player by their username.
     *
     * @param username The username of the player to find.
     * @return Optional<Player> The player object found by username,
     * or an empty Optional if no player with that username is found.
     */
    @Override
    public Optional<Player> findByUsername(String username) {
        Player player = null;
        List<Player> list = new ArrayList<>(playerMap.values());

        for (Player pl : list) {
            if (pl.getUsername().equals(username)) {
                player = pl;
                break;
            }
        }
        return player == null ? Optional.empty() : Optional.of(player);
    }

    /**
     * Saves player data.
     *
     * @param player The player object to save.
     * @return Player The saved player object with an assigned ID.
     */
    @Override
    public Player save(Player player) {
        player.setId(getLastId());
        incrementId();
        playerMap.put(player.getId(), player);
        return playerMap.get(player.getId());
    }

    private Long getLastId() {
        return id;
    }

    private void incrementId() {
        id++;
    }
}