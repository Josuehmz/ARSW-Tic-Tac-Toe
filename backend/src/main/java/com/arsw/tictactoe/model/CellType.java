package com.arsw.tictactoe.model;

/**
 * Tipos de celdas especiales en el tablero
 */
public enum CellType {
    /**
     * NORMAL: Celda estándar sin efectos
     */
    NORMAL,
    
    /**
     * TRAP: Al jugar aquí, pierdes un turno
     */
    TRAP,
    
    /**
     * POWER: Al jugar aquí, ganas un poder aleatorio
     */
    POWER,
    
    /**
     * FAKE: La pieza desaparece después de 2 turnos
     */
    FAKE,
    
    /**
     * DOUBLE_POINTS: Vale doble si ganas con esta celda
     */
    DOUBLE_POINTS,
    
    /**
     * REVERSE: Invierte el orden de turnos temporalmente
     */
    REVERSE
}

