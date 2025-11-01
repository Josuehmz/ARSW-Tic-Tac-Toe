package com.arsw.tictactoe.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Resultado de una jugada
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoveResult {
    private boolean success;
    private String message;
    private CellEffect cellEffect;
}

