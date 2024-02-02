package com.ivan.service.impl;

import com.ivan.dao.MeterReadingDao;
import com.ivan.exception.DuplicateReadingsException;
import com.ivan.model.entity.MeterReading;
import com.ivan.model.types.MeterType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.YearMonth;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PlayerServiceImplTest {

    @InjectMocks
    private PlayerServiceImpl playerService;
    @Mock
    private MeterReadingDao meterReadingDao;

    @ParameterizedTest
    @MethodSource("getArguments1")
    void getCurrentMeterReadings_Test(Long playerId, List<MeterReading> meterReadingList) {
        when(meterReadingDao.findAllByPlayerIdAndYearMonth(playerId, YearMonth.now().getYear(), YearMonth.now().getMonthValue())).thenReturn(meterReadingList);

        playerService.getCurrentMeterReadings(playerId);

        verify(meterReadingDao).findAllByPlayerIdAndYearMonth(playerId, YearMonth.now().getYear(), YearMonth.now().getMonthValue());
    }

    @Test
    void submitMeterReading_Success() {
        when(meterReadingDao.findAllByPlayerId(anyLong())).thenReturn(Collections.emptyList());

        assertDoesNotThrow(() -> playerService.submitMeterReading(1L, "HOT_WATTER", 12));

        verify(meterReadingDao, times(1)).save(any(MeterReading.class));
    }

    @Test
    void submitMeterReading_ThrowException() {
        MeterReading meterReading = MeterReading.builder()
                .id(2L)
                .meterType(MeterType.valueOf("HOT_WATTER"))
                .counter(100)
                .date(YearMonth.now())
                .playerId(1L)
                .build();

        List<MeterReading> readingList = List.of(
                meterReading
        );

        when(meterReadingDao.findAllByPlayerId(anyLong())).thenReturn(readingList);

        assertThrows(DuplicateReadingsException.class, () -> playerService.submitMeterReading(1L, "HOT_WATTER", 12));

        verify(meterReadingDao, never()).save(any(MeterReading.class));
    }

    @ParameterizedTest
    @MethodSource("getArguments2")
    void getMeterReadingsByMonth(Long playerId, Integer year, Integer month, List<MeterReading> meterReadingList) {
        when(meterReadingDao.findAllByPlayerIdAndYearMonth(playerId, year, month)).thenReturn(meterReadingList);

        playerService.getMeterReadingsByMonth(playerId, year, month);

        verify(meterReadingDao).findAllByPlayerIdAndYearMonth(playerId, year, month);
    }

    @ParameterizedTest
    @MethodSource("getArguments1")
    void getMeterReadingHistory_Success(Long playerId, List<MeterReading> meterReadingList) {
        when(meterReadingDao.findAllByPlayerId(playerId)).thenReturn(meterReadingList);

        playerService.getMeterReadingHistory(playerId);

        verify(meterReadingDao).findAllByPlayerId(playerId);
    }

    static Stream<Arguments> getArguments1() {
        Long playerId1 = 1L;
        Long playerId2 = 2L;

        return Stream.of(
                Arguments.of(playerId1, Collections.singletonList(
                                MeterReading.builder()
                                        .id(1L)
                                        .meterType(MeterType.COLD_WATTER)
                                        .counter(120)
                                        .date(YearMonth.parse("2024-01"))
                                        .playerId(playerId1)
                                        .build()
                        )
                ),

                Arguments.of(playerId2, Arrays.asList(
                                MeterReading.builder()
                                        .id(2L)
                                        .meterType(MeterType.HEATING)
                                        .counter(130)
                                        .date(YearMonth.parse("2022-08"))
                                        .playerId(playerId2)
                                        .build(),

                                MeterReading.builder()
                                        .id(3L)
                                        .meterType(MeterType.HOT_WATTER)
                                        .counter(140)
                                        .date(YearMonth.parse("2023-10"))
                                        .playerId(playerId2)
                                        .build()
                        )
                )
        );
    }

    static Stream<Arguments> getArguments2() {
        Long playerId1 = 1L;
        Integer year1 = 2024;
        Integer month1 = 1;

        Long playerId2 = 3L;
        Integer year2 = 2022;
        Integer month2 = 8;

        return Stream.of(
                Arguments.of(playerId1, year1, month1, Collections.singletonList(
                                MeterReading.builder()
                                        .id(1L)
                                        .meterType(MeterType.COLD_WATTER)
                                        .counter(120)
                                        .date(YearMonth.parse("2024-01"))
                                        .playerId(playerId1)
                                        .build()
                        )
                ),

                Arguments.of(playerId2, year2, month2, Arrays.asList(
                                MeterReading.builder()
                                        .id(2L)
                                        .meterType(MeterType.HEATING)
                                        .counter(130)
                                        .date(YearMonth.parse("2022-08"))
                                        .playerId(playerId2)
                                        .build(),

                                MeterReading.builder()
                                        .id(3L)
                                        .meterType(MeterType.HOT_WATTER)
                                        .counter(140)
                                        .date(YearMonth.parse("2022-08"))
                                        .playerId(playerId2)
                                        .build()
                        )
                )
        );
    }
}