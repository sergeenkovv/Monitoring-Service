package com.ivan.service;

import com.ivan.model.entity.Player;

public interface SecurityService {

    Player registration(String login, String password);

    Player authorization(String login, String password);
}