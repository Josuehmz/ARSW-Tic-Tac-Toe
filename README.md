# 🎮 Tic-Tac-Toe (Tres en Raya)

Aplicación interactiva del clásico juego Tic-Tac-Toe desarrollada con React 19. Este proyecto incluye funcionalidades avanzadas como historial de movimientos y la capacidad de retroceder en el tiempo.

## 📋 Descripción

Este proyecto implementa el juego tradicional de Tres en Raya (también conocido como Tic-Tac-Toe o Ta-Te-Ti) con las siguientes características:

- 🎯 Tablero de juego interactivo 3x3
- 👥 Dos jugadores alternados (X y O)
- 🏆 Detección automática del ganador
- ⏮️ Historial completo de movimientos
- ⏰ Capacidad de volver a cualquier movimiento anterior (viaje en el tiempo)
- 🎨 Interfaz limpia y responsive

## 🎯 Características

### Funcionalidades principales:

1. **Juego Básico:**
   - Turnos alternados entre jugador X y jugador O
   - Validación de casillas ocupadas
   - Detección de victoria en filas, columnas y diagonales

2. **Historial de Movimientos:**
   - Registro de cada estado del tablero
   - Lista de movimientos con botones interactivos
   - Visualización del estado actual del juego

3. **Viaje en el Tiempo:**
   - Retrocede a cualquier movimiento anterior
   - El juego continúa desde ese punto
   - Historial se actualiza automáticamente

## 🚀 Tecnologías Utilizadas

- **React 19.2.0** - Biblioteca de JavaScript para construir interfaces de usuario
- **React Hooks** - `useState` para manejo de estado
- **React Scripts 5.0** - Herramientas de construcción y desarrollo
- **CSS3** - Estilos personalizados

## 📁 Estructura del Proyecto

```
ARSW-Tic-Tac-Toe/
│
├── public/
│   └── index.html          # HTML base
│
├── src/
│   ├── App.js              # Componentes principales del juego
│   ├── index.js            # Punto de entrada de React
│   └── styles.css          # Estilos del juego
│
├── package.json            # Dependencias y scripts
├── .gitignore             # Archivos ignorados por Git
└── README.md              # Este archivo
```

## 🎮 Componentes

### 1. `Square`
Representa cada casilla individual del tablero.
- **Props:** `value`, `onSquareClick`
- **Función:** Renderiza un botón clickeable que muestra X, O o está vacío

### 2. `Board`
Representa el tablero completo de 3x3.
- **Props:** `xIsNext`, `squares`, `onPlay`
- **Función:** 
  - Gestiona los clicks en las casillas
  - Calcula y muestra el estado del juego
  - Previene movimientos inválidos

### 3. `Game` (Componente principal)
Controla toda la lógica del juego.
- **Estado:**
  - `history`: Array con todos los estados del tablero
  - `currentMove`: Movimiento actual siendo visualizado
- **Función:**
  - Gestiona el historial completo
  - Permite viajar entre movimientos
  - Coordina Board y la lista de movimientos

### 4. `calculateWinner`
Función auxiliar que determina si hay un ganador.
- **Parámetros:** Array de 9 elementos (estado del tablero)
- **Retorna:** 'X', 'O' o null

## 🛠️ Instalación y Ejecución

### Prerrequisitos

- Node.js (versión 14 o superior)
- npm (viene incluido con Node.js)

### Pasos de instalación:

1. **Clona o descarga el repositorio:**
```bash
cd ARSW-Tic-Tac-Toe
```

2. **Instala las dependencias:**
```bash
npm install
```

3. **Inicia el servidor de desarrollo:**
```bash
npm start
```

4. **Abre tu navegador:**
   - La aplicación se abrirá automáticamente en `http://localhost:3000`
   - Si no se abre, copia y pega la URL en tu navegador

## 📜 Scripts Disponibles

En el directorio del proyecto, puedes ejecutar:

### `npm start`
Inicia la aplicación en modo desarrollo.
- Abre [http://localhost:3000](http://localhost:3000) en el navegador
- La página se recarga automáticamente al hacer cambios
- Verás los errores de lint en la consola

### `npm test`
Ejecuta los tests en modo interactivo.

### `npm run build`
Construye la aplicación para producción en la carpeta `build`.
- Optimiza el código para mejor rendimiento
- Los archivos están minificados
- Los nombres incluyen hashes para caché

### `npm run eject`
⚠️ **Nota: Esta es una operación irreversible**

Si no estás satisfecho con las herramientas de configuración, puedes hacer "eject" en cualquier momento.

## 🎲 Cómo Jugar

1. El juego comienza con el jugador **X**
2. Haz clic en cualquier casilla vacía para colocar tu marca
3. Los jugadores se alternan automáticamente (X → O → X...)
4. El primer jugador en conseguir **3 marcas en línea** (horizontal, vertical o diagonal) **gana**
5. Si todas las casillas se llenan sin ganador, es un **empate**

### Características Especiales:

- **Ver Historial:** Observa la lista de movimientos en el panel derecho
- **Volver Atrás:** Haz clic en cualquier botón del historial para volver a ese momento
- **Reiniciar:** Haz clic en "Go to game start" para comenzar de nuevo

## 🔧 Lógica de Victoria

El juego verifica las siguientes combinaciones ganadoras:

```javascript
Horizontales: [0,1,2], [3,4,5], [6,7,8]
Verticales:   [0,3,6], [1,4,7], [2,5,8]
Diagonales:   [0,4,8], [2,4,6]
```

Donde los números representan las posiciones del tablero:
```
0 | 1 | 2
---------
3 | 4 | 5
---------
6 | 7 | 8
```

## 🎨 Personalización

Puedes personalizar los estilos editando `src/styles.css`:
- Colores del tablero
- Tamaño de las casillas
- Fuentes y tipografía
- Espaciado y diseño

## 📚 Recursos de Aprendizaje

Este proyecto está basado en el tutorial oficial de React:
- [Tutorial de React - Tic-Tac-Toe](https://react.dev/learn/tutorial-tic-tac-toe)
- [Documentación de React](https://react.dev/)

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Algunas ideas para mejorar:

- [ ] Añadir detección de empate
- [ ] Resaltar la combinación ganadora
- [ ] Añadir animaciones
- [ ] Implementar modo contra IA
- [ ] Añadir sonidos
- [ ] Hacer el tablero configurable (4x4, 5x5, etc.)
- [ ] Añadir temas de color (modo oscuro/claro)
- [ ] Guardar estadísticas de partidas



