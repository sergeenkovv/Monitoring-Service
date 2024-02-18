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
class PlayerControllerTest {

    @InjectMocks
    private PlayerController playerController;
    @Mock
    private PlayerService playerService;

    @Test
    void showCurrentMeterReadings_Success() {
        String username = "ivan";
        String password = "123";

        Player expected = Player.builder()
                .id(1L)
                .username(username)
                .password(password)
                .build();

        playerController.showCurrentMeterReadings(expected);
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

        assertDoesNotThrow(() -> playerController.submitMeterReading(expected, meterReading.getMeterType().name(), meterReading.getCounter().toString()));

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
                            () -> playerController.submitMeterReading(expected, "DIRTY_WATTER", "120"),
                            "You have entered an incorrect meter reading type. Check the correct spelling of the meter reading type.");
                    assertEquals(exception.getMessage(), "You have entered an incorrect meter reading type. Check the correct spelling of the meter reading type.");
                },
                () -> {
                    NotValidArgumentException exception = assertThrows(NotValidArgumentException.class,
                            () -> playerController.submitMeterReading(expected, "HOT_WATTER", "-123"),
                            "You entered an unknown value. Make sure the number you enter contains only digits. The number must be greater than or equal to 0.");
                    assertEquals(exception.getMessage(), "You entered an unknown value. Make sure the number you enter contains only digits. The number must be greater than or equal to 0.");
                },
                () -> {
                    NotValidArgumentException exception = assertThrows(NotValidArgumentException.class,
                            () -> playerController.submitMeterReading(expected, "HOT_WATTER", "120-20L"),
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

        assertDoesNotThrow(() -> playerController.showMeterReadingsByMonth(player, year, month));
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
                () -> playerController.showMeterReadingsByMonth(player, year, month),
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

        playerController.showMeterReadingHistory(player);
        verify(playerService, times(1)).getMeterReadingHistory(player.getId());
    }
}