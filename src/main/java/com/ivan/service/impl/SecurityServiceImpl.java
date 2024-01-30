package com.ivan.service.impl;

import com.ivan.dao.PlayerDao;
import com.ivan.exception.AuthorizeException;
import com.ivan.exception.RegisterException;
import com.ivan.model.entity.Player;
import com.ivan.service.SecurityService;

import java.util.Optional;

public class SecurityServiceImpl implements SecurityService {

    private final PlayerDao playerDao;

    public SecurityServiceImpl(PlayerDao playerDao) {
        this.playerDao = playerDao;
    }

    @Override
    public Player registration(String username, String password) {
        Optional<Player> player = playerDao.findByUsername(username);

        if (player.isPresent()) {
            throw new RegisterException("The player with this login already exists.");
        }

        Player newPlayer = Player.builder()
                .username(username)
                .password(password)
                .build();

        return playerDao.save(newPlayer);
    }


    @Override
    public Player authorization(String username, String password) {
        Optional<Player> maybePlayer = playerDao.findByUsername(username);

        if (maybePlayer.isEmpty()) {
            throw new AuthorizeException("There is no player with this login in the database.");
        }

        Player player = maybePlayer.get();
        if (!player.getPassword().equals(password)) {
            throw new AuthorizeException("Incorrect password.");
        }
        return player;
    }
}