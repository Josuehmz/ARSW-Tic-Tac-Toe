package com.arsw.tictactoe.model;

/**
 * Tipos de poderes especiales que los jugadores pueden usar
 */
public enum PowerType {
    /**
     * EXTRA_TURN: Permite jugar dos veces seguidas
     */
    EXTRA_TURN("Extra Turn", "Juega dos veces seguidas"),
    
    /**
     * REMOVE_OPPONENT: Remueve una pieza del oponente del tablero
     */
    REMOVE_OPPONENT("Remove Opponent", "Elimina una pieza del oponente"),
    
    /**
     * SWAP_PIECES: Intercambia dos piezas en el tablero
     */
    SWAP_PIECES("Swap Pieces", "Intercambia dos piezas"),
    
    /**
     * REVEAL_TRAP: Revela todas las casillas trampa
     */
    REVEAL_TRAP("Reveal Trap", "Revela todas las trampas"),
    
    /**
     * BLOCK_CELL: Bloquea una casilla por 2 turnos
     */
    BLOCK_CELL("Block Cell", "Bloquea una casilla temporalmente"),
    
    /**
     * TRIPLE_PLAY: Coloca 3 piezas al mismo tiempo
     */
    TRIPLE_PLAY("Triple Play", "Coloca 3 piezas de una vez");
    
    private final String displayName;
    private final String description;
    
    PowerType(String displayName, String description) {
        this.displayName = displayName;
        this.description = description;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    public String getDescription() {
        return description;
    }
}

