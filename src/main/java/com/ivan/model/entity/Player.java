package com.ivan.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Player {

    private Long id;

    private String username;

    private String password;

    //бляяя хочу сюда private List<MeterReading> meterReadingList; въебать.
//    как это сделать -- смотри в плейлисте про хибернейт
}