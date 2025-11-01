# 📖 Instrucciones de Instalación y Ejecución

## 🔧 Instalación Completa Paso a Paso

### Paso 1: Verificar Prerrequisitos

#### Verificar Java
```bash
java -version
# Debe mostrar Java 17 o superior
```

Si no tienes Java 17:
- **Windows**: Descargar desde [Oracle](https://www.oracle.com/java/technologies/downloads/) o [AdoptOpenJDK](https://adoptium.net/)
- **macOS**: `brew install openjdk@17`
- **Linux**: `sudo apt install openjdk-17-jdk`

#### Verificar Maven
```bash
mvn -version
# Debe mostrar Maven 3.6 o superior
```

Si no tienes Maven:
- **Windows**: Descargar desde [Apache Maven](https://maven.apache.org/download.cgi)
- **macOS**: `brew install maven`
- **Linux**: `sudo apt install maven`

#### Verificar Node.js y npm
```bash
node --version
# Debe mostrar v14 o superior

npm --version
# Debe mostrar npm instalado
```

Si no tienes Node.js:
- Descargar desde [nodejs.org](https://nodejs.org/) (versión LTS recomendada)

---

### Paso 2: Clonar o Descargar el Proyecto

```bash
cd ARSW-Tic-Tac-Toe
```

---

### Paso 3: Configurar y Ejecutar el Backend

```bash
# Navegar a la carpeta del backend
cd backend

# Compilar el proyecto
mvn clean install

# Esto descargará todas las dependencias (puede tardar algunos minutos la primera vez)

# Ejecutar el servidor Spring Boot
mvn spring-boot:run
```

**Salida esperada:**
```
  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::                (v3.2.0)

2024-10-31 12:00:00.000  INFO --- [           main] c.a.t.TicTacToeApplication    : Started TicTacToeApplication in 3.456 seconds
```

El backend estará corriendo en: **http://localhost:8080**

**Dejar esta terminal abierta** - el servidor debe seguir corriendo.

---

### Paso 4: Configurar y Ejecutar el Frontend

Abrir una **NUEVA terminal** (dejar la anterior corriendo):

```bash
# Desde la raíz del proyecto (no en backend/)
cd ..  # Si estás en backend, volver a la raíz

# Instalar dependencias
npm install

# Esto puede tardar algunos minutos la primera vez

# Ejecutar el cliente React
npm start
```

**Salida esperada:**
```
Compiled successfully!

You can now view tictactoe-multiplayer in the browser.

  Local:            http://localhost:3000
  On Your Network:  http://192.168.1.X:3000

Note that the development build is not optimized.
To create a production build, use npm run build.
```

El navegador debería abrirse automáticamente en: **http://localhost:3000**

Si no se abre, abrir manualmente: http://localhost:3000

---

### Paso 5: Verificar que Todo Funciona

1. **Verificar Backend**:
   ```bash
   # En una nueva terminal
   curl http://localhost:8080/api/games
   ```
   Debe retornar: `[]` (lista vacía de juegos)

2. **Verificar Frontend**:
   - Abrir http://localhost:3000 en el navegador
   - Deberías ver la pantalla de inicio del juego
   - Estado de conexión debe mostrar: ✅ Conectado

---

## 🎮 Cómo Jugar

### Juego Local (Un Solo Navegador)

1. **Abrir dos pestañas** del navegador en http://localhost:3000

2. **En la Pestaña 1**:
   - Ingresar nombre: "Jugador 1"
   - Click en "Crear Nuevo Juego"
   - Copiar el ID del juego (ej: `a3f7d2e1`)

3. **En la Pestaña 2**:
   - Ingresar nombre: "Jugador 2"
   - Pegar el ID del juego
   - Click en "Unirse al Juego"

4. **¡Jugar!**
   - El juego comenzará automáticamente
   - Los turnos se alternan
   - Las jugadas se sincronizan en tiempo real

### Juego en Red Local

Para jugar con otra computadora en la misma red:

1. **En el Backend**, modificar `application.properties`:
   ```properties
   # Permitir conexiones de cualquier IP
   spring.web.cors.allowed-origins=*
   ```

2. **Obtener tu IP local**:
   - Windows: `ipconfig` (buscar "IPv4 Address")
   - macOS/Linux: `ifconfig` o `ip addr`
   - Ejemplo: `192.168.1.100`

3. **En el Frontend**, modificar `App.js` línea del socket:
   ```javascript
   // Cambiar
   const socket = new SockJS('http://localhost:8080/ws');
   
   // Por
   const socket = new SockJS('http://192.168.1.100:8080/ws');
   ```

4. **Jugador 1** (servidor):
   - Ejecutar backend y frontend normalmente
   - Crear juego y compartir ID

5. **Jugador 2** (cliente):
   - Abrir en navegador: `http://192.168.1.100:3000`
   - Unirse al juego con el ID compartido

---

## 🐛 Solución de Problemas

### Error: "Puerto 8080 ya está en uso"

```bash
# Windows
netstat -ano | findstr :8080
# Matar el proceso con el PID mostrado
taskkill /PID [número] /F

# macOS/Linux
lsof -ti:8080 | xargs kill -9
```

### Error: "Cannot connect to backend"

1. Verificar que el backend esté corriendo:
   ```bash
   curl http://localhost:8080/api/games
   ```

2. Verificar firewall no bloquea puerto 8080

3. Reiniciar backend:
   ```bash
   # En la terminal del backend, presionar Ctrl+C
   # Luego ejecutar de nuevo:
   mvn spring-boot:run
   ```

### Error: "WebSocket connection failed"

1. Verificar en consola del navegador (F12)
2. Buscar errores de CORS
3. Asegurar que `application.properties` tiene:
   ```properties
   spring.web.cors.allowed-origins=http://localhost:3000
   ```

### Error: "Module not found" en Frontend

```bash
# Limpiar caché y reinstalar
rm -rf node_modules package-lock.json
npm install
```

### Backend compila pero no inicia

1. Verificar versión de Java:
   ```bash
   java -version
   ```
   Debe ser 17 o superior

2. Limpiar proyecto Maven:
   ```bash
   mvn clean
   mvn install
   ```

---

## 🚀 Compilación para Producción

### Backend (JAR ejecutable)

```bash
cd backend
mvn clean package -DskipTests

# El JAR estará en:
# target/tictactoe-websocket-1.0.0.jar

# Ejecutar:
java -jar target/tictactoe-websocket-1.0.0.jar
```

### Frontend (Build optimizado)

```bash
npm run build

# Los archivos estarán en:
# build/

# Para servir estáticamente:
npm install -g serve
serve -s build -p 3000
```

---

## 📝 Notas Importantes

1. **Ambos servidores deben estar corriendo**:
   - Backend en puerto 8080
   - Frontend en puerto 3000

2. **No cerrar las terminales** mientras juegas

3. **Memoria**: El backend usa ~512MB RAM por defecto

4. **Primera ejecución**: Maven descargará ~150MB de dependencias

5. **Desarrollo**:
   - El frontend se recarga automáticamente al guardar cambios
   - El backend requiere reiniciar para ver cambios

---

## 🔄 Comandos Rápidos de Referencia

```bash
# Backend
cd backend
mvn spring-boot:run              # Iniciar servidor
mvn clean install                # Recompilar
mvn test                         # Ejecutar tests

# Frontend
npm start                        # Modo desarrollo
npm run build                    # Build producción
npm test                         # Ejecutar tests

# Verificar servicios
curl http://localhost:8080/api/games     # Backend API
curl http://localhost:3000               # Frontend
```

---

## 💡 Tips para Desarrollo

1. **Hot Reload Frontend**:
   - Cambios en archivos `.js` se aplican automáticamente
   - No necesitas refrescar el navegador

2. **DevTools Backend**:
   - Spring DevTools incluido en pom.xml
   - Cambios en Java requieren recompilar (Ctrl+F9 en IntelliJ)

3. **Logs**:
   - Backend: Logs en consola donde corre
   - Frontend: Consola del navegador (F12)

4. **Debug WebSocket**:
   - Abrir Developer Tools (F12)
   - Pestaña "Network" > filtrar "WS"
   - Ver mensajes WebSocket en tiempo real

---

**¿Listo para jugar? ¡Abre http://localhost:3000 y diviértete! 🎮**

