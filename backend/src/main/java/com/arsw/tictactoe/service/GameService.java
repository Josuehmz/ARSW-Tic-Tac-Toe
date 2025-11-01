package com.arsw.tictactoe.service;

import com.arsw.tictactoe.model.*;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Servicio que maneja la lógica de negocio de los juegos
 */
@Service
public class GameService {
    
    // Almacén en memoria de juegos activos
    private final Map<String, Game> games = new ConcurrentHashMap<>();
    
    // Símbolos disponibles para jugadores
    private static final String[] SYMBOLS = {"X", "O", "△", "□"};
    
    /**
     * Crea un nuevo juego
     */
    public Game createGame() {
        String gameId = UUID.randomUUID().toString().substring(0, 8);
        Game game = new Game(gameId);
        games.put(gameId, game);
        return game;
    }
    
    /**
     * Obtiene un juego por ID
     */
    public Optional<Game> getGame(String gameId) {
        return Optional.ofNullable(games.get(gameId));
    }
    
    /**
     * Obtiene todos los juegos
     */
    public Collection<Game> getAllGames() {
        return games.values();
    }
    
    /**
     * Un jugador se une a un juego
     */
    public Player joinGame(String gameId, String username) {
        Game game = games.get(gameId);
        if (game == null) {
            throw new IllegalArgumentException("Juego no encontrado");
        }
        
        if (game.getPlayers().size() >= 4) {
            throw new IllegalStateException("El juego está lleno");
        }
        
        // Crear jugador con símbolo único
        String playerId = UUID.randomUUID().toString();
        String symbol = SYMBOLS[game.getPlayers().size()];
        Player player = new Player(playerId, username, symbol);
        
        // Agregar al juego
        game.addPlayer(player);
        
        return player;
    }
    
    /**
     * Hace una jugada en el juego
     */
    public MoveResult makeMove(String gameId, String playerId, int position) {
        Game game = games.get(gameId);
        if (game == null) {
            MoveResult result = new MoveResult();
            result.setSuccess(false);
            result.setMessage("Juego no encontrado");
            return result;
        }
        
        return game.makeMove(playerId, position);
    }
    
    /**
     * Usa un poder especial
     */
    public boolean usePower(String gameId, String playerId, PowerType powerType, int targetPosition) {
        Game game = games.get(gameId);
        if (game == null) {
            return false;
        }
        
        Player player = game.getPlayers().stream()
                .filter(p -> p.getId().equals(playerId))
                .findFirst()
                .orElse(null);
        
        if (player == null || !player.usePower(powerType)) {
            return false;
        }
        
        // Aplicar efecto del poder
        switch (powerType) {
            case EXTRA_TURN:
                // El jugador no avanza al siguiente
                break;
                
            case REMOVE_OPPONENT:
                // Remover pieza del oponente
                if (targetPosition >= 0 && targetPosition < game.getBoard().size()) {
                    Cell targetCell = game.getBoard().get(targetPosition);
                    if (targetCell.isOccupied() && !targetCell.getValue().equals(player.getSymbol())) {
                        targetCell.setValue(null);
                        game.getGameLog().add(player.getUsername() + " removió una pieza enemiga");
                    }
                }
                break;
                
            case SWAP_PIECES:
                // Intercambiar dos piezas (implementación simplificada)
                game.getGameLog().add(player.getUsername() + " usó Swap Pieces");
                break;
                
            case REVEAL_TRAP:
                // Revelar todas las trampas
                game.getBoard().forEach(cell -> {
                    if (cell.getType() == CellType.TRAP) {
                        cell.setRevealed(true);
                    }
                });
                game.getGameLog().add(player.getUsername() + " reveló todas las trampas");
                break;
                
            case BLOCK_CELL:
                // Bloquear una celda
                if (targetPosition >= 0 && targetPosition < game.getBoard().size()) {
                    Cell targetCell = game.getBoard().get(targetPosition);
                    targetCell.setBlocked(true);
                    targetCell.setBlockedTurns(2);
                    game.getGameLog().add(player.getUsername() + " bloqueó una celda");
                }
                break;
                
            case TRIPLE_PLAY:
                game.getGameLog().add(player.getUsername() + " usó Triple Play");
                break;
        }
        
        return true;
    }
    
    /**
     * Remover jugador de un juego
     */
    public void removePlayer(String gameId, String playerId) {
        Game game = games.get(gameId);
        if (game != null) {
            game.removePlayer(playerId);
            
            // Si el juego está vacío, eliminarlo
            if (game.getPlayers().isEmpty()) {
                games.remove(gameId);
            }
        }
    }
    
    /**
     * Reinicia un juego existente (mantiene los jugadores)
     */
    public Game restartGame(String gameId) {
        Game oldGame = games.get(gameId);
        if (oldGame == null) {
            return null;
        }
        
        // Crear nuevo juego con los mismos jugadores
        Game newGame = new Game(gameId);
        oldGame.getPlayers().forEach(player -> {
            Player newPlayer = new Player(player.getId(), player.getUsername(), player.getSymbol());
            newPlayer.setScore(player.getScore());
            newGame.addPlayer(newPlayer);
        });
        
        games.put(gameId, newGame);
        return newGame;
    }
    
    /**
     * Reinicia un juego existente (alias para compatibilidad)
     */
    public Game resetGame(String gameId) {
        return restartGame(gameId);
    }
}

