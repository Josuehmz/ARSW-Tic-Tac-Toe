package com.arsw.tictactoe.dto;

import com.arsw.tictactoe.model.Game;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Mensaje de WebSocket para actualizar el estado del juego
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class GameMessage {
    private MessageType type;
    private Game game;
    private String message;
    private String playerId;
    
    public enum MessageType {
        GAME_UPDATE,      // Actualización del juego
        PLAYER_JOINED,    // Jugador se unió
        PLAYER_LEFT,      // Jugador salió
        MOVE_MADE,        // Jugada realizada
        GAME_OVER,        // Juego terminado
        ERROR             // Error
    }
}

