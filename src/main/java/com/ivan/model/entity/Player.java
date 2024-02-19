package com.ivan.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The Player class represents a player entity with an ID, username, and password.
 * It is used to store information about users of the system.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Player {
    /**
     * The unique identifier for the player
     */
    private Long id;
    /**
     * The username of the player
     */
    private String username;
    /**
     * The password of the player
     */
    private String password;
}