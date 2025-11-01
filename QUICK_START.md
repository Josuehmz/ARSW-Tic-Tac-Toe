# ⚡ Quick Start Guide

## 🚀 Inicio Rápido (5 minutos)

### Requisitos
- ✅ Java 17+
- ✅ Maven 3.6+
- ✅ Node.js 14+

### Paso 1: Backend (Terminal 1)
```bash
cd backend
mvn spring-boot:run
```
✅ Backend corriendo en http://localhost:8080

### Paso 2: Frontend (Terminal 2)
```bash
npm install
npm start
```
✅ Frontend corriendo en http://localhost:3000

### Paso 3: Jugar
1. Abrir http://localhost:3000 en **DOS pestañas**
2. **Pestaña 1**: Ingresar nombre → "Crear Nuevo Juego" → Copiar ID
3. **Pestaña 2**: Ingresar nombre → Pegar ID → "Unirse al Juego"
4. ¡Jugar! 🎮

---

## 🎯 Características Principales

| Característica | Descripción |
|---|---|
| **Multijugador Real-Time** | 2-4 jugadores vía WebSockets |
| **Poderes Especiales** | 6 tipos de habilidades únicas |
| **Casillas Trampa** | 5 tipos de efectos especiales |
| **Sistema de Puntos** | Puntuación acumulada por jugador |
| **Log de Eventos** | Historial completo del juego |

---

## 📝 Poderes Disponibles

- 🔄 **Extra Turn** - Juega dos veces
- ❌ **Remove Opponent** - Elimina pieza enemiga
- 🔀 **Swap Pieces** - Intercambia piezas
- 👁️ **Reveal Trap** - Muestra trampas
- 🔒 **Block Cell** - Bloquea casilla
- 3️⃣ **Triple Play** - Coloca 3 piezas

---

## 🎲 Tipos de Casillas

- 💣 **Trampa** - Pierdes turno
- ⚡ **Poder** - Ganas habilidad
- 👻 **Falsa** - Pieza desaparece
- 💎 **Doble** - Puntos x2
- 🔄 **Reversa** - Invierte turnos

---

## 🏗️ Arquitectura

```
React Frontend ←→ WebSocket/STOMP ←→ Spring Boot Backend
                      (Puerto 8080)
```

**Tecnologías:**
- Frontend: React 19, STOMP.js, SockJS
- Backend: Spring Boot 3.2, WebSocket, Maven
- Comunicación: WebSocket full-duplex

---

## 📚 Documentación Completa

- 📖 [README.md](README.md) - Documentación técnica completa
- 🔧 [INSTRUCCIONES.md](INSTRUCCIONES.md) - Guía de instalación detallada
- 🏗️ [ARQUITECTURA.md](ARQUITECTURA.md) - Diseño del sistema

---

## 🐛 Problemas Comunes

### Backend no inicia
```bash
# Verificar Java
java -version

# Limpiar y recompilar
cd backend
mvn clean install
```

### Frontend no conecta
```bash
# Verificar backend está corriendo
curl http://localhost:8080/api/games

# Reinstalar dependencias
npm install
```

### Puerto 8080 ocupado
```bash
# Windows
netstat -ano | findstr :8080
taskkill /PID [número] /F

# macOS/Linux
lsof -ti:8080 | xargs kill -9
```

---

## 🎮 Comandos Útiles

```bash
# Backend
cd backend
mvn spring-boot:run       # Iniciar
mvn clean install         # Recompilar
mvn test                  # Tests

# Frontend
npm start                 # Desarrollo
npm run build            # Producción
npm test                 # Tests

# Verificar servicios
curl http://localhost:8080/api/games
```

---

## 💡 Tips

✅ Ambos servidores deben estar corriendo simultáneamente
✅ Usa diferentes puertos: Backend (8080), Frontend (3000)
✅ Para jugar en red local, comparte tu IP
✅ Los logs del juego se ven en tiempo real
✅ Presiona F12 para ver mensajes WebSocket

---

## 🌐 Jugar en Red Local

1. Obtener tu IP:
   ```bash
   # Windows
   ipconfig
   
   # macOS/Linux
   ifconfig
   ```

2. Otros jugadores abren:
   ```
   http://TU_IP:3000
   ```

3. Compartir ID del juego

---

## 🎯 Próximos Pasos

Después de probar el juego básico, explora:
- [ ] Usar diferentes poderes
- [ ] Jugar con 3-4 jugadores
- [ ] Experimentar con casillas especiales
- [ ] Ver el log de eventos
- [ ] Revisar el código fuente

---

## 📞 Soporte

¿Problemas? Revisa:
1. [INSTRUCCIONES.md](INSTRUCCIONES.md) - Instalación detallada
2. [README.md](README.md) - Documentación técnica
3. Console del navegador (F12) - Errores de frontend
4. Terminal del backend - Logs del servidor

---

**¡Listo para jugar! Abre http://localhost:3000 🎮**

