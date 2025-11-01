package com.arsw.tictactoe.controller;

import com.arsw.tictactoe.dto.*;
import com.arsw.tictactoe.model.Game;
import com.arsw.tictactoe.model.MoveResult;
import com.arsw.tictactoe.model.Player;
import com.arsw.tictactoe.service.GameService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;

/**
 * Controlador WebSocket para manejar eventos del juego en tiempo real
 */
@Controller
public class GameController {
    
    @Autowired
    private GameService gameService;
    
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    
    /**
     * Endpoint para crear un nuevo juego
     */
    @MessageMapping("/game/create")
    @SendTo("/topic/games")
    public GameMessage createGame() {
        Game game = gameService.createGame();
        return new GameMessage(
            GameMessage.MessageType.GAME_UPDATE,
            game,
            "Nuevo juego creado: " + game.getGameId(),
            null
        );
    }
    
    /**
     * Endpoint para unirse a un juego
     */
    @MessageMapping("/game/join")
    public void joinGame(JoinGameRequest request) {
        try {
            Player player = gameService.joinGame(request.getGameId(), request.getUsername());
            Game game = gameService.getGame(request.getGameId()).orElse(null);
            
            if (game != null) {
                GameMessage message = new GameMessage(
                    GameMessage.MessageType.PLAYER_JOINED,
                    game,
                    player.getUsername() + " se unió al juego",
                    player.getId()
                );
                
                // Enviar a todos los suscriptores del juego
                messagingTemplate.convertAndSend("/topic/game/" + request.getGameId(), message);
            }
        } catch (Exception e) {
            GameMessage errorMessage = new GameMessage(
                GameMessage.MessageType.ERROR,
                null,
                e.getMessage(),
                null
            );
            messagingTemplate.convertAndSend("/topic/game/" + request.getGameId(), errorMessage);
        }
    }
    
    /**
     * Endpoint para hacer una jugada
     */
    @MessageMapping("/game/move")
    public void makeMove(MoveRequest request) {
        MoveResult result = gameService.makeMove(
            request.getGameId(),
            request.getPlayerId(),
            request.getPosition()
        );
        
        Game game = gameService.getGame(request.getGameId()).orElse(null);
        
        if (result.isSuccess() && game != null) {
            GameMessage message = new GameMessage(
                GameMessage.MessageType.MOVE_MADE,
                game,
                result.getMessage(),
                request.getPlayerId()
            );
            
            messagingTemplate.convertAndSend("/topic/game/" + request.getGameId(), message);
            
            // Si el juego terminó, enviar mensaje especial
            if (game.getStatus() == com.arsw.tictactoe.model.GameStatus.FINISHED) {
                GameMessage gameOverMessage = new GameMessage(
                    GameMessage.MessageType.GAME_OVER,
                    game,
                    "Juego terminado",
                    null
                );
                messagingTemplate.convertAndSend("/topic/game/" + request.getGameId(), gameOverMessage);
            }
        } else {
            GameMessage errorMessage = new GameMessage(
                GameMessage.MessageType.ERROR,
                game,
                result.getMessage(),
                request.getPlayerId()
            );
            messagingTemplate.convertAndSend("/topic/game/" + request.getGameId(), errorMessage);
        }
    }
    
    /**
     * Endpoint para usar un poder
     */
    @MessageMapping("/game/power")
    public void usePower(PowerRequest request) {
        boolean success = gameService.usePower(
            request.getGameId(),
            request.getPlayerId(),
            request.getPowerType(),
            request.getTargetPosition()
        );
        
        if (success) {
            Game game = gameService.getGame(request.getGameId()).orElse(null);
            if (game != null) {
                GameMessage message = new GameMessage(
                    GameMessage.MessageType.GAME_UPDATE,
                    game,
                    "Poder usado: " + request.getPowerType(),
                    request.getPlayerId()
                );
                messagingTemplate.convertAndSend("/topic/game/" + request.getGameId(), message);
            }
        }
    }
    
    /**
     * REST endpoint para obtener lista de juegos
     */
    @GetMapping("/api/games")
    @ResponseBody
    public Collection<Game> getGames() {
        return gameService.getAllGames();
    }
    
    /**
     * REST endpoint para obtener un juego específico
     */
    @GetMapping("/api/games/{gameId}")
    @ResponseBody
    public Game getGame(@PathVariable String gameId) {
        return gameService.getGame(gameId).orElse(null);
    }
    
    /**
     * REST endpoint para crear un juego
     */
    @PostMapping("/api/games")
    @ResponseBody
    public Game createGameRest() {
        return gameService.createGame();
    }
    
    /**
     * REST endpoint para reiniciar un juego
     */
    @PostMapping("/api/games/{gameId}/restart")
    @ResponseBody
    public Game restartGame(@PathVariable String gameId) {
        Game game = gameService.restartGame(gameId);
        
        // Notificar a todos los jugadores del juego
        if (game != null) {
            GameMessage message = new GameMessage(
                GameMessage.MessageType.GAME_UPDATE,
                game,
                "El juego ha sido reiniciado",
                null
            );
            messagingTemplate.convertAndSend("/topic/game/" + gameId, message);
        }
        
        return game;
    }
}

