package com.ivan.model.entity;

import com.ivan.model.types.MeterType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.YearMonth;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeterReading {

//    private Player playerId;
    private String username; //в планах Player

    private MeterType meterType;

    private Integer counter;

    private YearMonth date;
}