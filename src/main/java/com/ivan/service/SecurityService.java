package com.ivan.service;

import java.util.UUID;

public interface SecurityService {

    void registration(String login, String password);

    String authorization(String login, String password);
}
