package com.arsw.tictactoe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request para unirse a un juego
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JoinGameRequest {
    private String gameId;
    private String username;
}

