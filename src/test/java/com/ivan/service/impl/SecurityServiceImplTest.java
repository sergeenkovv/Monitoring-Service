package com.ivan.service.impl;

import com.ivan.dao.PlayerDao;
import com.ivan.exception.AuthorizationException;
import com.ivan.exception.RegistrationException;
import com.ivan.model.entity.Player;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecurityServiceImplTest {

    @InjectMocks
    private SecurityServiceImpl securityService;
    @Mock
    private PlayerDao playerDao;

    @Test
    void testRegistration_Success() {
        String username = "ivan";
        String password = "123";

        Player player = Player.builder()
                .username(username)
                .password(password)
                .build();

        when(playerDao.findByUsername(username)).thenReturn(Optional.empty());
        when(playerDao.save(any(Player.class))).thenReturn(player);

        Player registrationPlayer = securityService.registration(username, password);
        assertEquals(username, registrationPlayer.getUsername());
        assertEquals(password, registrationPlayer.getPassword());
    }

    @Test
    void testRegister_ThrowException() {
        String username = "ivan";
        String password = "123";

        Player player = Player.builder()
                .username(username)
                .password(password)
                .build();

        when(playerDao.findByUsername(username)).thenReturn(Optional.of(player));

        assertThrows(RegistrationException.class, () -> securityService.registration(username, password));
    }

    @Test
    void testAuthorization_Success() {
        String username = "ivan";
        String password = "123";

        Player player = Player.builder()
                .username(username)
                .password(password)
                .build();

        when(playerDao.findByUsername(username)).thenReturn(Optional.of(player));

        Player authorizationPlayer = securityService.authorization(username, password);
        assertEquals(username, authorizationPlayer.getUsername());
        assertEquals(password, authorizationPlayer.getPassword());
    }

    @Test
    void testAuthorization_ThrowException() {
        String username = "ivan";
        String password = "123";
        when(playerDao.findByUsername(username)).thenReturn(Optional.empty());

        assertThrows(AuthorizationException.class, () -> securityService.authorization(username, password));
    }
}