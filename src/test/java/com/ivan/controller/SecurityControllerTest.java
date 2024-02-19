package com.ivan.controller;

import com.ivan.exception.NotValidArgumentException;
import com.ivan.model.entity.Player;
import com.ivan.service.SecurityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SecurityControllerTest {

    @InjectMocks
    private SecurityController securityController;
    @Mock
    private SecurityService securityService;

    @Test
    void register_Success() {
        String username = "ivan";
        String password = "123";

        Player expected = Player.builder()
                .username(username)
                .password(password)
                .build();

        when(securityService.registration(username, password)).thenReturn(expected);

        Player result = securityController.register(username, password);

        assertDoesNotThrow(() -> result);

        assertEquals(expected, result);
    }

    @Test
    void register_ThrowException() {
        assertAll(
                () -> {
                    NotValidArgumentException exception = assertThrows(NotValidArgumentException.class,
                            () -> securityController.register(" ", ""),
                            "The password or login cannot be empty or consist of only spaces.");
                    assertEquals(exception.getMessage(), "The password or login cannot be empty or consist of only spaces.");
                },
                () -> {
                    NotValidArgumentException exception = assertThrows(NotValidArgumentException.class,
                            () -> securityController.register("ivan", "12"),
                            "The password or login cannot be empty or consist of only spaces.");
                    assertEquals(exception.getMessage(), "The password must be between 3 and 32 characters long.");
                }
        );
    }

    @Test
    void authorize_Success() {
        String username = "ivan";
        String password = "123";

        Player expected = Player.builder()
                .username(username)
                .password(password)
                .build();

        when(securityService.authorization(username, password)).thenReturn(expected);

        Player result = securityController.authorize(username, password);

        assertDoesNotThrow(() -> result);

        assertEquals(expected, result);
    }

    @Test
    void authorize_ThrowException() {
        NotValidArgumentException exception = assertThrows(NotValidArgumentException.class,
                () -> securityController.authorize(" ", ""),
                "The password or login cannot be empty or consist of only spaces.");
        assertEquals(exception.getMessage(), "The password or login cannot be empty or consist of only spaces.");
    }
}