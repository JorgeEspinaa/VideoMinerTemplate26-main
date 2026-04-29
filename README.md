# VideoMiner - Sistema de Minería de Datos Multimedia

## Descripción
VideoMiner es una arquitectura de tres microservicios para integrar y analizar contenido multimedia de diferentes plataformas.

## Componentes

### 1. **VideoMiner** (Puerto 8080)
Servicio central que almacena y gestiona los datos mediante API REST con H2 database.

#### Endpoints Disponibles:

**Canales:**
- `GET /api/channels` - Listar todos los canales
- `GET /api/channels/{id}` - Obtener canal por ID
- `POST /api/channels` - Crear nuevo canal
- `PUT /api/channels/{id}` - Actualizar canal
- `DELETE /api/channels/{id}` - Eliminar canal

**Videos:**
- `GET /api/videos` - Listar todos los videos
- `GET /api/videos/{id}` - Obtener video por ID
- `POST /api/videos` - Crear nuevo video
- `GET /api/videos/{videoId}/comments` - Comentarios de un video
- `GET /api/videos/{videoId}/captions` - Subtítulos de un video

**Captions:**
- `GET /api/captions` - Listar todos los subtítulos
- `GET /api/captions/{id}` - Obtener subtítulo por ID
- `POST /api/captions` - Crear nuevo subtítulo

**Comments:**
- `GET /api/comments` - Listar todos los comentarios
- `GET /api/comments/{id}` - Obtener comentario por ID
- `POST /api/comments` - Crear nuevo comentario

**Users:**
- `GET /api/users` - Listar todos los usuarios
- `GET /api/users/{id}` - Obtener usuario por ID
- `POST /api/users` - Crear nuevo usuario

#### Base de Datos:
- H2 Console: `http://localhost:8080/h2-ui`
- Usuario: `sa`
- Contraseña: (vacía)

---

### 2. **PeerTubeMiner** (Puerto 8001)
Adaptador que consume la API de PeerTube y envía datos a VideoMiner.

#### Endpoints:

**Canal de PeerTube:**
- `GET /api/channels/{id}?maxVideos=10&maxComments=2` 
  - Obtiene datos de PeerTube sin almacenarlos (lectura)
  - Ejemplo: `GET /api/channels/jpm/channels/mostlydungeon?maxVideos=5&maxComments=3`

- `POST /api/channels/{id}?maxVideos=10&maxComments=2`
  - Obtiene datos de PeerTube y los almacena en VideoMiner
  - Ejemplo: `POST /api/channels/jpm/channels/mostlydungeon?maxVideos=5&maxComments=3`

#### Parámetros:
- `maxVideos`: Límite de videos a procesar (defecto: 10)
- `maxComments`: Límite de comentarios por video (defecto: 2)

---

### 3. **DailymotionMiner** (Puerto 8002)
Adaptador que consume la API de Dailymotion y envía datos a VideoMiner.

#### Endpoints:

**Canal de Dailymotion:**
- `GET /api/channels/{id}?maxVideos=10&maxPages=2`
  - Obtiene datos de Dailymotion sin almacenarlos (lectura)
  - Ejemplo: `GET /api/channels/BBC?maxVideos=10&maxPages=2`

- `POST /api/channels/{id}?maxVideos=10&maxPages=2`
  - Obtiene datos de Dailymotion y los almacena en VideoMiner
  - Ejemplo: `POST /api/channels/BBC?maxVideos=10&maxPages=2`

#### Parámetros:
- `maxVideos`: Límite de videos a procesar (defecto: 10)
- `maxPages`: Número máximo de páginas de resultados (defecto: 2)

---

## Cómo Ejecutar

### Requisitos
- Java 17 o superior
- Maven
- Git

### Pasos

1. **Compilar cada proyecto:**
   ```bash
   # VideoMiner
   cd VideoMinerTemplate26-main
   mvnw clean install

   # PeerTubeMiner
   cd ../PeerTubeMiner
   mvnw clean install

   # DailymotionMiner
   cd ../DailymotionMiner
   mvnw clean install
   ```

2. **Ejecutar los servicios (en terminales separadas):**
   ```bash
   # VideoMiner
   cd VideoMinerTemplate26-main
   mvnw spring-boot:run

   # PeerTubeMiner
   cd PeerTubeMiner
   mvnw spring-boot:run

   # DailymotionMiner
   cd DailymotionMiner
   mvnw spring-boot:run
   ```

3. **Verificar que los servicios están corriendo:**
   - VideoMiner: `http://localhost:8080`
   - PeerTubeMiner: `http://localhost:8001`
   - DailymotionMiner: `http://localhost:8002`

---

## Flujo de Uso

### Ejemplo 1: Importar datos desde PeerTube

1. **Leer datos de PeerTube (solo lectura):**
   ```bash
   GET http://localhost:8001/api/channels/jpm/channels/mostlydungeon?maxVideos=3&maxComments=2
   ```

2. **Almacenar en VideoMiner:**
   ```bash
   POST http://localhost:8001/api/channels/jpm/channels/mostlydungeon?maxVideos=3&maxComments=2
   ```

3. **Consultar datos en VideoMiner:**
   ```bash
   GET http://localhost:8080/api/channels
   GET http://localhost:8080/api/videos
   ```

### Ejemplo 2: Importar datos desde Dailymotion

1. **Leer datos de Dailymotion (solo lectura):**
   ```bash
   GET http://localhost:8002/api/channels/BBC?maxVideos=5&maxPages=1
   ```

2. **Almacenar en VideoMiner:**
   ```bash
   POST http://localhost:8002/api/channels/BBC?maxVideos=5&maxPages=1
   ```

3. **Consultar datos en VideoMiner:**
   ```bash
   GET http://localhost:8080/api/videos/channel-id
   ```

---

## Modelo de Datos

### Channel
```json
{
  "id": "string",
  "name": "string",
  "description": "string",
  "createdTime": "string",
  "videos": []
}
```

### Video
```json
{
  "id": "string",
  "name": "string",
  "description": "string",
  "releaseTime": "string",
  "user": {},
  "comments": [],
  "captions": []
}
```

### Caption
```json
{
  "id": "string",
  "link": "string",
  "language": "string"
}
```

### Comment
```json
{
  "id": "string",
  "text": "string",
  "createdOn": "string"
}
```

### User
```json
{
  "id": "string",
  "name": "string",
  "user_link": "string",
  "picture_link": "string"
}
```

---

## Notas Importantes

- **H2 Console:** Disponible en `http://localhost:8080/h2-ui` para inspeccionar la base de datos
- **CORS:** Los servicios están configurados para comunicarse entre sí
- **URLs de las APIs externas:**
  - PeerTube: `https://framatube.org/api/v1`
  - Dailymotion: `https://api.dailymotion.com`

---

## Estructura del Proyecto

```
VideoMinerTemplate26-main/
├── VideoMinerTemplate26-main/        # Servicio principal
│   ├── src/main/java/aiss/videominer/
│   │   ├── model/                    # Clases de dominio
│   │   ├── repository/               # Interfaces JPA
│   │   ├── service/                  # Lógica de negocio
│   │   └── controller/               # REST Controllers
│   ├── src/main/resources/
│   │   └── application.properties
│   └── pom.xml
│
├── PeerTubeMiner/                    # Adaptador PeerTube
│   ├── src/main/java/aiss/peertubeiner/
│   │   ├── model/                    # DTOs de PeerTube
│   │   ├── service/                  # Servicios de integración
│   │   └── controller/               # REST Controllers
│   ├── src/main/resources/
│   │   └── application.properties
│   └── pom.xml
│
└── DailymotionMiner/                 # Adaptador Dailymotion
    ├── src/main/java/aiss/dailymotionminer/
    │   ├── model/                    # DTOs de Dailymotion
    │   ├── service/                  # Servicios de integración
    │   └── controller/               # REST Controllers
    ├── src/main/resources/
    │   └── application.properties
    └── pom.xml
```

---

## Próximos Pasos

1. ✅ Implementar pruebas unitarias (JUnit)
2. ✅ Crear colección Postman con todos los endpoints
3. ✅ Documentar API con OpenAPI/Swagger
4. ✅ Implementar autenticación (API Key)
5. ✅ Agregar paginación y filtros

---

Autores: Proyecto Arquitectura e Integración de Sistemas Software 2025/2026
