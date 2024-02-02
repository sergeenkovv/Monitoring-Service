package com.ivan.controller;

import com.ivan.exception.NotValidArgumentException;
import com.ivan.model.entity.MeterReading;
import com.ivan.model.entity.Player;
import com.ivan.model.types.MeterType;
import com.ivan.service.PlayerService;
import com.ivan.service.SecurityService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.YearMonth;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MainControllerTest {

    @InjectMocks
    private MainController mainController;
    @Mock
    private SecurityService securityService;
    @Mock
    private PlayerService playerService;

    @Test
    void register_Success() {
        String username = "ivan";
        String password = "123";

        Player expected = Player.builder()
                .username(username)
                .password(password)
                .build();

        when(securityService.registration(username, password)).thenReturn(expected);

        Player result = mainController.register(username, password);

        assertDoesNotThrow(() -> result);

        assertEquals(expected, result);
    }

    @Test
    void register_ThrowException() {
        assertAll(
                () -> {
                    NotValidArgumentException exception = assertThrows(NotValidArgumentException.class,
                            () -> mainController.register(" ", ""),
                            "The password or login cannot be empty or consist of only spaces.");
                    assertEquals(exception.getMessage(), "The password or login cannot be empty or consist of only spaces.");
                },
                () -> {
                    NotValidArgumentException exception = assertThrows(NotValidArgumentException.class,
                            () -> mainController.register("ivan", "12"),
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

        Player result = mainController.authorize(username, password);

        assertDoesNotThrow(() -> result);

        assertEquals(expected, result);
    }

    @Test
    void authorize_ThrowException() {
        NotValidArgumentException exception = assertThrows(NotValidArgumentException.class,
                () -> mainController.authorize(" ", ""),
                "The password or login cannot be empty or consist of only spaces.");
        assertEquals(exception.getMessage(), "The password or login cannot be empty or consist of only spaces.");
    }

    @Test
    void showCurrentMeterReadings_Success() {
        String username = "ivan";
        String password = "123";

        Player expected = Player.builder()
                .id(1L)
                .username(username)
                .password(password)
                .build();

        mainController.showCurrentMeterReadings(expected);
        verify(playerService, times(1)).getCurrentMeterReadings(expected.getId());
    }

    @Test
    void submitMeterReading_Success() {
        String username = "ivan";
        String password = "123";

        Player expected = Player.builder()
                .id(1L)
                .username(username)
                .password(password)
                .build();

        MeterReading meterReading = MeterReading.builder()
                .id(2L)
                .meterType(MeterType.valueOf("HOT_WATTER"))
                .counter(100)
                .date(YearMonth.now())
                .playerId(1L)
                .build();

        assertDoesNotThrow(() -> mainController.submitMeterReading(expected, meterReading.getMeterType().name(), meterReading.getCounter().toString()));

        verify(playerService, times(1)).submitMeterReading(expected.getId(), meterReading.getMeterType().name(), meterReading.getCounter());
    }

    @Test
    void submitMeterReading_ThrowException() {
        String username = "ivan";
        String password = "123";

        Player expected = Player.builder()
                .id(1L)
                .username(username)
                .password(password)
                .build();

        assertAll(
                () -> {
                    NotValidArgumentException exception = assertThrows(NotValidArgumentException.class,
                            () -> mainController.submitMeterReading(expected, "DIRTY_WATTER", "120"),
                            "You have entered an incorrect meter reading type. Check the correct spelling of the meter reading type.");
                    assertEquals(exception.getMessage(), "You have entered an incorrect meter reading type. Check the correct spelling of the meter reading type.");
                },
                () -> {
                    NotValidArgumentException exception = assertThrows(NotValidArgumentException.class,
                            () -> mainController.submitMeterReading(expected, "HOT_WATTER", "-123"),
                            "You entered an unknown value. Make sure the number you enter contains only digits. The number must be greater than or equal to 0.");
                    assertEquals(exception.getMessage(), "You entered an unknown value. Make sure the number you enter contains only digits. The number must be greater than or equal to 0.");
                },
                () -> {
                    NotValidArgumentException exception = assertThrows(NotValidArgumentException.class,
                            () -> mainController.submitMeterReading(expected, "HOT_WATTER", "120-20L"),
                            "You entered an unknown value. Make sure the number you enter contains only digits. The number must be greater than or equal to 0.");
                    assertEquals(exception.getMessage(), "You entered an unknown value. Make sure the number you enter contains only digits. The number must be greater than or equal to 0.");
                }
        );
    }

    @Test
    void showMeterReadingsByMonth_Success() {
        String username = "ivan";
        String password = "123";

        Player player = Player.builder()
                .id(1L)
                .username(username)
                .password(password)
                .build();

        String year = "2024";
        String month = "1";

        assertDoesNotThrow(() -> mainController.showMeterReadingsByMonth(player, year, month));
        verify(playerService, times(1)).getMeterReadingsByMonth(player.getId(), Integer.parseInt(year), Integer.parseInt(month));
    }

    @Test
    void showMeterReadingsByMonth_ThrowException() {
        String username = "ivan";
        String password = "123";

        Player player = Player.builder()
                .id(1L)
                .username(username)
                .password(password)
                .build();

        String year = "2024-1";
        String month = "XXL";

        NotValidArgumentException exception = assertThrows(NotValidArgumentException.class,
                () -> mainController.showMeterReadingsByMonth(player, year, month),
                "You entered an unknown value. Make sure the number you enter is digits only.");
        assertEquals(exception.getMessage(), "You entered an unknown value. Make sure the number you enter is digits only.");
    }

    @Test
    void showMeterReadingHistory_Success() {
        String username = "ivan";
        String password = "123";

        Player player = Player.builder()
                .id(1L)
                .username(username)
                .password(password)
                .build();

        mainController.showMeterReadingHistory(player);
        verify(playerService, times(1)).getMeterReadingHistory(player.getId());
    }
}