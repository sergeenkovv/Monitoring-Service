package com.ivan.dao.impl;

import com.ivan.dao.MeterReadingDao;
import com.ivan.model.entity.MeterReading;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MemoryMeterReadingsDaoImpl implements MeterReadingDao {

    private final Map<Long, MeterReading> readingMap = new HashMap<>();
    private Long id = 1L;

    @Override
    public List<MeterReading> findAllByPlayerUsername(String playerName) {
        List<MeterReading> result = new ArrayList<>();

        for (MeterReading reading : readingMap.values()) {
            if (reading.getUsername().equals(playerName)) {
                result.add(reading);
            }
        }
        return result;
    }

    @Override
    public List<MeterReading> findAllByPlayerUsernameAndYearMonth(String playerName, int year, int month) {
        List<MeterReading> result = new ArrayList<>();

        for (MeterReading reading : readingMap.values()) {
            if (reading.getUsername().equals(playerName)
                && reading.getDate().getYear() == year
                && reading.getDate().getMonthValue() == month) {
                result.add(reading);
            }
        }
        return result;
    }

    @Override
    public MeterReading save(MeterReading reading) {
        reading.setId(getLastId());
        incrementId();
        readingMap.put(reading.getId(), reading);
        return readingMap.get(reading.getId());
    }

    private Long getLastId() {
        return id;
    }

    private void incrementId() {
        id++;
    }
}