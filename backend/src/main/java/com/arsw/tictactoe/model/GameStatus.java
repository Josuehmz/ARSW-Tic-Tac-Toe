package com.arsw.tictactoe.model;

/**
 * Estados posibles de un juego
 */
public enum GameStatus {
    WAITING,   // Esperando jugadores
    ACTIVE,    // Juego en progreso
    PAUSED,    // Juego pausado
    FINISHED   // Juego terminado
}

