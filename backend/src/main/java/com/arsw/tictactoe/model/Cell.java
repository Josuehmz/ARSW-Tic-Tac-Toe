package com.arsw.tictactoe.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representa una celda del tablero de juego
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Cell {
    private int position;        // Posición (0-8 para tablero 3x3)
    private String value;        // Valor de la celda (null, "X", "O", etc.)
    private CellType type;       // Tipo de celda (NORMAL, TRAP, POWER, FAKE)
    private boolean isRevealed;  // Si el tipo está revelado
    private boolean isBlocked;   // Si está bloqueada temporalmente
    private int blockedTurns;    // Turnos restantes de bloqueo
    
    public Cell(int position) {
        this.position = position;
        this.value = null;
        this.type = CellType.NORMAL;
        this.isRevealed = false;
        this.isBlocked = false;
        this.blockedTurns = 0;
    }
    
    /**
     * Verifica si la celda está ocupada
     */
    public boolean isOccupied() {
        return value != null && !value.isEmpty();
    }
    
    /**
     * Verifica si se puede jugar en esta celda
     */
    public boolean isPlayable() {
        return !isOccupied() && !isBlocked;
    }
}

