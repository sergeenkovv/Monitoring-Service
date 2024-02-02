package com.ivan.model.entity;

import com.ivan.model.types.MeterType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.YearMonth;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeterReading {

    private Long id;

    private MeterType meterType;

    private Integer counter;

    private YearMonth date;

    private Long playerId;
}