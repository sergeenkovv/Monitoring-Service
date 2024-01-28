package com.ivan.dao;

import com.ivan.model.entity.Player;

import java.util.Optional;

public interface PlayerDao extends GeneralDao<Long, Player> {

    Optional<Player> findByUsername(String username);
}
