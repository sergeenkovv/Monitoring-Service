package com.ivan.dao.impl;

import com.ivan.dao.PlayerDao;
import com.ivan.model.entity.Player;

import java.util.*;

public class MemoryPlayerDaoImpl implements PlayerDao {

    private final Map<Long, Player> playerMap = new HashMap<>();
    private Long id = 1L;

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