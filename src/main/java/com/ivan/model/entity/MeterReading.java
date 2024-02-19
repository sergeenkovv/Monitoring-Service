package com.ivan.model.entity;

import com.ivan.model.types.MeterType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.YearMonth;

/**
 * The MeterReading class represents a meter reading entity.
 * It includes attributes such as ID, meter type, counter value, date, and player ID.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MeterReading {
    /**
     * The unique identifier for the meter reading
     */
    private Long id;
    /**
     * The type of the meter reading (e.g., electricity, water)
     */
    private MeterType meterType;
    /**
     * The reading value of the meter
     */
    private Integer counter;
    /**
     * The date associated with the meter reading
     */
    private YearMonth date;
    /**
     * The ID of the player to whom the meter reading belongs
     */
    private Long playerId;
}