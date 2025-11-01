package com.arsw.tictactoe.dto;

import com.arsw.tictactoe.model.PowerType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request para usar un poder
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PowerRequest {
    private String gameId;
    private String playerId;
    private PowerType powerType;
    private int targetPosition;  // Posición objetivo (si aplica)
}

