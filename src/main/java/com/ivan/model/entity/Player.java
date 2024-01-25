package com.ivan.model.entity;

import com.ivan.model.types.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Calendar;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Player {

    //    private Long id;
    private String username;

    private String password;

    private Integer personalAccount;

    private Role role;

    private List<MeterReading> readingsList;

}
