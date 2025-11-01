# 🏗️ Arquitectura del Sistema Tic-Tac-Toe Multijugador

## 📐 Diagrama de Arquitectura General

```
┌─────────────────────────────────────────────────────────────────┐
│                        CAPA DE PRESENTACIÓN                      │
│                                                                   │
│  ┌────────────────────────────────────────────────────────────┐ │
│  │                 React Frontend (SPA)                       │ │
│  │                                                            │ │
│  │  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐   │ │
│  │  │  Components  │  │    Hooks     │  │     State    │   │ │
│  │  │   - Board    │  │  - useState  │  │   - game     │   │ │
│  │  │   - Square   │  │  - useEffect │  │   - player   │   │ │
│  │  │   - Players  │  │  - useRef    │  │   - message  │   │ │
│  │  └──────────────┘  └──────────────┘  └──────────────┘   │ │
│  │                                                            │ │
│  │  ┌──────────────────────────────────────────────────┐   │ │
│  │  │         WebSocket Client (STOMP.js)              │   │ │
│  │  │  - Conexión bidireccional                        │   │ │
│  │  │  - Suscripciones a topics                        │   │ │
│  │  │  - Publicación de mensajes                       │   │ │
│  │  └──────────────────────────────────────────────────┘   │ │
│  └────────────────────────────────────────────────────────────┘ │
└────────────────────────────┬──────────────────────────────────────┘
                             │
                             │ WebSocket/STOMP Protocol
                             │ (Comunicación Full-Duplex)
                             │
┌────────────────────────────▼──────────────────────────────────────┐
│                     CAPA DE COMUNICACIÓN                           │
│                                                                    │
│  ┌──────────────────────────────────────────────────────────────┐│
│  │              Spring WebSocket + STOMP                        ││
│  │                                                              ││
│  │  ┌────────────────────────────────────────────────────────┐ ││
│  │  │  WebSocket Config                                      │ ││
│  │  │  - Registrar endpoints (/ws)                          │ ││
│  │  │  - Configurar message broker (/topic, /app)          │ ││
│  │  │  - Habilitar SockJS fallback                         │ ││
│  │  └────────────────────────────────────────────────────────┘ ││
│  │                                                              ││
│  │  ┌────────────────────────────────────────────────────────┐ ││
│  │  │  Message Broker (Simple In-Memory)                    │ ││
│  │  │  - Routing de mensajes                                │ ││
│  │  │  - Broadcasting a suscriptores                        │ ││
│  │  │  - Gestión de sesiones                                │ ││
│  │  └────────────────────────────────────────────────────────┘ ││
│  └──────────────────────────────────────────────────────────────┘│
└────────────────────────────┬──────────────────────────────────────┘
                             │
                             │ Invoca
                             │
┌────────────────────────────▼──────────────────────────────────────┐
│                        CAPA DE APLICACIÓN                          │
│                                                                    │
│  ┌──────────────────────────────────────────────────────────────┐│
│  │                    Controllers (@Controller)                 ││
│  │                                                              ││
│  │  ┌─────────────────────────────────────────────────────┐   ││
│  │  │  GameController                                     │   ││
│  │  │                                                     │   ││
│  │  │  @MessageMapping("/game/create")                   │   ││
│  │  │  @MessageMapping("/game/join")                     │   ││
│  │  │  @MessageMapping("/game/move")                     │   ││
│  │  │  @MessageMapping("/game/power")                    │   ││
│  │  │                                                     │   ││
│  │  │  @GetMapping("/api/games")                         │   ││
│  │  │  @PostMapping("/api/games")                        │   ││
│  │  └─────────────────────────────────────────────────────┘   ││
│  └──────────────────────────────────────────────────────────────┘│
└────────────────────────────┬──────────────────────────────────────┘
                             │
                             │ Delega lógica a
                             │
┌────────────────────────────▼──────────────────────────────────────┐
│                      CAPA DE NEGOCIO                               │
│                                                                    │
│  ┌──────────────────────────────────────────────────────────────┐│
│  │                   Services (@Service)                        ││
│  │                                                              ││
│  │  ┌─────────────────────────────────────────────────────┐   ││
│  │  │  GameService                                        │   ││
│  │  │                                                     │   ││
│  │  │  + createGame()                                    │   ││
│  │  │  + getGame(gameId)                                 │   ││
│  │  │  + joinGame(gameId, username)                      │   ││
│  │  │  + makeMove(gameId, playerId, position)           │   ││
│  │  │  + usePower(gameId, playerId, power, target)      │   ││
│  │  │  + removePlayer(gameId, playerId)                 │   ││
│  │  │  + resetGame(gameId)                              │   ││
│  │  │                                                     │   ││
│  │  │  Lógica:                                           │   ││
│  │  │  - Validaciones de jugadas                        │   ││
│  │  │  - Detección de ganadores                         │   ││
│  │  │  - Aplicación de efectos de celdas               │   ││
│  │  │  - Gestión de poderes                            │   ││
│  │  │  - Control de turnos                             │   ││
│  │  └─────────────────────────────────────────────────────┘   ││
│  └──────────────────────────────────────────────────────────────┘│
└────────────────────────────┬──────────────────────────────────────┘
                             │
                             │ Opera sobre
                             │
┌────────────────────────────▼──────────────────────────────────────┐
│                      CAPA DE DOMINIO                               │
│                                                                    │
│  ┌──────────────────────────────────────────────────────────────┐│
│  │                  Modelos de Dominio                          ││
│  │                                                              ││
│  │  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐     ││
│  │  │     Game     │  │    Player    │  │     Cell     │     ││
│  │  │              │  │              │  │              │     ││
│  │  │ - gameId     │  │ - id         │  │ - position   │     ││
│  │  │ - players    │  │ - username   │  │ - value      │     ││
│  │  │ - board      │  │ - symbol     │  │ - type       │     ││
│  │  │ - status     │  │ - score      │  │ - isRevealed │     ││
│  │  │ - turnCount  │  │ - powers     │  │ - isBlocked  │     ││
│  │  │              │  │ - isActive   │  │              │     ││
│  │  │ makeMove()   │  │ addPower()   │  │ isPlayable() │     ││
│  │  │ checkWinner()│  │ usePower()   │  │              │     ││
│  │  └──────────────┘  └──────────────┘  └──────────────┘     ││
│  │                                                              ││
│  │  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐     ││
│  │  │  PowerType   │  │  CellType    │  │  GameStatus  │     ││
│  │  │   (Enum)     │  │   (Enum)     │  │   (Enum)     │     ││
│  │  │              │  │              │  │              │     ││
│  │  │ EXTRA_TURN   │  │ NORMAL       │  │ WAITING      │     ││
│  │  │ REMOVE_OPP   │  │ TRAP         │  │ ACTIVE       │     ││
│  │  │ SWAP_PIECES  │  │ POWER        │  │ PAUSED       │     ││
│  │  │ REVEAL_TRAP  │  │ FAKE         │  │ FINISHED     │     ││
│  │  │ BLOCK_CELL   │  │ DOUBLE_PTS   │  │              │     ││
│  │  │ TRIPLE_PLAY  │  │ REVERSE      │  │              │     ││
│  │  └──────────────┘  └──────────────┘  └──────────────┘     ││
│  └──────────────────────────────────────────────────────────────┘│
└────────────────────────────┬──────────────────────────────────────┘
                             │
                             │ Almacenado en
                             │
┌────────────────────────────▼──────────────────────────────────────┐
│                    CAPA DE PERSISTENCIA                            │
│                                                                    │
│  ┌──────────────────────────────────────────────────────────────┐│
│  │              ConcurrentHashMap (En Memoria)                  ││
│  │                                                              ││
│  │  Map<String, Game> games = new ConcurrentHashMap<>();       ││
│  │                                                              ││
│  │  Nota: En producción usar DB real:                          ││
│  │  - MongoDB para estructura flexible                         ││
│  │  - PostgreSQL para transacciones ACID                       ││
│  │  - Redis para caché de sesiones                            ││
│  └──────────────────────────────────────────────────────────────┘│
└─────────────────────────────────────────────────────────────────────┘
```

---

## 🔄 Flujo de Datos: Hacer una Jugada

```
┌──────────────┐
│   Usuario    │
│  (Jugador 1) │
└──────┬───────┘
       │
       │ 1. Click en celda
       ↓
┌──────────────────────────────────────┐
│         React Component              │
│     handleSquareClick(position)      │
└──────────────┬───────────────────────┘
               │
               │ 2. Publicar mensaje STOMP
               │    Destino: /app/game/move
               │    Body: {gameId, playerId, position}
               ↓
┌──────────────────────────────────────┐
│        WebSocket Client              │
│        stompClient.publish()         │
└──────────────┬───────────────────────┘
               │
               │ 3. Mensaje WebSocket
               ↓
╔══════════════════════════════════════╗
║      Spring WebSocket Server         ║
║        (Puerto 8080)                 ║
╚══════════════┬═══════════════════════╝
               │
               │ 4. Rutear mensaje
               ↓
┌──────────────────────────────────────┐
│        GameController                │
│   @MessageMapping("/game/move")      │
│   makeMove(MoveRequest request)      │
└──────────────┬───────────────────────┘
               │
               │ 5. Validar y procesar
               ↓
┌──────────────────────────────────────┐
│         GameService                  │
│   makeMove(gameId, playerId, pos)    │
│                                      │
│   1. Validar turno del jugador       │
│   2. Validar celda disponible        │
│   3. Colocar pieza                   │
│   4. Aplicar efecto de celda         │
│   5. Verificar ganador               │
│   6. Avanzar turno                   │
└──────────────┬───────────────────────┘
               │
               │ 6. Actualizar modelo
               ↓
┌──────────────────────────────────────┐
│            Game                      │
│   board.get(pos).setValue(symbol)    │
│   applyCellEffect(cell, player)      │
│   checkWinner()                      │
│   nextPlayer()                       │
└──────────────┬───────────────────────┘
               │
               │ 7. Retornar resultado
               ↓
┌──────────────────────────────────────┐
│        GameController                │
│   Crear GameMessage                  │
│   messagingTemplate.convertAndSend   │
└──────────────┬───────────────────────┘
               │
               │ 8. Broadcast a todos
               │    Destino: /topic/game/{gameId}
               ↓
╔══════════════════════════════════════╗
║         Message Broker               ║
║   Enviar a todos los suscriptores    ║
╚══════════════┬═══════════════════════╝
               │
               ├─────────────────────┬──────────────────────┐
               │                     │                      │
               ↓                     ↓                      ↓
┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐
│  Cliente 1       │  │  Cliente 2       │  │  Cliente 3       │
│  (Jugador 1)     │  │  (Jugador 2)     │  │  (Espectador)    │
└──────┬───────────┘  └──────┬───────────┘  └──────┬───────────┘
       │                     │                      │
       │ 9. Recibir mensaje  │                      │
       ↓                     ↓                      ↓
┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐
│  WebSocket       │  │  WebSocket       │  │  WebSocket       │
│  onMessage       │  │  onMessage       │  │  onMessage       │
└──────┬───────────┘  └──────┬───────────┘  └──────┬───────────┘
       │                     │                      │
       │ 10. Actualizar UI   │                      │
       ↓                     ↓                      ↓
┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐
│  React           │  │  React           │  │  React           │
│  setGame(game)   │  │  setGame(game)   │  │  setGame(game)   │
└──────┬───────────┘  └──────┬───────────┘  └──────┬───────────┘
       │                     │                      │
       │ 11. Re-render       │                      │
       ↓                     ↓                      ↓
┌──────────────────┐  ┌──────────────────┐  ┌──────────────────┐
│  UI Actualizado  │  │  UI Actualizado  │  │  UI Actualizado  │
│  ✓ Celda marcada │  │  ✓ Celda marcada │  │  ✓ Celda marcada │
│  ✓ Turno cambia  │  │  ✓ Es su turno   │  │  ✓ Ve jugada     │
└──────────────────┘  └──────────────────┘  └──────────────────┘
```

---

## 🎯 Patrones de Diseño Utilizados

### 1. **Observer Pattern** (WebSocket/STOMP)
- **Problema**: Múltiples clientes necesitan ser notificados de cambios
- **Solución**: Suscripción a topics, broadcast automático
- **Implementación**: 
  ```java
  messagingTemplate.convertAndSend("/topic/game/" + gameId, message);
  ```

### 2. **Command Pattern** (Message Mapping)
- **Problema**: Encapsular requests como objetos
- **Solución**: DTOs (MoveRequest, JoinGameRequest, PowerRequest)
- **Implementación**:
  ```java
  @MessageMapping("/game/move")
  public void makeMove(MoveRequest request) { ... }
  ```

### 3. **State Pattern** (GameStatus, CellType)
- **Problema**: Comportamiento cambia según estado
- **Solución**: Enums con estados bien definidos
- **Implementación**:
  ```java
  public enum GameStatus {
      WAITING, ACTIVE, PAUSED, FINISHED
  }
  ```

### 4. **Strategy Pattern** (Cell Effects)
- **Problema**: Diferentes efectos según tipo de celda
- **Solución**: Switch case en `applyCellEffect()`
- **Implementación**:
  ```java
  switch (cell.getType()) {
      case TRAP: return CellEffect.SKIP_TURN;
      case POWER: return CellEffect.POWER_GAINED;
      ...
  }
  ```

### 5. **Singleton Pattern** (GameService)
- **Problema**: Una sola instancia del servicio
- **Solución**: Spring @Service (singleton por defecto)
- **Implementación**:
  ```java
  @Service
  public class GameService { ... }
  ```

### 6. **Repository Pattern** (aunque simplificado)
- **Problema**: Separar lógica de persistencia
- **Solución**: ConcurrentHashMap como store
- **Implementación**:
  ```java
  private final Map<String, Game> games = new ConcurrentHashMap<>();
  ```

---

## 🔐 Consideraciones de Seguridad

### Vulnerabilidades Actuales (Proyecto Educativo)

1. **Sin Autenticación**
   - Cualquiera puede unirse con cualquier nombre
   - No hay verificación de identidad

2. **Sin Autorización**
   - Jugadores podrían hacer jugadas fuera de turno (validado en backend pero sin auth)

3. **CORS Abierto**
   ```java
   .setAllowedOriginPatterns("*")  // ⚠️ Permitir cualquier origen
   ```

4. **Sin Rate Limiting**
   - Un cliente podría enviar miles de mensajes

5. **Datos en Memoria**
   - Se pierden al reiniciar el servidor

### Mejoras Recomendadas para Producción

```java
// 1. Agregar Spring Security
@Configuration
@EnableWebSocketSecurity
public class WebSocketSecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        http
            .authorizeRequests()
            .anyRequest().authenticated()
            .and()
            .oauth2Login();
        return http.build();
    }
}

// 2. Validar jugador en cada acción
@MessageMapping("/game/move")
public void makeMove(MoveRequest request, Principal principal) {
    // Verificar que principal.getName() == request.getPlayerId()
    if (!isAuthorized(principal, request.getPlayerId())) {
        throw new UnauthorizedException();
    }
    // ... procesar jugada
}

// 3. Rate Limiting
@Component
public class RateLimitInterceptor implements ChannelInterceptor {
    private final RateLimiter rateLimiter = 
        RateLimiter.create(10.0); // 10 requests/segundo
    
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        if (!rateLimiter.tryAcquire()) {
            throw new RateLimitExceededException();
        }
        return message;
    }
}

// 4. Persistencia con MongoDB
@Document(collection = "games")
public class Game {
    @Id
    private String gameId;
    // ... resto de campos
}

@Repository
public interface GameRepository extends MongoRepository<Game, String> {
    List<Game> findByStatus(GameStatus status);
}
```

---

## 📊 Escalabilidad

### Limitaciones Actuales

1. **Single Server**
   - Todos los juegos en un servidor
   - Si se cae, se pierden todos los juegos

2. **In-Memory Storage**
   - Limitado por RAM del servidor
   - ~1000 juegos simultáneos máximo

3. **Message Broker Simple**
   - No distribuido
   - No persistente

### Arquitectura Escalable (Futuro)

```
                        ┌──────────────┐
                        │ Load Balancer│
                        │   (Nginx)    │
                        └──────┬───────┘
                               │
            ┌──────────────────┼──────────────────┐
            │                  │                  │
     ┌──────▼──────┐   ┌───────▼──────┐  ┌──────▼──────┐
     │  Server 1   │   │   Server 2   │  │  Server 3   │
     │ Spring Boot │   │ Spring Boot  │  │ Spring Boot │
     └──────┬──────┘   └───────┬──────┘  └──────┬──────┘
            │                  │                 │
            └──────────────────┼─────────────────┘
                               │
            ┌──────────────────┴──────────────────┐
            │                                     │
     ┌──────▼────────┐                   ┌───────▼──────┐
     │  RabbitMQ     │                   │  MongoDB     │
     │  (Distributed │                   │  (Cluster)   │
     │   Broker)     │                   │              │
     └───────────────┘                   └──────────────┘

Ventajas:
✓ Múltiples servidores (alta disponibilidad)
✓ RabbitMQ para mensajes distribuidos
✓ MongoDB para persistencia
✓ Escalado horizontal
```

---

## 🧪 Testing

### Estructura de Tests (Recomendada)

```java
// Unit Tests
@SpringBootTest
class GameServiceTest {
    @Test
    void testCreateGame() {
        Game game = gameService.createGame();
        assertNotNull(game.getGameId());
        assertEquals(GameStatus.WAITING, game.getStatus());
    }
    
    @Test
    void testMakeMoveValidation() {
        // Given
        Game game = gameService.createGame();
        Player player = gameService.joinGame(game.getGameId(), "Test");
        
        // When
        MoveResult result = gameService.makeMove(
            game.getGameId(), 
            "wrong-id",  // ID incorrecto
            0
        );
        
        // Then
        assertFalse(result.isSuccess());
    }
}

// Integration Tests
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
class GameControllerIntegrationTest {
    @LocalServerPort
    private int port;
    
    @Test
    void testWebSocketConnection() {
        // Probar conexión WebSocket completa
    }
}
```

---

## 📈 Métricas y Monitoreo

### Endpoints Útiles (Agregar Spring Actuator)

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-actuator</artifactId>
</dependency>
```

```properties
# application.properties
management.endpoints.web.exposure.include=health,info,metrics
```

**Endpoints disponibles:**
- `GET /actuator/health` - Estado del servidor
- `GET /actuator/metrics` - Métricas de performance
- `GET /actuator/info` - Información de la app

---

**Esta arquitectura demuestra principios sólidos de diseño de software, comunicación en tiempo real y escalabilidad incremental. 🚀**

