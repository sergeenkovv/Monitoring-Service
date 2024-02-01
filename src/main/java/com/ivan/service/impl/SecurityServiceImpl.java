package com.ivan.service.impl;

import com.ivan.dao.PlayerDao;
import com.ivan.exception.AuthorizationException;
import com.ivan.exception.RegistrationException;
import com.ivan.model.entity.Player;
import com.ivan.service.SecurityService;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class SecurityServiceImpl implements SecurityService {

    private final PlayerDao playerDao;

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