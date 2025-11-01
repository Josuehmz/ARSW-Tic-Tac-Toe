import { useState, useEffect, useRef } from 'react';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

function Square({ cell, onSquareClick, isActive, currentPlayerSymbol }) {
  const getCellClass = () => {
    let classes = 'square';
    
    if (cell.isBlocked) {
      classes += ' blocked';
    }
    
   
    if (cell.isRevealed && cell.type && cell.type !== 'NORMAL') {
      classes += ` cell-${cell.type.toLowerCase()}`;
    }
    
    if (cell.value) {
      classes += ' occupied';
    }
    
    return classes;
  };
  
  const getCellContent = () => {
   
    if (cell.value) {
      return (
        <div style={{ position: 'relative', width: '100%', height: '100%', display: 'flex', alignItems: 'center', justifyContent: 'center' }}>
          <span>{cell.value}</span>
          {/* Mostrar icono pequeño del tipo de celda si fue revelada */}
          {cell.isRevealed && cell.type && cell.type !== 'NORMAL' && (
            <span style={{ 
              position: 'absolute', 
              top: '2px', 
              right: '2px', 
              fontSize: '0.6em',
              opacity: 0.7
            }}>
              {getCellTypeIcon(cell.type)}
            </span>
          )}
        </div>
      );
    }
    
    
    return '';
  };
  
  const getCellTypeIcon = (type) => {
    const icons = {
      TRAP: '💣',
      POWER: '⚡',
      FAKE: '👻',
      DOUBLE_POINTS: '💎',
      REVERSE: '🔄'
    };
    return icons[type] || '';
  };
  
  return (
    <button 
      className={getCellClass()} 
      onClick={() => onSquareClick(cell.position)}
      disabled={!isActive || cell.isBlocked || cell.value}
    >
      <span className="cell-content">{getCellContent()}</span>
      {cell.isBlocked && <span className="blocked-indicator">🚫</span>}
    </button>
  );
}


function Board({ game, onSquareClick, currentPlayer, snapshot }) {
  
  const boardToShow = snapshot ? snapshot.board : game?.board;
  const isSnapshot = !!snapshot;
  
  if (!boardToShow) {
    return <div>Cargando tablero...</div>;
  }
  
  const isMyTurn = !isSnapshot && currentPlayer && game.currentPlayerIndex !== undefined 
    && game.players[game.currentPlayerIndex]?.id === currentPlayer.id;
  
  return (
    <div className={`board ${isSnapshot ? 'board-snapshot' : ''}`}>
      {isSnapshot && (
        <div className="snapshot-overlay">
          <div className="snapshot-label">Vista previa histórica</div>
        </div>
      )}
      {[0, 1, 2].map(row => (
        <div key={row} className="board-row">
          {[0, 1, 2].map(col => {
            const index = row * 3 + col;
            return (
              <Square 
                key={index}
                cell={boardToShow[index]} 
                onSquareClick={isSnapshot ? () => {} : onSquareClick}
                isActive={!isSnapshot && isMyTurn}
                currentPlayerSymbol={currentPlayer?.symbol}
              />
            );
          })}
        </div>
      ))}
    </div>
  );
}


function PowersPanel({ player, onUsePower, selectedPower, onCancelPower }) {
  
}

function getPowerIcon(power) {
  const icons = {
    EXTRA_TURN: '🔄',
    REMOVE_OPPONENT: '❌',
    SWAP_PIECES: '🔀',
    REVEAL_TRAP: '👁️',
    BLOCK_CELL: '🔒',
    TRIPLE_PLAY: '3️⃣'
  };
  return icons[power] || '⚡';
}

function getPowerDescription(power) {
  const descriptions = {
    EXTRA_TURN: 'Juega dos veces seguidas',
    REMOVE_OPPONENT: 'Elimina una pieza del oponente',
    SWAP_PIECES: 'Intercambia dos piezas',
    REVEAL_TRAP: 'Revela todas las trampas',
    BLOCK_CELL: 'Bloquea una casilla temporalmente',
    TRIPLE_PLAY: 'Coloca 3 piezas de una vez'
  };
  return descriptions[power] || 'Poder especial';
}

// Componente de lista de jugadores
function PlayersList({ game, currentPlayer }) {
  if (!game || !game.players) {
    return null;
  }
  
  return (
    <div className="players-list">
      <h3>Jugadores</h3>
      {game.players.map((player, index) => (
        <div 
          key={player.id} 
          className={`player-item ${player.isActive ? 'active' : ''} ${player.id === currentPlayer?.id ? 'current' : ''}`}
        >
          <span className="player-symbol">{player.symbol}</span>
          <span className="player-name">{player.username}</span>
          <span className="player-score">Puntos: {player.score}</span>
          {player.isActive && <span className="turn-indicator">🎯</span>}
        </div>
      ))}
    </div>
  );
}

// Componente de log del juego
function GameLog({ game, snapshots, onSelectSnapshot, viewingSnapshot }) {
  const [showAll, setShowAll] = useState(false);
  
  if (!game || !game.gameLog || game.gameLog.length === 0) {
    return null;
  }
  
  const displayedLogs = showAll 
    ? game.gameLog.map((entry, idx) => ({ entry, idx })).reverse()
    : game.gameLog.slice(-5).map((entry, idx) => ({ entry, idx: game.gameLog.length - 5 + idx })).reverse();
  
  const handleLogClick = (moveIndex) => {
    if (snapshots[moveIndex]) {
      onSelectSnapshot(moveIndex);
    }
  };
  
  return (
    <div className="game-log">
      <h3>Historial del Juego ({game.gameLog.length} jugadas)</h3>
      {viewingSnapshot !== null && (
        <div className="viewing-snapshot-banner">
          📜 Viendo jugada #{viewingSnapshot + 1}
          <button 
            className="back-to-current"
            onClick={() => onSelectSnapshot(null)}
          >
            ⏩ Volver al presente
          </button>
        </div>
      )}
      <div className="log-entries">
        {displayedLogs.map(({ entry, idx }) => (
          <div 
            key={idx} 
            className={`log-entry ${viewingSnapshot === idx ? 'selected' : ''}`}
            onClick={() => handleLogClick(idx)}
            title="Haz clic para ver el tablero en este momento"
          >
            <span className="move-number">#{idx + 1}</span> {entry}
          </div>
        ))}
      </div>
      {game.gameLog.length > 5 && (
        <button 
          className="history-toggle"
          onClick={() => setShowAll(!showAll)}
        >
          {showAll ? '📋 Mostrar últimas 5' : '📜 Ver todas las jugadas'}
        </button>
      )}
    </div>
  );
}

// Componente principal del juego
export default function Game() {
  const [game, setGame] = useState(null);
  const [currentPlayer, setCurrentPlayer] = useState(null);
  const [gameId, setGameId] = useState('');
  const [username, setUsername] = useState('');
  const [isConnected, setIsConnected] = useState(false);
  const [message, setMessage] = useState('');
  const [gameSnapshots, setGameSnapshots] = useState([]);
  const [viewingSnapshot, setViewingSnapshot] = useState(null);
  const [selectedPower, setSelectedPower] = useState(null);
  const [notification, setNotification] = useState(null);
  const stompClientRef = useRef(null);
  
  // Cargar datos del localStorage al iniciar
  useEffect(() => {
    const savedGameId = localStorage.getItem('tictactoe_gameId');
    const savedUsername = localStorage.getItem('tictactoe_username');
    const savedPlayer = localStorage.getItem('tictactoe_currentPlayer');
    
    if (savedGameId) setGameId(savedGameId);
    if (savedUsername) setUsername(savedUsername);
    if (savedPlayer) {
      try {
        setCurrentPlayer(JSON.parse(savedPlayer));
      } catch (e) {
        console.error('Error parsing saved player:', e);
      }
    }
  }, []);
  
  // Reconectar automáticamente al juego guardado
  const hasAttemptedReconnect = useRef(false);
  
  useEffect(() => {
    if (isConnected && gameId && username && !game && stompClientRef.current && !hasAttemptedReconnect.current) {
      // Esperar un poco para asegurar que el WebSocket esté listo
      const timer = setTimeout(() => {
        console.log('Intentando reconectar al juego:', gameId);
        hasAttemptedReconnect.current = true;
        joinGame();
      }, 1000);
      return () => clearTimeout(timer);
    }
  }, [isConnected]);
  
  // Guardar snapshots del juego
  useEffect(() => {
    if (game && game.board) {
      const snapshot = {
        board: JSON.parse(JSON.stringify(game.board)),
        players: JSON.parse(JSON.stringify(game.players)),
        currentPlayerIndex: game.currentPlayerIndex,
        status: game.status,
        winner: game.winner,
        moveNumber: gameSnapshots.length
      };
      setGameSnapshots(prev => [...prev, snapshot]);
    }
  }, [game?.gameLog?.length]); // Se actualiza cuando cambia el historial
  
  // Detectar cuando se obtiene un nuevo poder
  const previousPowersRef = useRef([]);
  useEffect(() => {
    if (currentPlayer && game) {
      const playerInGame = game.players.find(p => p.id === currentPlayer.id);
      if (playerInGame && playerInGame.powers) {
        const currentPowers = playerInGame.powers;
        const previousPowers = previousPowersRef.current;
        
        // Verificar si hay un poder nuevo
        if (currentPowers.length > previousPowers.length) {
          const newPower = currentPowers[currentPowers.length - 1];
          setNotification({
            type: 'power',
            message: `⚡ ¡Obtuviste un nuevo poder: ${newPower.replace(/_/g, ' ')}!`,
            icon: getPowerIcon(newPower)
          });
          
          // Ocultar la notificación después de 4 segundos
          setTimeout(() => setNotification(null), 4000);
        }
        
        previousPowersRef.current = [...currentPowers];
      }
    }
  }, [game?.players, currentPlayer]);
  
  // Conectar a WebSocket
  useEffect(() => {
    const socket = new SockJS('http://localhost:8080/ws');
    const stompClient = new Client({
      webSocketFactory: () => socket,
      debug: (str) => {
        console.log('STOMP:', str);
      },
      onConnect: () => {
        console.log('Conectado a WebSocket');
        setIsConnected(true);
      },
      onDisconnect: () => {
        console.log('Desconectado de WebSocket');
        setIsConnected(false);
      },
      onStompError: (frame) => {
        console.error('Error STOMP:', frame);
      }
    });
    
    stompClient.activate();
    stompClientRef.current = stompClient;
    
    return () => {
      if (stompClient) {
        stompClient.deactivate();
      }
    };
  }, []);
  
  // Crear nuevo juego
  const createGame = async () => {
    try {
      const response = await fetch('/api/games', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' }
      });
      
      const newGame = await response.json();
      setGameId(newGame.gameId);
      setMessage(`Juego creado: ${newGame.gameId}`);
    } catch (error) {
      console.error('Error creando juego:', error);
      setMessage('Error al crear juego');
    }
  };
  
  // Unirse a un juego
  const joinGame = () => {
    if (!gameId || !username) {
      setMessage('Ingresa ID de juego y nombre de usuario');
      return;
    }
    
    if (!stompClientRef.current) {
      setMessage('WebSocket no está inicializado');
      return;
    }
    
    // Verificar que esté conectado
    if (!stompClientRef.current.connected) {
      setMessage('Conectando al servidor... Intenta de nuevo en un momento');
      return;
    }
    
    try {
      // Guardar en localStorage
      localStorage.setItem('tictactoe_gameId', gameId);
      localStorage.setItem('tictactoe_username', username);
      
      // Suscribirse al canal del juego
      stompClientRef.current.subscribe(`/topic/game/${gameId}`, (message) => {
        const gameMessage = JSON.parse(message.body);
        console.log('Mensaje recibido:', gameMessage);
        
        if (gameMessage.game) {
          setGame(gameMessage.game);
        }
        
        // Si es el mensaje de unirse, guardar el ID del jugador
        if (gameMessage.type === 'PLAYER_JOINED' && gameMessage.playerId) {
          const player = gameMessage.game.players.find(p => p.id === gameMessage.playerId);
          if (player && player.username === username) {
            setCurrentPlayer(player);
            localStorage.setItem('tictactoe_currentPlayer', JSON.stringify(player));
            setMessage(`Te uniste como ${player.symbol}`);
          }
        }
        
        if (gameMessage.message) {
          setMessage(gameMessage.message);
        }
      });
      
      // Enviar petición de unirse
      stompClientRef.current.publish({
        destination: '/app/game/join',
        body: JSON.stringify({
          gameId: gameId,
          username: username
        })
      });
      
      setMessage('Uniéndose al juego...');
    } catch (error) {
      console.error('Error al unirse al juego:', error);
      setMessage('Error al conectar. Verifica que el backend esté corriendo.');
    }
  };
  
  // Hacer una jugada
  const handleSquareClick = (position) => {
    if (!currentPlayer || !game || !stompClientRef.current) {
      return;
    }
    
    // Si hay un poder seleccionado, usarlo en esta celda
    if (selectedPower) {
      handleCellClickWithPower(position);
      return;
    }
    
    const currentPlayerIndex = game.currentPlayerIndex;
    if (game.players[currentPlayerIndex]?.id !== currentPlayer.id) {
      setMessage('No es tu turno');
      return;
    }
    
    stompClientRef.current.publish({
      destination: '/app/game/move',
      body: JSON.stringify({
        gameId: game.gameId,
        playerId: currentPlayer.id,
        position: position
      })
    });
  };
  
  // Usar un poder
  const handleUsePower = (powerType) => {
    if (!currentPlayer || !game || !stompClientRef.current) {
      return;
    }
    
    // Determinar si el poder necesita objetivo
    const needsTarget = ['REMOVE_OPPONENT', 'BLOCK_CELL', 'REVEAL_TRAP'].includes(powerType);
    
    if (needsTarget) {
      setSelectedPower(powerType);
      setMessage(`🎯 Selecciona una celda del tablero para usar: ${getPowerDescription(powerType)}`);
    } else {
      // Usar el poder directamente
      stompClientRef.current.publish({
        destination: '/app/game/power',
        body: JSON.stringify({
          gameId: game.gameId,
          playerId: currentPlayer.id,
          powerType: powerType,
          targetPosition: -1
        })
      });
      setMessage(`⚡ Poder usado: ${powerType.replace(/_/g, ' ')}`);
    }
  };
  
  // Manejar clic en celda cuando hay un poder seleccionado
  const handleCellClickWithPower = (position) => {
    if (selectedPower) {
      stompClientRef.current.publish({
        destination: '/app/game/power',
        body: JSON.stringify({
          gameId: game.gameId,
          playerId: currentPlayer.id,
          powerType: selectedPower,
          targetPosition: position
        })
      });
      setSelectedPower(null);
      setMessage(`⚡ Poder usado en posición ${position}`);
    }
  };
  
  // Reiniciar juego (sin sacar a los jugadores)
  const handleRestart = async () => {
    if (!game || !stompClientRef.current) {
      return;
    }
    
    try {
      // Intentar reiniciar a través del backend
      const response = await fetch(`/api/games/${game.gameId}/restart`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' }
      });
      
      if (response.ok) {
        setMessage('Reiniciando el juego...');
        setGameSnapshots([]);
        setViewingSnapshot(null);
      } else {
        setMessage('Error al reiniciar el juego');
      }
    } catch (error) {
      console.error('Error reiniciando juego:', error);
      setMessage('Error al reiniciar el juego');
    }
  };
  
  // Salir del lobby y volver al menú
  const handleLeaveLobby = () => {
    // Limpiar todo el estado
    setGame(null);
    setCurrentPlayer(null);
    setGameId('');
    setMessage('');
    setGameSnapshots([]);
    setViewingSnapshot(null);
    
    // Reiniciar flag de reconexión
    hasAttemptedReconnect.current = false;
    
    // Limpiar localStorage
    localStorage.removeItem('tictactoe_gameId');
    localStorage.removeItem('tictactoe_username');
    localStorage.removeItem('tictactoe_currentPlayer');
  };
  
  // Interfaz de conexión
  if (!game) {
    return (
      <div className="game-container">
        <div className="connection-panel">
          <h1>🎮 Tic-Tac-Toe Multijugador</h1>
          
          <div className="connection-status">
            {isConnected ? (
              <span className="status-connected">✅ Conectado</span>
            ) : (
              <span className="status-disconnected">❌ Desconectado</span>
            )}
          </div>
          
          <div className="form-group">
            <input
              type="text"
              placeholder="Tu nombre"
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              className="input-field"
            />
          </div>
          
          <div className="form-group">
            <input
              type="text"
              placeholder="ID del juego"
              value={gameId}
              onChange={(e) => setGameId(e.target.value)}
              className="input-field"
            />
          </div>
          
          <div className="button-group">
            <button 
              onClick={createGame} 
              disabled={!isConnected}
              className="btn btn-primary"
            >
              Crear Nuevo Juego
            </button>
            
            <button 
              onClick={joinGame} 
              disabled={!isConnected || !gameId || !username}
              className="btn btn-success"
            >
              Unirse al Juego
            </button>
          </div>
          
          {message && (
            <div className="message-box">
              {message}
            </div>
          )}
          
          <div className="instructions">
            <h3>📋 Instrucciones</h3>
            {!isConnected && (
              <div style={{ background: '#fee2e2', color: '#991b1b', padding: '15px', borderRadius: '8px', marginBottom: '15px', border: '2px solid #ef4444' }}>
                <strong>⚠️ No se puede conectar al servidor</strong>
                <p style={{ marginTop: '8px', fontSize: '0.9rem' }}>
                  Asegúrate de que el backend esté corriendo en el puerto 8080.
                </p>
                <p style={{ marginTop: '5px', fontSize: '0.85rem' }}>
                  Ejecuta: <code style={{ background: '#fff', padding: '2px 6px', borderRadius: '3px' }}>cd backend && mvnw.cmd spring-boot:run</code>
                </p>
              </div>
            )}
            <ul>
              <li>Crea un nuevo juego o únete con un ID</li>
              <li>Espera a que se unan 2-4 jugadores</li>
              <li>Juega por turnos en el tablero</li>
              <li>Las casillas especiales otorgan poderes</li>
              <li></li>
            </ul>
          </div>
        </div>
      </div>
    );
  }
  
  // Interfaz del juego
  return (
    <div className="game-container">
      {/* Notificación flotante */}
      {notification && (
        <div className="power-notification" style={{
          position: 'fixed',
          top: '20px',
          left: '50%',
          transform: 'translateX(-50%)',
          backgroundColor: '#4ade80',
          color: 'white',
          padding: '15px 30px',
          borderRadius: '12px',
          fontSize: '1.2rem',
          fontWeight: 'bold',
          boxShadow: '0 4px 12px rgba(0,0,0,0.3)',
          zIndex: 9999,
          animation: 'slideDown 0.3s ease-out',
          textAlign: 'center'
        }}>
          <div style={{ fontSize: '2rem', marginBottom: '5px' }}>{notification.icon}</div>
          <div>{notification.message}</div>
        </div>
      )}
      
      <div className="game-header">
        <h1>🎮 Tic-Tac-Toe Multijugador</h1>
        <div className="game-id">ID del Juego: <strong>{game.gameId}</strong></div>
        <button 
          className="leave-lobby-button"
          onClick={handleLeaveLobby}
          title="Salir del juego y volver al menú principal"
        >
          ⬅️ Volver al Menú
        </button>
      </div>
      
      <div className="game-layout">
        <div className="left-panel">
          <PlayersList game={game} currentPlayer={currentPlayer} />
          <PowersPanel 
            player={currentPlayer} 
            onUsePower={handleUsePower}
            selectedPower={selectedPower}
            onCancelPower={() => {
              setSelectedPower(null);
              setMessage('Poder cancelado');
            }}
          />
        </div>
        
        <div className="center-panel">
          <div className="game-status">
            {game.status === 'WAITING' ? (
              <div className="waiting-status">
                ⏳ Esperando jugadores... ({game.players.length}/2)
                <div style={{ fontSize: '0.9rem', marginTop: '5px' }}>
                  El juego comienza con 2 jugadores mínimo
                </div>
              </div>
            ) : game.status === 'FINISHED' ? (
              <div className="game-over">
                {game.winner ? `🏆 Ganador: ${game.winner}` : '🤝 Empate'}
              </div>
            ) : (
              <div className="current-turn">
                {currentPlayer && game.players[game.currentPlayerIndex]?.id === currentPlayer.id
                  ? '🎯 ¡Tu turno!'
                  : `Turno de: ${game.players[game.currentPlayerIndex]?.username || '...'}`
                }
                <div style={{ fontSize: '0.85rem', marginTop: '5px', color: '#666' }}>
                  {game.players.length} jugadores en partida
                </div>
              </div>
            )}
          </div>
          
          <Board 
            game={game} 
            onSquareClick={handleSquareClick}
            currentPlayer={currentPlayer}
            snapshot={viewingSnapshot !== null ? gameSnapshots[viewingSnapshot] : null}
          />
          
          {game.status === 'FINISHED' && (
            <button 
              className="restart-button"
              onClick={handleRestart}
            >
              🔄 Volver a Jugar
            </button>
          )}
          
          {message && (
            <div className="status-message">
              {message}
            </div>
          )}
        </div>
        
        <div className="right-panel">
          <GameLog 
            game={game} 
            snapshots={gameSnapshots}
            onSelectSnapshot={setViewingSnapshot}
            viewingSnapshot={viewingSnapshot}
          />
          
          <div className="legend">
            <h3></h3>
            <div className="legend-item"></div>
            <div className="legend-item"></div>
            <div className="legend-item"></div>
            <div className="legend-item"></div>
            <div className="legend-item"></div>
          </div>
        </div>
      </div>
    </div>
  );
}
