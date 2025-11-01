package com.arsw.tictactoe.model;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Representa una partida de Tic-Tac-Toe
 */
@Data
public class Game {
    private String gameId;              // ID único del juego
    private List<Player> players;       // Lista de jugadores (2-4 jugadores)
    private List<Cell> board;           // Tablero de juego (9 celdas)
    private int currentPlayerIndex;     // Índice del jugador actual
    private GameStatus status;          // Estado del juego
    private String winner;              // ID del ganador (si hay uno)
    private LocalDateTime createdAt;    // Fecha de creación
    private LocalDateTime updatedAt;    // Última actualización
    private int turnCount;              // Contador de turnos
    private boolean specialCellsEnabled; // Si las celdas especiales están activas
    private List<String> gameLog;       // Log de eventos del juego
    
    private static final Random random = new Random();
    
    public Game(String gameId) {
        this.gameId = gameId;
        this.players = new ArrayList<>();
        this.currentPlayerIndex = 0;
        this.status = GameStatus.WAITING;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        this.turnCount = 0;
        this.specialCellsEnabled = true;  // Inicializar ANTES de crear el tablero
        this.gameLog = new ArrayList<>();
        this.board = initializeBoard();   // Ahora sí inicializar el tablero
    }
    
    /**
     * Inicializa el tablero con celdas
     * Algunas celdas tienen tipos especiales
     */
    private List<Cell> initializeBoard() {
        List<Cell> cells = new ArrayList<>();
        List<Integer> specialPositions = new ArrayList<>();
        
        // Inicializar todas las celdas como normales primero
        for (int i = 0; i < 9; i++) {
            Cell cell = new Cell(i);
            cells.add(cell);
        }
        
        if (specialCellsEnabled) {
            // Asegurar que al menos 3-4 celdas sean especiales
            int minSpecialCells = 3;
            int maxSpecialCells = 5;
            int targetSpecialCells = minSpecialCells + random.nextInt(maxSpecialCells - minSpecialCells + 1);
            
            // Seleccionar posiciones aleatorias para celdas especiales
            List<Integer> availablePositions = new ArrayList<>();
            for (int i = 0; i < 9; i++) {
                availablePositions.add(i);
            }
            Collections.shuffle(availablePositions);
            
            // Asignar tipos especiales a las posiciones seleccionadas
            CellType[] specialTypes = {CellType.TRAP, CellType.POWER, CellType.FAKE, 
                                       CellType.DOUBLE_POINTS, CellType.REVERSE};
            
            for (int i = 0; i < targetSpecialCells && i < availablePositions.size(); i++) {
                int position = availablePositions.get(i);
                Cell cell = cells.get(position);
                
                // Asignar tipo especial aleatorio
                CellType specialType = specialTypes[random.nextInt(specialTypes.length)];
                cell.setType(specialType);
                cell.setRevealed(false); // NO revelar hasta que se juegue en ella
                
                specialPositions.add(position);
                System.out.println("✨ Celda especial creada en posición " + position + ": " + specialType);
            }
            
            System.out.println("🎮 Tablero inicializado con " + specialPositions.size() + " celdas especiales: " + specialPositions);
        }
        
        return cells;
    }
    
    /**
     * Agrega un jugador al juego
     */
    public boolean addPlayer(Player player) {
        // Permitir que se unan jugadores mientras no esté terminado y no esté lleno
        if (players.size() < 4 && status != GameStatus.FINISHED) {
            players.add(player);
            
            // Si hay 2 jugadores, el juego puede comenzar
            if (players.size() == 2 && status == GameStatus.WAITING) {
                status = GameStatus.ACTIVE;
                players.get(0).setActive(true);
                addLog(players.get(0).getUsername() + " comienza el juego");
            }
            
            addLog(player.getUsername() + " se unió al juego (Jugador " + players.size() + "/4)");
            return true;
        }
        return false;
    }
    
    /**
     * Obtiene el jugador actual
     */
    public Player getCurrentPlayer() {
        if (players.isEmpty()) {
            return null;
        }
        return players.get(currentPlayerIndex);
    }
    
    /**
     * Avanza al siguiente jugador
     */
    public void nextPlayer() {
        if (players.isEmpty()) return;
        
        players.get(currentPlayerIndex).setActive(false);
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        players.get(currentPlayerIndex).setActive(true);
        turnCount++;
        updatedAt = LocalDateTime.now();
        
        // Reducir contadores de bloqueo
        for (Cell cell : board) {
            if (cell.isBlocked() && cell.getBlockedTurns() > 0) {
                cell.setBlockedTurns(cell.getBlockedTurns() - 1);
                if (cell.getBlockedTurns() <= 0) {
                    cell.setBlocked(false);
                }
            }
        }
    }
    
    /**
     * Hace una jugada en el tablero
     */
    public MoveResult makeMove(String playerId, int position) {
        MoveResult result = new MoveResult();
        
        // Validaciones
        if (status != GameStatus.ACTIVE) {
            result.setSuccess(false);
            result.setMessage("El juego no está activo");
            return result;
        }
        
        Player currentPlayer = getCurrentPlayer();
        if (!currentPlayer.getId().equals(playerId)) {
            result.setSuccess(false);
            result.setMessage("No es tu turno");
            return result;
        }
        
        if (position < 0 || position >= board.size()) {
            result.setSuccess(false);
            result.setMessage("Posición inválida");
            return result;
        }
        
        Cell cell = board.get(position);
        if (!cell.isPlayable()) {
            result.setSuccess(false);
            result.setMessage("Celda no disponible");
            return result;
        }
        
        // Hacer la jugada
        cell.setValue(currentPlayer.getSymbol());
        cell.setRevealed(true);
        
        addLog(currentPlayer.getUsername() + " jugó en posición " + position);
        
        // Aplicar efecto de la celda
        CellEffect effect = applyCellEffect(cell, currentPlayer);
        result.setCellEffect(effect);
        
        // Verificar ganador
        String winner = checkWinner();
        if (winner != null) {
            this.winner = winner;
            this.status = GameStatus.FINISHED;
            Player winnerPlayer = players.stream()
                    .filter(p -> p.getSymbol().equals(winner))
                    .findFirst()
                    .orElse(null);
            if (winnerPlayer != null) {
                winnerPlayer.setScore(winnerPlayer.getScore() + 1);
                addLog(winnerPlayer.getUsername() + " ganó el juego!");
            }
        } else if (isBoardFull()) {
            this.status = GameStatus.FINISHED;
            addLog("Empate!");
        } else {
            // Siguiente turno si el efecto no fue SKIP_TURN
            if (effect != CellEffect.SKIP_TURN) {
                nextPlayer();
            } else {
                nextPlayer(); // Salta el turno
                nextPlayer(); // Y avanza al siguiente
            }
        }
        
        result.setSuccess(true);
        result.setMessage("Jugada exitosa");
        updatedAt = LocalDateTime.now();
        
        return result;
    }
    
    /**
     * Aplica el efecto de una celda especial
     */
    private CellEffect applyCellEffect(Cell cell, Player player) {
        System.out.println("🎯 Aplicando efecto de celda tipo: " + cell.getType() + " para jugador: " + player.getUsername());
        
        switch (cell.getType()) {
            case TRAP:
                addLog("💣 ¡Trampa! " + player.getUsername() + " pierde un turno");
                System.out.println("💣 TRAMPA activada para " + player.getUsername());
                return CellEffect.SKIP_TURN;
                
            case POWER:
                PowerType randomPower = PowerType.values()[random.nextInt(PowerType.values().length)];
                player.addPower(randomPower);
                addLog("⚡ " + player.getUsername() + " ganó poder: " + randomPower.getDisplayName());
                System.out.println("⚡ PODER otorgado a " + player.getUsername() + ": " + randomPower);
                System.out.println("📋 Poderes actuales de " + player.getUsername() + ": " + player.getPowers());
                return CellEffect.POWER_GAINED;
                
            case FAKE:
                addLog("👻 ¡Celda falsa! La pieza desaparecerá pronto");
                System.out.println("👻 FAKE activada");
                return CellEffect.FAKE_CELL;
                
            case DOUBLE_POINTS:
                addLog("💎 ¡Celda de puntos dobles activada!");
                System.out.println("💎 DOUBLE_POINTS activada");
                return CellEffect.DOUBLE_POINTS;
                
            case REVERSE:
                // Invertir orden de jugadores
                addLog("🔄 ¡Orden de turnos invertido!");
                System.out.println("🔄 REVERSE activada");
                return CellEffect.REVERSE_ORDER;
                
            default:
                System.out.println("⚪ Celda normal, sin efectos");
                return CellEffect.NONE;
        }
    }
    
    /**
     * Verifica si hay un ganador
     */
    private String checkWinner() {
        int[][] winConditions = {
            {0, 1, 2}, {3, 4, 5}, {6, 7, 8}, // Filas
            {0, 3, 6}, {1, 4, 7}, {2, 5, 8}, // Columnas
            {0, 4, 8}, {2, 4, 6}             // Diagonales
        };
        
        for (int[] condition : winConditions) {
            String first = board.get(condition[0]).getValue();
            if (first != null && !first.isEmpty()) {
                if (first.equals(board.get(condition[1]).getValue()) &&
                    first.equals(board.get(condition[2]).getValue())) {
                    return first;
                }
            }
        }
        
        return null;
    }
    
    /**
     * Verifica si el tablero está lleno
     */
    private boolean isBoardFull() {
        return board.stream().allMatch(Cell::isOccupied);
    }
    
    /**
     * Agrega entrada al log del juego
     */
    private void addLog(String message) {
        if (gameLog == null) {
            gameLog = new ArrayList<>();
        }
        gameLog.add(LocalDateTime.now() + ": " + message);
    }
    
    /**
     * Remover jugador del juego
     */
    public void removePlayer(String playerId) {
        players.removeIf(p -> p.getId().equals(playerId));
        if (players.isEmpty()) {
            status = GameStatus.FINISHED;
        }
        addLog("Un jugador abandonó el juego");
    }
}

