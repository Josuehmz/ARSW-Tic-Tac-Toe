package com.arsw.tictactoe.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request para hacer una jugada
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoveRequest {
    private String gameId;
    private String playerId;
    private int position;
}

