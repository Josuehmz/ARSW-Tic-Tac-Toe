package com.arsw.tictactoe.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * Configuración de WebSocket para el juego Tic-Tac-Toe Multijugador
 * 
 * Esta clase configura:
 * - STOMP (Simple Text Oriented Messaging Protocol) sobre WebSocket
 * - Message broker para pub/sub de eventos del juego
 * - Endpoints para conexiones de clientes
 */
@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /**
     * Configura el message broker
     * - /topic: Para broadcasting a múltiples clientes (ej: actualización de juego)
     * - /app: Prefijo para mensajes dirigidos a @MessageMapping
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Habilitar un simple message broker en memoria
        config.enableSimpleBroker("/topic");
        
        // Prefijo para mensajes destinados a @MessageMapping
        config.setApplicationDestinationPrefixes("/app");
    }

    /**
     * Registra endpoints STOMP
     * - /ws: Endpoint principal para conexiones WebSocket
     * - withSockJS(): Fallback para navegadores que no soportan WebSocket nativo
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")  // Permitir conexiones desde cualquier origen (ajustar en producción)
                .withSockJS();  // Habilitar SockJS como fallback
    }
}

