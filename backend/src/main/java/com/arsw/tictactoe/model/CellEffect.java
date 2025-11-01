package com.arsw.tictactoe.model;

/**
 * Efectos que pueden tener las celdas especiales
 */
public enum CellEffect {
    NONE,           // Sin efecto
    SKIP_TURN,      // Pierde el siguiente turno
    POWER_GAINED,   // Ganó un poder
    FAKE_CELL,      // Celda falsa (desaparecerá)
    DOUBLE_POINTS,  // Puntos dobles
    REVERSE_ORDER   // Orden invertido
}

