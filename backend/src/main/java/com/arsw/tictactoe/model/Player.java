package com.arsw.tictactoe.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * Representa a un jugador en el juego
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Player {
    private String id;           // ID único del jugador
    private String username;     // Nombre del jugador
    private String symbol;       // Símbolo del jugador (X, O, etc.)
    private int score;           // Puntuación acumulada
    private List<PowerType> powers;  // Poderes disponibles
    private boolean isActive;    // Si es el turno del jugador
    
    public Player(String id, String username, String symbol) {
        this.id = id;
        this.username = username;
        this.symbol = symbol;
        this.score = 0;
        this.powers = new ArrayList<>();
        this.isActive = false;
    }
    
    /**
     * Agrega un poder al jugador
     */
    public void addPower(PowerType power) {
        if (this.powers == null) {
            this.powers = new ArrayList<>();
        }
        this.powers.add(power);
    }
    
    /**
     * Usa un poder (lo remueve de la lista)
     */
    public boolean usePower(PowerType power) {
        if (this.powers != null && this.powers.contains(power)) {
            this.powers.remove(power);
            return true;
        }
        return false;
    }
}

